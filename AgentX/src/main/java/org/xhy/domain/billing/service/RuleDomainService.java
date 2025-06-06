package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.billing.constant.ProductType;
import org.xhy.domain.billing.model.dto.RuleEntity;
import org.xhy.domain.billing.model.dto.RuleVersionEntity;
import org.xhy.domain.billing.model.dto.ProductEntity;
import org.xhy.domain.billing.model.config.BaseRule;
import org.xhy.domain.billing.model.config.BillingRule;
import org.xhy.domain.billing.repository.RuleRepository;
import org.xhy.domain.billing.repository.RuleVersionRepository;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RuleDomainService {

    private final RuleRepository ruleRepository;
    private final RuleVersionRepository ruleVersionRepository;
    private final ProductRepository productRepository;

    public RuleDomainService(
            RuleRepository ruleRepository,
            RuleVersionRepository ruleVersionRepository,
            ProductRepository productRepository) {
        this.ruleRepository = ruleRepository;
        this.ruleVersionRepository = ruleVersionRepository;
        this.productRepository = productRepository;
    }

    /**
     * 创建规则并关联到产品
     * @param request 创建规则请求
     * @return 创建的规则实体
     */
    public RuleEntity createRule(CreateRuleRequest request) {
        // 1. 获取产品信息
        ProductEntity product = productRepository.selectById(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品不存在");
        }

        // 2. 根据产品类型处理规则
        BaseRule rule = request.getRule();
        if (rule instanceof BillingRule) {
            if (ProductType.AGENT.name().equals(product.getProductType()) || 
                ProductType.CHAT.name().equals(product.getProductType())) {
                // 合法
            } else {
                throw new IllegalArgumentException("该产品类型不支持计费规则");
            }
        } else {
            throw new IllegalArgumentException("不支持的规则类型");
        }

        RuleEntity ruleEntity;
        boolean isNewRule = false;
        // 3. 判断产品是否已有规则
        if (product.getRuleId() != null && !product.getRuleId().isEmpty()) {
            // 已有规则，更新规则表
            ruleEntity = ruleRepository.selectById(product.getRuleId());
            if (ruleEntity == null) {
                throw new IllegalStateException("产品关联的规则不存在");
            }
            ruleEntity.setVersion(request.getVersion());
            ruleEntity.setDescription(request.getDescription());
            ruleEntity.setRule(rule);
            ruleRepository.updateById(ruleEntity);
        } else {
            // 没有规则，插入新规则
            ruleEntity = new RuleEntity();
            ruleEntity.setVersion(request.getVersion());
            ruleEntity.setDescription(request.getDescription());
            ruleEntity.setRule(rule);
            ruleRepository.insert(ruleEntity);
            isNewRule = true;
        }

        // 4. 如果是新规则，更新产品的 ruleId
        if (isNewRule) {
            LambdaUpdateWrapper<ProductEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ProductEntity::getId, request.getProductId())
                        .set(ProductEntity::getRuleId, ruleEntity.getId());
            productRepository.update(null, updateWrapper);
        }

        // 5. 插入规则版本表
        RuleVersionEntity versionEntity = new RuleVersionEntity();
        versionEntity.setRuleId(ruleEntity.getId());
        versionEntity.setVersion(request.getVersion());
        versionEntity.setDescription(request.getDescription());
        versionEntity.setRule(rule);
        versionEntity.setEffectiveAt(LocalDateTime.now());
        versionEntity.setExpiredAt(null); // 可根据业务需要设置
        ruleVersionRepository.insert(versionEntity);

        return ruleEntity;
    }

    /**
     * 创建规则版本
     * @param versionEntity 规则版本实体
     * @return 创建的规则版本实体
     */
    public RuleVersionEntity createRuleVersion(RuleVersionEntity versionEntity) {
        ruleVersionRepository.insert(versionEntity);
        return versionEntity;
    }

    /**
     * 获取规则
     * @param id 规则ID
     * @return 规则实体
     */
    public RuleEntity getRule(String id) {
        return ruleRepository.selectById(id);
    }

    /**
     * 获取规则版本
     * @param id 版本ID
     * @return 规则版本实体
     */
    public RuleVersionEntity getRuleVersion(String id) {
        return ruleVersionRepository.selectById(id);
    }

    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionEntity> getRuleVersions(String ruleId) {
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId)
               .orderByDesc(RuleVersionEntity::getCreatedAt);
        return ruleVersionRepository.selectList(wrapper);
    }

    /**
     * 获取当前有效的规则版本
     * @param ruleId 规则ID
     * @return 当前有效的规则版本
     */
    public RuleVersionEntity getCurrentRuleVersion(String ruleId) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId)
               .le(RuleVersionEntity::getEffectiveAt, now)
               .ge(RuleVersionEntity::getExpiredAt, now)
               .orderByDesc(RuleVersionEntity::getCreatedAt)
               .last("LIMIT 1");
        return ruleVersionRepository.selectOne(wrapper);
    }

    /**
     * 删除规则
     * @param id 规则ID
     * @param productId 产品ID
     */
    @Transactional
    public void deleteRule(String id, String productId) {
        // 删除规则
        ruleRepository.deleteById(id);
        
        // 删除相关的规则版本
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, id);
        ruleVersionRepository.delete(wrapper);

        // 清除产品表中的规则ID
        LambdaUpdateWrapper<ProductEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProductEntity::getId, productId)
                    .set(ProductEntity::getRuleId, null);
        productRepository.update(null, updateWrapper);
    }

    /**
     * 更新规则
     * @param id 规则ID
     * @param ruleEntity 规则实体
     */
    @Transactional
    public void updateRule(String id, RuleEntity ruleEntity) {
        RuleEntity existingRule = ruleRepository.selectById(id);
        if (existingRule == null) {
            throw new IllegalArgumentException("规则不存在");
        }
        
        existingRule.setVersion(ruleEntity.getVersion());
        existingRule.setDescription(ruleEntity.getDescription());
        existingRule.setRule(ruleEntity.getRule());
        
        ruleRepository.updateById(existingRule);
    }

    /**
     * 更新规则并创建新版本
     * @param id 规则ID
     * @param ruleEntity 规则实体
     */
    @Transactional
    public void updateRuleWithVersion(String id, RuleEntity ruleEntity) {
        // 1. 更新规则
        updateRule(id, ruleEntity);
        
        // 2. 创建新的规则版本
        RuleVersionEntity versionEntity = new RuleVersionEntity();
        versionEntity.setRuleId(id);
        versionEntity.setVersion(ruleEntity.getVersion());
        versionEntity.setDescription(ruleEntity.getDescription());
        versionEntity.setRule(ruleEntity.getRule());
        versionEntity.setEffectiveAt(LocalDateTime.now());
        versionEntity.setExpiredAt(null);
        
        ruleVersionRepository.insert(versionEntity);
    }
} 
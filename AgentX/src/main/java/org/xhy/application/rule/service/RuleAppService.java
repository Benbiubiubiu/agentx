package org.xhy.application.rule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.rule.assembler.RuleAssembler;
import org.xhy.application.billing.assembler.RuleVersionAssembler;
import org.xhy.application.rule.dto.RuleDTO;
import org.xhy.domain.product.constant.ProductType;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.rule.model.dto.RuleEntity;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.service.RuleDomainService;
import org.xhy.domain.rule.service.RuleVersionDomainService;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import org.xhy.domain.product.service.ProductDomainService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 规则应用服务
 * 用于适配领域层的规则服务
 * 职责：
 * 1. 接收和验证来自接口层的请求
 * 2. 将请求转换为领域对象或参数
 * 3. 调用领域服务执行业务逻辑
 * 4. 转换和返回结果给接口层
 */
@Service
public class RuleAppService {

    private final RuleDomainService ruleDomainService;
    private final ProductDomainService productDomainService;
    private final RuleVersionDomainService ruleVersionDomainService;

    public RuleAppService(RuleDomainService ruleDomainService,
                          ProductDomainService productDomainService, RuleVersionDomainService ruleVersionDomainService) {
        this.ruleDomainService = ruleDomainService;
        this.productDomainService = productDomainService;
        this.ruleVersionDomainService = ruleVersionDomainService;
    }

    /**
     * 创建规则并关联到产品
     * @param request 创建规则请求
     * @return 创建的规则实体
     */
    @Transactional
    public void createRule(CreateRuleRequest request) {
        // 1. 获取产品信息
        ProductEntity product = productDomainService.getProduct(request.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("产品不存在");
        }

        // 2. 验证产品类型是否支持计费规则
        if (!ProductType.AGENT.name().equals(product.getProductType()) &&
            !ProductType.CHAT.name().equals(product.getProductType())) {
            throw new IllegalArgumentException("该产品类型不支持计费规则");
        }

        RuleEntity ruleEntity;
        boolean isNewRule = false;
        // 3. 判断产品是否已有规则
        if (product.getRuleId() != null && !product.getRuleId().isEmpty()) {
            // 已有规则，更新规则表
            ruleEntity = ruleDomainService.getRule(product.getRuleId());
            if (ruleEntity == null) {
                throw new IllegalStateException("产品关联的规则不存在");
            }
            // 使用 toEntity 创建新实体
            RuleEntity newEntity = RuleAssembler.toEntity(request);
            newEntity.setId(ruleEntity.getId());
            ruleDomainService.updateRule(product.getRuleId(), newEntity);
            ruleEntity = newEntity;
        } else {
            // 没有规则，插入新规则
            ruleEntity = RuleAssembler.toEntity(request);
            ruleDomainService.createRule(ruleEntity);
            isNewRule = true;
        }

        // 4. 如果是新规则，更新产品的 ruleId
        if (isNewRule) {
            productDomainService.updateProductRuleId(request.getProductId(), ruleEntity.getId());
        }

        // 5. 创建规则版本
        RuleVersionEntity versionEntity = RuleVersionAssembler.toEntity(ruleEntity.getId(), request, LocalDateTime.now(), null);
        ruleVersionDomainService.createRuleVersion(versionEntity);
    }

    /**
     * 获取规则信息
     * @param ruleId 规则ID
     * @return 规则信息
     */
    public RuleDTO getRule(String ruleId) {
        RuleEntity rule = ruleDomainService.getRule(ruleId);
        return RuleAssembler.toDTO(rule);
    }

    /**
     * 更新规则
     * @param ruleId 规则ID
     * @param request 更新规则请求
     */
    @Transactional
    public void updateRule(String ruleId, CreateRuleRequest request) {
        // 将请求转换为领域实体
        RuleEntity ruleEntity = RuleAssembler.toEntity(request);
        // 调用领域服务更新规则
        ruleDomainService.updateRule(ruleId, ruleEntity);
        
        // 创建规则版本
        RuleVersionEntity ruleVersion = RuleVersionAssembler.toEntity(ruleId, request, LocalDateTime.now(), null);
        ruleVersionDomainService.createRuleVersion(ruleVersion);
    }

    /**
     * 删除规则
     * @param ruleId 规则ID
     * @param productId 产品ID
     */
    @Transactional
    public void deleteRule(String ruleId, String productId) {
        ruleDomainService.deleteRule(ruleId, productId);
        ruleVersionDomainService.deleteRuleVersions(ruleId);
    }

    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionEntity> getRuleVersions(String ruleId) {
        return ruleVersionDomainService.getRuleVersions(ruleId);
    }

    /**
     * 获取当前有效的规则版本
     * @param ruleId 规则ID
     * @return 当前有效的规则版本
     */
    public RuleVersionEntity getCurrentRuleVersion(String ruleId) {
        return ruleVersionDomainService.getCurrentRuleVersion(ruleId);
    }

    /**
     * 创建规则版本
     * @param ruleId 规则ID
     * @param request 创建规则请求
     * @param effectiveAt 生效时间
     * @param expiredAt 过期时间
     * @return 创建的规则版本
     */
    @Transactional
    public RuleVersionEntity createRuleVersion(String ruleId, CreateRuleRequest request,
                                             LocalDateTime effectiveAt, LocalDateTime expiredAt) {
        // 将请求转换为领域实体
        RuleVersionEntity versionEntity = RuleVersionAssembler.toEntity(ruleId, request, effectiveAt, expiredAt);
        // 调用领域服务创建规则版本
        return ruleVersionDomainService.createRuleVersion(versionEntity);
    }
} 
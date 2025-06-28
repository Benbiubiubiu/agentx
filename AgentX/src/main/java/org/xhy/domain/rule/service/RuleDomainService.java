package org.xhy.domain.rule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.xhy.domain.rule.model.dto.RuleEntity;
import org.xhy.domain.rule.repository.RuleRepository;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import org.xhy.domain.product.repository.ProductRepository;
import org.xhy.infrastructure.exception.BusinessException;

/**
 * 规则领域服务
 */
@Service
public class RuleDomainService {

    private final RuleRepository ruleRepository;

    public RuleDomainService(
            RuleRepository ruleRepository
            ) {
        this.ruleRepository = ruleRepository;
        ;
    }
    /**
     * 创建规则并关联到产品
     * @param entity 创建规则请求
     * @return 创建的规则实体
     */
    public RuleEntity createRule(RuleEntity entity) {
        ruleRepository.checkInsert(entity);
        return entity;
    }

    /**
     * 获取规则
     * @param id 规则ID
     * @return 规则实体
     */
    public RuleEntity getRule(String id) {
        RuleEntity rule = ruleRepository.selectById(id);
        if (rule == null) {
            throw new BusinessException("规则不存在");
        }
        return rule;
    }



    /**
     * 删除规则
     * @param id 规则ID
     */
    public void deleteRule(String id) {
        // 删除规则
        LambdaQueryWrapper<RuleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleEntity::getId, id);
        ruleRepository.checkedDelete(wrapper);
    }

    /**
     * 更新规则
     * @param ruleEntity 规则实体
     */
    public void updateRule(RuleEntity ruleEntity) {
        RuleEntity existingRule = ruleRepository.selectById(ruleEntity.getId());
        if (existingRule == null) {
            throw new IllegalArgumentException("规则不存在");
        }
        
        existingRule.setVersion(ruleEntity.getVersion());
        existingRule.setDescription(ruleEntity.getDescription());
        existingRule.setRule(ruleEntity.getRule());
        
        ruleRepository.checkedUpdateById(existingRule);
    }
} 
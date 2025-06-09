package org.xhy.domain.rule.service;

import org.springframework.stereotype.Service;
import org.xhy.domain.rule.model.dto.RuleEntity;
import org.xhy.domain.rule.repository.RuleRepository;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import org.xhy.domain.product.repository.ProductRepository;

@Service
public class RuleDomainService {

    private final RuleRepository ruleRepository;
    private final ProductRepository productRepository;

    public RuleDomainService(
            RuleRepository ruleRepository,
            RuleVersionRepository ruleVersionRepository,
            ProductRepository productRepository) {
        this.ruleRepository = ruleRepository;
        this.productRepository = productRepository;
    }

    /**
     * 创建规则并关联到产品
     * @param entity 创建规则请求
     * @return 创建的规则实体
     */
    public void createRule(RuleEntity entity) {
        ruleRepository.insert(entity);
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
     * 删除规则
     * @param id 规则ID
     * @param productId 产品ID
     */
    public void deleteRule(String id, String productId) {
        // 删除规则
        ruleRepository.deleteById(id);
    }

    /**
     * 更新规则
     * @param id 规则ID
     * @param ruleEntity 规则实体
     */
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
    public void updateRuleWithVersion(String id, RuleEntity ruleEntity) {
        // 1. 更新规则
        updateRule(id, ruleEntity);

    }
} 
package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.RuleAssembler;
import org.xhy.application.billing.assembler.RuleVersionAssembler;
import org.xhy.application.billing.dto.RuleDTO;
import org.xhy.domain.billing.model.dto.RuleEntity;
import org.xhy.domain.billing.model.dto.RuleVersionEntity;
import org.xhy.domain.billing.service.RuleDomainService;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;

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

    public RuleAppService(RuleDomainService ruleService) {
        this.ruleDomainService = ruleService;
    }

    /**
     * 创建规则
     * @param request 创建规则请求
     * @return 创建的规则
     */
    @Transactional
    public RuleDTO createRule(CreateRuleRequest request) {
        // 调用领域服务创建规则
        RuleEntity rule = ruleDomainService.createRule(request);
        
        // 转换为DTO
        return RuleAssembler.toDTO(rule);
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
        ruleDomainService.updateRuleWithVersion(ruleId, ruleEntity);
    }

    /**
     * 删除规则
     * @param ruleId 规则ID
     * @param productId 产品ID
     */
    @Transactional
    public void deleteRule(String ruleId, String productId) {
        ruleDomainService.deleteRule(ruleId, productId);
    }

    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionEntity> getRuleVersions(String ruleId) {
        return ruleDomainService.getRuleVersions(ruleId);
    }

    /**
     * 获取当前有效的规则版本
     * @param ruleId 规则ID
     * @return 当前有效的规则版本
     */
    public RuleVersionEntity getCurrentRuleVersion(String ruleId) {
        return ruleDomainService.getCurrentRuleVersion(ruleId);
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
        return ruleDomainService.createRuleVersion(versionEntity);
    }
} 
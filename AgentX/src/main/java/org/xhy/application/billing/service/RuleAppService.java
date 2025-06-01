package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.RuleAssembler;
import org.xhy.application.billing.dto.RuleDTO;
import org.xhy.domain.billing.entity.RuleEntity;
import org.xhy.domain.billing.entity.RuleVersionEntity;
import org.xhy.domain.billing.service.RuleService;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import org.xhy.interfaces.dto.billing.PageRequest;
import org.xhy.interfaces.dto.billing.PageResult;

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

    private final RuleService ruleService;

    public RuleAppService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    /**
     * 创建规则
     * @param request 创建规则请求
     * @return 创建的规则
     */
    @Transactional
    public RuleDTO createRule(CreateRuleRequest request) {
        // 调用领域服务创建规则
        RuleEntity rule = ruleService.createRule(request);
        
        // 转换为DTO
        return RuleAssembler.toDTO(rule);
    }

    /**
     * 获取规则信息
     * @param ruleId 规则ID
     * @return 规则信息
     */
    public RuleDTO getRule(String ruleId) {
        RuleEntity rule = ruleService.getRule(ruleId);
        return RuleAssembler.toDTO(rule);
    }

    /**
     * 更新规则
     * @param ruleId 规则ID
     * @param request 更新规则请求
     * @return 更新后的规则
     */
    @Transactional
    public RuleDTO updateRule(String ruleId, CreateRuleRequest request) {
        // 调用领域服务更新规则
        RuleEntity ruleEntity = ruleService.updateRule(ruleId, request);
        
        // 转换为DTO
        return RuleAssembler.toDTO(ruleEntity);
    }

    /**
     * 删除规则
     * @param ruleId 规则ID
     */
    @Transactional
    public void deleteRule(String ruleId) {
        ruleService.deleteRule(ruleId);
    }

    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionEntity> getRuleVersions(String ruleId) {
        return ruleService.getRuleVersions(ruleId);
    }

    /**
     * 获取当前有效的规则版本
     * @param ruleId 规则ID
     * @return 当前有效的规则版本
     */
    public RuleVersionEntity getCurrentRuleVersion(String ruleId) {
        return ruleService.getCurrentRuleVersion(ruleId);
    }
} 
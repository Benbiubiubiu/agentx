package org.xhy.interfaces.api.portal.rule;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.rule.dto.RuleAggregateDTO;
import org.xhy.application.rule.service.RuleAppService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.rule.CreateRuleRequest;

/**
 * 规则模块管理
 **/
@RestController
@RequestMapping("/rule")
public class RuleController {
    private final RuleAppService ruleAppService;

    public RuleController(RuleAppService ruleAppService) {
        this.ruleAppService = ruleAppService;
    }

    /**
     * 创建新规则
     * @param request 创建规则请求
     * @return 创建结果
     */
    @PostMapping("/rules")
    public Result createRule(@Validated @RequestBody CreateRuleRequest request) {
        ruleAppService.createRule(request);
        return Result.success();
    }

    /**
     * 更新规则
     * @param id 规则ID
     * @param request 更新规则请求
     * @return 更新结果
     */
    @PutMapping("/rules/{id}")
    public Result updateRule(@PathVariable String id, @Validated @RequestBody CreateRuleRequest request) {
        ruleAppService.updateRule(id, request);
        return Result.success();
    }

    /**
     * 删除规则(产品相关）
     * @param id 规则ID
     * @param productId 产品ID
     * @return 删除结果
     */
    @DeleteMapping("/rules/{id}")
    public Result deleteRule(@PathVariable String id, @RequestParam String productId) {
        ruleAppService.deleteRuleWithProductUnbind(id, productId);
        return Result.success();
    }

    /**
     * 获取规则聚合根信息
     * @param id 规则ID
     * @return 规则聚合根信息
     */
    @GetMapping("/rules/{id}")
    public Result getRuleAggregate(@PathVariable String id) {
        RuleAggregateDTO aggregate = ruleAppService.getRuleAggregate(id);
        return Result.success(aggregate);
    }


}

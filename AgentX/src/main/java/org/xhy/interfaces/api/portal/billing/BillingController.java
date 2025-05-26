package org.xhy.interfaces.api.portal.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.billing.BillingRecordApplicationService;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.service.ProductService;
import org.xhy.domain.billing.service.RuleService;
import org.xhy.domain.billing.service.UserBillingCountService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.*;
import org.xhy.infrastructure.auth.UserContext;

import java.util.HashMap;
import java.util.Map;

/**
 * XHY
 * 2025/5/18 12:05
 * 用户账单管理
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@RestController
@RequestMapping("/billing")
public class BillingController {
    @Autowired
    private BillingRecordApplicationService billingRecordApplicationService;
    
    @Autowired
    private UserBillingCountService userBillingCountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RuleService ruleService;

    /**
     * 查询用户账单记录列表
     * @param request
     * @return
     */

    @GetMapping("/query")
    public Result<PageResult<RecordListDTO>> queryRecords(RecordQueryRequest request) {
        request.setUserId(UserContext.getCurrentUserId());
        PageResult<RecordListDTO> result = billingRecordApplicationService.queryRecords(request);
        return Result.success(result);
    }

    /**
     * 查询用户余额信息
     * @return
     */
    @GetMapping("/balance")
    public Result<Map<String, Object>> queryBalance() {
        String userId = UserContext.getCurrentUserId();
        UserBillingCountEntity balance = userBillingCountService.queryBalance(userId);
        
        Map<String, Object> data = new HashMap<>();
        if (balance != null) {
            data.put("balance", balance.getBalance());
            data.put("cumulativeRechargeAmount", balance.getCumulativeRechargeAmount());
            data.put("lastTransactionAt", balance.getLastTransactionAt());
        }
        
        return Result.success(data);
    }

    /**
     * 创建新产品
     * @param request 创建产品请求
     * @return 创建结果
     */
    @PostMapping("/create_product")
    public Result createProduct(@RequestBody CreateProductRequest request) {
        productService.createProduct(request);
        return Result.success();
    }

    /**
     * 创建新规则
     * @param request
     * @return
     */
    @PostMapping("/create_rule")
    public Result createRule(@RequestBody CreateRuleRequest request) {
        ruleService.createRule(request);
        return Result.success();
    }
}

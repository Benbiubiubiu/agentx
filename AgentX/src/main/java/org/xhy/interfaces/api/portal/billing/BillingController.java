package org.xhy.interfaces.api.portal.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.domain.billing.service.ProductService;
import org.xhy.domain.billing.service.RuleService;
import org.xhy.domain.billing.service.UserBillingCountService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.*;
import org.xhy.infrastructure.auth.UserContext;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 计费模块管理
 */
@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingRecordDomainService billingRecordApplicationService;
    
    @Autowired
    private UserBillingCountService userBillingCountService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RuleService ruleService;

    /**
     * 查询用户账单记录列表
     * @param request 查询请求
     * @return 账单记录列表
     */
    @GetMapping("/query")
    public Result<PageResult<RecordListDTO>> queryRecords(@Validated @RequestBody PageRequest request) {
        String userId = UserContext.getCurrentUserId();
        PageResult<RecordListDTO> result = billingRecordApplicationService.queryRecords(userId,request);
        return Result.success(result);
    }

    /**
     * 查询用户余额信息
     * @return 余额信息
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
    public Result createProduct(@Validated @RequestBody CreateProductRequest request) {
        productService.createProduct(request);
        return Result.success();
    }

    /**
     * 查询产品列表
     * @param pageRequest 分页请求参数
     * @return 产品列表
     */
    @GetMapping("/product_list")
    public Result<PageResult<ProductListDTO>> queryProducts(PageRequest pageRequest) {
        PageResult<ProductListDTO> result = productService.queryProducts(pageRequest.getPage(), pageRequest.getSize());
        return Result.success(result);
    }

    /**
     * 查询我的产品列表
     * @param pageRequest 分页请求参数
     * @return 我的产品列表
     */
    @GetMapping("/product_list/my_products")
    public Result<PageResult<ProductListDTO>> queryMyProducts(PageRequest pageRequest) {
        String userId = UserContext.getCurrentUserId();
        PageResult<ProductListDTO> result = productService.queryMyProducts(userId, pageRequest.getPage(), pageRequest.getSize());
        return Result.success(result);
    }

    /**
     * 创建新规则
     * @param request 创建规则请求
     * @return 创建结果
     */
    @PostMapping("/create_rule")
    public Result createRule(@Validated @RequestBody CreateRuleRequest request) {
        ruleService.createRule(request);
        return Result.success();
    }

    /**
     * 更新产品信息
     * @param id 产品ID
     * @param request 更新产品请求
     * @return 更新结果
     */
    @PutMapping("/product/{id}")
    public Result updateProduct(@PathVariable String id, @Validated @RequestBody CreateProductRequest request) {
        productService.updateProduct(id, request);
        return Result.success();
    }

    /**
     * 更新规则
     * @param id 规则ID
     * @param request 更新规则请求
     * @return 更新结果
     */
    @PutMapping("/rule/{id}")
    public Result updateRule(@PathVariable String id, @Validated @RequestBody CreateRuleRequest request) {
        // 更新规则
        ruleService.updateRule(id, request);
        // 创建新的规则版本
        ruleService.createRuleVersion(id, request, LocalDateTime.now(), null);
        return Result.success();
    }
}

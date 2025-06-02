package org.xhy.interfaces.api.portal.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.application.billing.service.BillingRecordAppService;
import org.xhy.application.billing.service.ProductAppService;
import org.xhy.domain.billing.service.ProductService;
import org.xhy.domain.billing.service.RuleService;
import org.xhy.domain.billing.service.UserBillingCountService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.*;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.application.billing.service.UserBillingCountAppService;
import org.xhy.interfaces.dto.Page;

import java.time.LocalDateTime;

/**
 * 计费模块管理
 */
@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingRecordAppService billingRecordAppService;
    private final ProductAppService productAppService;
    private final RuleService ruleService;
    private final UserBillingCountAppService userBillingCountAppService;

    public BillingController(
            BillingRecordAppService billingRecordAppService,
            ProductAppService productAppService,
            RuleService ruleService,
            UserBillingCountAppService userBillingCountAppService
    ) {
        this.billingRecordAppService = billingRecordAppService;
        this.productAppService = productAppService;
        this.ruleService = ruleService;
        this.userBillingCountAppService = userBillingCountAppService;
    }

    /**
     * 查询用户账单记录列表
     * @param page 查询请求
     * @return 账单记录列表
     */
    @GetMapping("/query")
    public Result<PageResult<BillingRecordDTO>> queryRecords(@RequestBody Page page) {
        PageResult<BillingRecordDTO> result = billingRecordAppService.queryRecords(page);
        return Result.success(result);
    }

    /**
     * 查询用户余额
     * @return 余额信息
     */
    @GetMapping("/balance")
    public Result<BalanceDTO> queryBalance() {
        String userId = UserContext.getCurrentUserId();
        BalanceDTO balance = userBillingCountAppService.queryBalance(userId);
        return Result.success(balance);
    }

    /**
     * 创建新产品
     * @param request 创建产品请求
     * @return 创建结果
     */
    @PostMapping("/create_product")
    public Result createProduct(@Validated @RequestBody CreateProductRequest request) {
        productAppService.createProduct(request);
        return Result.success();
    }

    /**
     * 查询产品列表
     * @param page 分页请求参数
     * @return 产品列表
     */
    @GetMapping("/product_list")
    public Result<PageResult<ProductListDTO>> queryProducts(@RequestBody Page page) {
        PageResult<ProductListDTO> result = productAppService.queryProducts(page);
        return Result.success(result);
    }

    /**
     * 查询我的产品列表
     * @param page 分页请求参数
     * @return 我的产品列表
     */
    @GetMapping("/product_list/my_products")
    public Result<PageResult<ProductListDTO>> queryMyProducts(@RequestBody Page page) {
        PageResult<ProductListDTO> result = productAppService.queryMyProducts( page);
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
        productAppService.updateProduct(id, request);
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

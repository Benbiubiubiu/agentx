package org.xhy.interfaces.api.portal.billing;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.application.billing.service.BillingRecordAppService;
import org.xhy.application.billing.service.ProductAppService;
import org.xhy.application.billing.service.RuleAppService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.*;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.application.billing.service.UserBillingCountAppService;
import org.xhy.domain.billing.model.dto.ProductEntity;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;

/**
 * 计费模块管理
 */
@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingRecordAppService billingRecordAppService;
    private final ProductAppService productAppService;
    private final RuleAppService ruleAppService;
    private final UserBillingCountAppService userBillingCountAppService;

    public BillingController(
            BillingRecordAppService billingRecordAppService,
            ProductAppService productAppService,
            RuleAppService ruleAppService,
            UserBillingCountAppService userBillingCountAppService
    ) {
        this.billingRecordAppService = billingRecordAppService;
        this.productAppService = productAppService;
        this.ruleAppService = ruleAppService;
        this.userBillingCountAppService = userBillingCountAppService;
    }

    /**
     * 查询用户账单记录列表
     * @param page 查询请求
     * @return 账单记录列表
     */
    @GetMapping("/records")
    public Result<Page<BillingRecordDTO>> getRecords(@RequestBody Page<BillingUsageRecordEntity> page) {
        Page<BillingRecordDTO> result = billingRecordAppService.getRecords(page);
        return Result.success(result);
    }

    /**
     * 查询用户余额
     * @return 余额信息
     */
    @GetMapping("/balance")
    public Result<BalanceDTO> getBalance() {
        String userId = UserContext.getCurrentUserId();
        BalanceDTO balance = userBillingCountAppService.getBalance(userId);
        return Result.success(balance);
    }

    /**
     * 创建新产品
     * @param request 创建产品请求
     * @return 创建结果
     */
    @PostMapping("/products")
    public Result createProduct(@Validated @RequestBody CreateProductRequest request) {
        productAppService.createProduct(request);
        return Result.success();
    }

    /**
     * 查询产品列表
     * @param page 分页请求参数
     * @return 产品列表
     */
    @GetMapping("/products")
    public Result<Page<ProductListDTO>> getProducts(@RequestBody Page<ProductEntity> page) {
        Page<ProductListDTO> result = productAppService.getProducts(page);
        return Result.success(result);
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
     * 更新产品信息
     * @param id 产品ID
     * @param request 更新产品请求
     * @return 更新结果
     */
    @PutMapping("/products/{id}")
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
    @PutMapping("/rules/{id}")
    public Result updateRule(@PathVariable String id, @Validated @RequestBody CreateRuleRequest request) {
        ruleAppService.updateRule(id, request);
        return Result.success();
    }

    /**
     * 删除规则
     * @param id 规则ID
     * @param productId 产品ID
     * @return 删除结果
     */
    @DeleteMapping("/rules/{id}")
    public Result deleteRule(@PathVariable String id, @RequestParam String productId) {
        ruleAppService.deleteRule(id, productId);
        return Result.success();
    }

}

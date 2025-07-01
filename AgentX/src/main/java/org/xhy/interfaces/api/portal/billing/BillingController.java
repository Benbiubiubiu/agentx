package org.xhy.interfaces.api.portal.billing;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.billing.dto.BillingStatisticsDTO;
import org.xhy.application.billing.service.BillingRecordAppService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.application.billing.service.UserBillingCountAppService;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.interfaces.dto.billing.GetBillingStatisticsRequest;

import jakarta.validation.Valid;

/**
 * 计费模块管理
 */
@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingRecordAppService billingRecordAppService;
    private final UserBillingCountAppService userBillingCountAppService;

    public BillingController(
            BillingRecordAppService billingRecordAppService,
            UserBillingCountAppService userBillingCountAppService
    ) {
        this.billingRecordAppService = billingRecordAppService;
        this.userBillingCountAppService = userBillingCountAppService;
    }

    /**
     * 查询用户账单记录列表
     * @param page 查询请求
     * @return 账单记录列表
     */
    @PostMapping("/records")
    public Result<Page<BillingRecordDTO>> queryRecords(@RequestBody Page<BillingUsageRecordEntity> page) {
        String userId = UserContext.getCurrentUserId();
        Page<BillingRecordDTO> result = billingRecordAppService.queryRecords(page,userId);
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
     * 获取账单统计信息
     * @param request 统计请求
     * @return 账单统计信息
     */
    @PostMapping("/statistics")
    public Result<BillingStatisticsDTO> getBillingStatistics(@Valid @RequestBody GetBillingStatisticsRequest request) {
        String userId = UserContext.getCurrentUserId();
        BillingStatisticsDTO statistics = billingRecordAppService.getBillingStatistics(userId, request);
        return Result.success(statistics);
    }

}

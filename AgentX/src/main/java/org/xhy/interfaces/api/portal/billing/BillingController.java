package org.xhy.interfaces.api.portal.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.billing.BillingRecordApplicationService;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.service.UserBillingCountService;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.PageResult;
import org.xhy.interfaces.dto.billing.RecordListDTO;
import org.xhy.interfaces.dto.billing.RecordQueryRequest;
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

    @GetMapping("/query")
    public Result<PageResult<RecordListDTO>> queryRecords(RecordQueryRequest request) {
        request.setUserId(UserContext.getCurrentUserId());
        PageResult<RecordListDTO> result = billingRecordApplicationService.queryRecords(request);
        return Result.success(result);
    }

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
}

package org.xhy.interfaces.api.portal.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhy.application.billing.BillingRecordApplicationService;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.PageResult;
import org.xhy.interfaces.dto.billing.QueryRecordRequest;
import org.xhy.interfaces.dto.billing.RecordListDTO;
import org.xhy.interfaces.dto.billing.RecordQueryRequest;

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

    @GetMapping("/query")
    public Result<PageResult<RecordListDTO>> queryRecords(RecordQueryRequest request) {
        PageResult<RecordListDTO> result = billingRecordApplicationService.queryRecords(request);
        return Result.success(result);
    }
}

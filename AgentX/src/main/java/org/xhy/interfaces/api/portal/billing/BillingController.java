package org.xhy.interfaces.api.portal.billing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.billing.QueryRecordRequest;

/**
 * XHY
 * 2025/5/18 12:05
 * 用户账单管理
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@RestController
@RequestMapping("/billing")
public class BillingController {
    /**
     * 获取用户账单记录
     * @return
     */
    @GetMapping("/page")
    public Result<> getRecord(@RequestBody QueryRecordRequest request) {
        String userId = UserContext.getCurrentUserId();
        request.setUserId(userId);


        return null;
    }
}

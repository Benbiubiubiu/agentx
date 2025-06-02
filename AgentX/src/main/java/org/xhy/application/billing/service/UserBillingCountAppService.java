package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.xhy.application.billing.assembler.BalanceAssembler;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.service.UserBillingCountService;

/**
 * 用户账单计数应用服务
 * 用于适配领域层的用户账单计数服务
 */
@Service
public class UserBillingCountAppService {

    private final UserBillingCountService userBillingCountService;

    public UserBillingCountAppService(UserBillingCountService userBillingCountService) {
        this.userBillingCountService = userBillingCountService;
    }

    /**
     * 查询用户余额
     * @param userId 用户ID
     * @return 余额信息
     */
    public BalanceDTO queryBalance(String userId) {
        UserBillingCountEntity balance = userBillingCountService.queryBalance(userId);
        return BalanceAssembler.toDTO(balance);
    }
} 
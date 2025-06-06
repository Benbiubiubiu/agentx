package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.xhy.application.billing.assembler.BalanceAssembler;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.domain.billing.model.dto.UserBillingCountEntity;
import org.xhy.domain.billing.service.UserBillingCountDomainService;

/**
 * 用户账单计数应用服务
 * 用于适配领域层的用户账单计数服务
 */
@Service
public class UserBillingCountAppService {

    private final UserBillingCountDomainService userBillingCountDomainService;

    public UserBillingCountAppService(UserBillingCountDomainService userBillingCountService) {
        this.userBillingCountDomainService = userBillingCountService;
    }

    /**
     * 查询用户余额
     * @param userId 用户ID
     * @return 余额信息
     */
    public BalanceDTO getBalance(String userId) {
        UserBillingCountEntity balance = userBillingCountDomainService.getBalance(userId);
        return BalanceAssembler.toDTO(balance);
    }
} 
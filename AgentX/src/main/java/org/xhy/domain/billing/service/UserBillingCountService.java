package org.xhy.domain.billing.service;

import org.xhy.domain.billing.entity.UserBillingCountEntity;
import java.math.BigDecimal;

public interface UserBillingCountService {
    
    /**
     * 查询用户账户余额
     * @param userId 用户ID
     * @return 用户账单统计实体
     */
    UserBillingCountEntity queryBalance(String userId);

    /**
     * 增加账户余额
     * @param userId 用户ID
     * @param amount 增加金额
     * @return 更新后的用户账单统计实体
     */
    UserBillingCountEntity increaseBalance(String userId, BigDecimal amount);
} 
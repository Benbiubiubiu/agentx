package org.xhy.domain.billing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.repository.UserBillingCountRepository;
import org.xhy.domain.billing.service.UserBillingCountService;

import java.math.BigDecimal;

@Service
public class UserBillingCountServiceImpl implements UserBillingCountService {

    @Autowired
    private UserBillingCountRepository userBillingCountRepository;

    @Override
    public UserBillingCountEntity queryBalance(String userId) {
        return userBillingCountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserBillingCountEntity increaseBalance(String userId, BigDecimal amount) {
        // 参数校验
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("增加金额必须大于0");
        }

        // 更新余额
        int updated = userBillingCountRepository.increaseBalance(userId, amount);
        if (updated > 0) {
            // 更新成功，返回更新后的实体
            return userBillingCountRepository.findByUserId(userId);
        }
        return null;
    }
} 
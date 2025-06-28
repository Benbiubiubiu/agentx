package org.xhy.domain.billing.service;

import org.springframework.stereotype.Service;
import org.xhy.domain.billing.model.dto.UserBillingCountEntity;
import org.xhy.domain.billing.repository.UserBillingCountRepository;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.xhy.infrastructure.exception.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户计费统计领域服务
 */
@Service
public class UserBillingCountDomainService {
    
    private final UserBillingCountRepository billingCountRepository;

    public UserBillingCountDomainService(UserBillingCountRepository billingCountRepository) {
        this.billingCountRepository = billingCountRepository;
    }

    /**
     * 获取用户余额
     * @param userId 用户ID
     * @return 用户余额信息
     * @throws BusinessException 用户余额记录不存在时抛出异常
     */
    public UserBillingCountEntity getBalance(String userId) {
        LambdaQueryWrapper<UserBillingCountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBillingCountEntity::getUserId, userId);
        UserBillingCountEntity balance = billingCountRepository.selectOne(wrapper);
        if (balance == null) {
            throw new BusinessException("用户余额记录不存在: " + userId);
        }
        return balance;
    }

    /**
     * 增加账户余额
     * @param userId 用户ID
     * @param amount 增加金额
     * @return 更新后的用户账单统计实体
     */
    public UserBillingCountEntity increaseBalance(String userId, BigDecimal amount) {
        UserBillingCountEntity entity = getBalance(userId);
        if (entity == null) {
            entity = new UserBillingCountEntity();
            entity.setUserId(userId);
            entity.setBalance(amount);
            entity.setCumulativeRechargeAmount(amount);
            entity.setLastTransactionAt(LocalDateTime.now());
            billingCountRepository.insert(entity);
        } else {
            entity.setBalance(entity.getBalance().add(amount));
            entity.setCumulativeRechargeAmount(entity.getCumulativeRechargeAmount().add(amount));
            entity.setLastTransactionAt(LocalDateTime.now());
            billingCountRepository.updateById(entity);
        }
        return entity;
    }
} 
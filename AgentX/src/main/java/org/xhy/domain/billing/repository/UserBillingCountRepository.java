package org.xhy.domain.billing.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * XHY
 * 2025/5/17 22:03
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@Mapper
public interface UserBillingCountRepository extends MyBatisPlusExtRepository<UserBillingCountEntity> {
    
    /**
     * 根据用户ID查询账户余额
     * @param userId 用户ID
     * @return 用户账单统计实体
     */
    default UserBillingCountEntity findByUserId(String userId) {
        LambdaQueryWrapper<UserBillingCountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserBillingCountEntity::getUserId, userId);
        return selectOne(wrapper);
    }

    /**
     * 增加账户余额
     * @param userId 用户ID
     * @param amount 增加金额
     * @return 影响的行数
     */
    default int increaseBalance(String userId, BigDecimal amount) {
        LambdaUpdateWrapper<UserBillingCountEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserBillingCountEntity::getUserId, userId)
               .setSql("balance = balance + " + amount)
               .setSql("cumulative_recharge_amount = cumulative_recharge_amount + " + amount)
               .set(UserBillingCountEntity::getLastTransactionAt, LocalDateTime.now())
               .set(UserBillingCountEntity::getUpdatedAt, LocalDateTime.now());
        return update(null, wrapper);
    }
}

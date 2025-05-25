package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;
import java.math.BigDecimal;

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
    UserBillingCountEntity findByUserId(String userId);

    /**
     * 增加账户余额
     * @param userId 用户ID
     * @param amount 增加金额
     * @return 影响的行数
     */
    int increaseBalance(@Param("userId") String userId, @Param("amount") BigDecimal amount);
}

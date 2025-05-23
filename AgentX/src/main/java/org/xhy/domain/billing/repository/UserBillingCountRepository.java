package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

/**
 * XHY
 * 2025/5/17 22:03
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@Mapper
public interface UserBillingCountRepository extends MyBatisPlusExtRepository<UserBillingCountEntity> {
}

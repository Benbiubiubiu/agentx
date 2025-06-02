package org.xhy.domain.billing.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

@Mapper
public interface BillingRecordRepository extends MyBatisPlusExtRepository<BillingUsageRecordEntity> {

}
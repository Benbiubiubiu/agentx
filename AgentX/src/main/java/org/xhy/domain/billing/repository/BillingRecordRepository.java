package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;


@Mapper
public interface BillingRecordRepository extends MyBatisPlusExtRepository<BillingUsageRecordEntity> {

}
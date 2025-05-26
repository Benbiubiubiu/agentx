package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;
@Mapper
public interface ProductRepository extends MyBatisPlusExtRepository<ProductEntity> {
}

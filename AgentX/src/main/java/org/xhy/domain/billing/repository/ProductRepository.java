package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.model.dto.ProductEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

/**
 * 产品仓储接口
 */
@Mapper
public interface ProductRepository extends MyBatisPlusExtRepository<ProductEntity> {

}

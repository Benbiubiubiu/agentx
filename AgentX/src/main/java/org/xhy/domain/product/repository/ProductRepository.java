package org.xhy.domain.product.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

@Mapper
public interface ProductRepository extends MyBatisPlusExtRepository<ProductEntity> {

}

package org.xhy.domain.billing.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

import java.util.List;

/**
 * 产品仓储接口
 */
@Mapper
public interface ProductRepository extends MyBatisPlusExtRepository<ProductEntity> {

}

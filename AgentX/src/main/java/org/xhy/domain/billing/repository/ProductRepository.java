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
    
    /**
     * 查询产品总数
     * @return 产品总数
     */
    long count();

    /**
     * 查询用户产品总数
     * @param userId 用户ID
     * @return 产品总数
     */
    long countByUserId(String userId);

    /**
     * 分页查询产品列表
     * @param offset 偏移量
     * @param size 每页大小
     * @return 产品列表
     */
    List<ProductEntity> queryProducts(int offset, int size);

    /**
     * 分页查询用户产品列表
     * @param userId 用户ID
     * @param offset 偏移量
     * @param size 每页大小
     * @return 产品列表
     */
    List<ProductEntity> queryMyProducts(String userId, int offset, int size);
}

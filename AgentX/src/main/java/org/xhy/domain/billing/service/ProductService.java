package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.interfaces.dto.billing.CreateProductRequest;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.interfaces.dto.billing.PageResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 创建产品
     * @param productEntity
     */
    public void createProduct(ProductEntity productEntity) {
        productRepository.insert(productEntity);
    }

    /**
     * 删除产品
     * @param id
     */
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    /**
     * 获取所有产品列表
     * @return 产品列表
     */
    public List<ProductEntity> getAllProducts() {
        return productRepository.selectList(new QueryWrapper<ProductEntity>()
                .eq("is_enabled", true)
                .isNull("deleted_at"));
    }

    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品实体
     */
    public ProductEntity getProductByUserId(String id) {
        return productRepository.selectOne(new QueryWrapper<ProductEntity>()
                .eq("user_id", id)
                .isNull("deleted_at"));
    }

    /**
     * 更新产品
     * @param productEntity 产品实体
     */
    public void updateProduct(ProductEntity productEntity) {
        productRepository.updateById(productEntity);
    }

    /**
     * 查询产品列表
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public Page<ProductEntity> queryProducts(int page, int size) {
        // 创建分页对象
        Page<ProductEntity> pageParam = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>()
                .eq("is_enabled", true)
                .isNull("deleted_at");
        
        // 执行分页查询
        return productRepository.selectPage(pageParam, queryWrapper);
    }

    /**
     * 查询我的产品列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public Page<ProductEntity> queryMyProducts(String userId, int page, int size) {
        // 创建分页对象
        Page<ProductEntity> pageParam = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>()
                .eq("user_id", userId)
                .eq("is_enabled", true)
                .isNull("deleted_at");
        
        // 执行分页查询
        return productRepository.selectPage(pageParam, queryWrapper);
    }
}

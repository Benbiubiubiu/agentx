package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.model.dto.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;

import java.util.List;

@Service
public class ProductDomainService {
    
    private final ProductRepository productRepository;

    public ProductDomainService(ProductRepository productRepository) {
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
        return productRepository.selectList(new LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getEnabled, true)
                .isNull(ProductEntity::getDeletedAt));
    }

    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品实体
     */
    public ProductEntity getProductByUserId(String id) {
        return productRepository.selectOne(new LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getUserId, id)
                .isNull(ProductEntity::getDeletedAt));
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
     */
    public Page<ProductEntity> getProducts(Page<ProductEntity> page) {
        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getEnabled, true)
                .isNull(ProductEntity::getDeletedAt);
        return productRepository.selectPage(page, queryWrapper);
    }

}

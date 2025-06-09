package org.xhy.domain.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.product.repository.ProductRepository;

/**
 * 产品领域服务
 */
@Service
public class ProductDomainService {
    private final ProductRepository productRepository;

    public ProductDomainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 获取产品信息
     * @param id 产品ID
     * @return 产品信息
     */
    public ProductEntity getProduct(String id) {
        return productRepository.selectById(id);
    }

    /**
     * 创建产品
     * @param product
     */
    public void createProduct(ProductEntity product) {
        productRepository.insert(product);
    }

    /**
     * 删除产品
     * @param id
     */
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    /**
     * 更新产品规则ID
     * @param productId 产品ID
     * @param ruleId 规则ID
     */
    public void updateProductRuleId(String productId, String ruleId) {
        ProductEntity product = getProduct(productId);
        if (product == null) {
            throw new IllegalArgumentException("产品不存在");
        }
        product.setRuleId(ruleId);
        productRepository.updateById(product);
    }

    /**
     * 更新产品信息
     * @param product
     */
    public void updateProduct(ProductEntity product) {
        productRepository.updateById(product);
    }

    /**
     * 查询产品列表
     * @param page 分页参数
     * @return 产品列表
     */
    public Page<ProductEntity> queryProducts(Page<ProductEntity> page) {
        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getEnabled, true)
                .isNull(ProductEntity::getDeletedAt);

        return productRepository.selectPage(page,queryWrapper);
    }
} 
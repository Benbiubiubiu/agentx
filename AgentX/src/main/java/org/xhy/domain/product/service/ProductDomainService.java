package org.xhy.domain.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.product.repository.ProductRepository;
import org.xhy.infrastructure.exception.BusinessException;

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
     * @throws BusinessException 产品不存在时抛出异常
     */
    public ProductEntity getProduct(String id) {
        ProductEntity product = productRepository.selectById(id);
        if (product == null) {
            throw new BusinessException( "产品不存在: " + id);
        }
        return product;
    }

    /**
     * 创建产品
     * @param product
     */
    public void createProduct(ProductEntity product) {
        productRepository.checkInsert(product);
    }

    /**
     * 删除产品
     * @param id
     */
    public void deleteProduct(String id) {
        LambdaQueryWrapper<ProductEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductEntity::getId, id);
        productRepository.checkedDelete(wrapper);
    }

    /**
     * 更新产品规则ID
     * @param productId 产品ID
     * @param ruleId 规则ID
     */
    public void updateProductRuleId(String productId, String ruleId) {
        ProductEntity product = getProduct(productId);
        if (product == null) {
            throw new BusinessException("产品不存在:" + productId);
        }
        product.setRuleId(ruleId);
        productRepository.checkedUpdateById(product);
    }

    /**
     * 更新产品信息
     * @param product
     */
    public void updateProduct(ProductEntity product) {
        productRepository.checkedUpdateById(product);
    }

    /**w
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
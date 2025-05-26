package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.interfaces.dto.billing.CreateProductRequest;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * 创建产品
     * @param request 创建产品请求
     */
    public void createProduct(CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(request.getProductName());
        productEntity.setProductType(request.getProductType());
        productEntity.setDescription(request.getDescription());
        productEntity.setIsEnabled(true);
        productEntity.setUserId(request.getUserId());
        // ruleId可以为空，后续可以更新
        if (request.getRuleId() != null && !request.getRuleId().trim().isEmpty()) {
            productEntity.setRuleId(request.getRuleId());
        }
        
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
     * @param productEntity
     */
    public void updateProduct(ProductEntity productEntity) {
        productRepository.updateById(productEntity);
    }
}

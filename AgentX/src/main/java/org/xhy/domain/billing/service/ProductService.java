package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.interfaces.dto.billing.CreateProductRequest;
import org.xhy.interfaces.dto.billing.ProductListDTO;
import org.xhy.interfaces.dto.billing.PageResult;

import java.util.List;
import java.util.stream.Collectors;

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
     * @param id 产品ID
     * @param request 更新产品请求
     */
    public void updateProduct(String id, CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        productEntity.setProductName(request.getProductName());
        productEntity.setProductType(request.getProductType());
        productEntity.setDescription(request.getDescription());
        if (request.getRuleId() != null && !request.getRuleId().trim().isEmpty()) {
            productEntity.setRuleId(request.getRuleId());
        }
        productRepository.updateById(productEntity);
    }

    /**
     * 查询产品列表
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public PageResult<ProductListDTO> queryProducts(int page, int size) {
        // 创建分页对象
        Page<ProductEntity> pageParam = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>()
                .eq("is_enabled", true)
                .isNull("deleted_at");
        
        // 执行分页查询
        Page<ProductEntity> result = productRepository.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<ProductListDTO> productList = result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(productList, result.getTotal(), page, size);
    }

    /**
     * 查询我的产品列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public PageResult<ProductListDTO> queryMyProducts(String userId, int page, int size) {
        // 创建分页对象
        Page<ProductEntity> pageParam = new Page<>(page, size);
        
        // 创建查询条件
        QueryWrapper<ProductEntity> queryWrapper = new QueryWrapper<ProductEntity>()
                .eq("user_id", userId)
                .eq("is_enabled", true)
                .isNull("deleted_at");
        
        // 执行分页查询
        Page<ProductEntity> result = productRepository.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<ProductListDTO> productList = result.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(productList, result.getTotal(), page, size);
    }

    /**
     * 将实体转换为DTO
     */
    private ProductListDTO convertToDTO(ProductEntity entity) {
        ProductListDTO dto = new ProductListDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getProductName());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getProductType());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}

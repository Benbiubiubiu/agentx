package org.xhy.application.billing.assembler;

import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.interfaces.dto.billing.CreateProductRequest;

/**
 * 产品转换器
 * 负责DTO和实体之间的转换
 */
public class ProductAssembler {
    
    /**
     * 将创建产品请求转换为产品实体
     * @param request 创建产品请求
     * @return 产品实体
     */
    public static ProductEntity toEntity(CreateProductRequest request) {
        if (request == null) {
            return null;
        }
        
        ProductEntity product = new ProductEntity();
        product.setProductName(request.getProductName());
        product.setProductType(request.getProductType());
        product.setDescription(request.getDescription());
        product.setUserId(request.getUserId());
        product.setIsEnabled(true);
        if (request.getRuleId() != null && !request.getRuleId().trim().isEmpty()) {
            product.setRuleId(request.getRuleId());
        }
        return product;
    }

    /**
     * 将产品实体转换为产品列表DTO
     * @param entity 产品实体
     * @return 产品列表DTO
     */
    public static ProductListDTO toDTO(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
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

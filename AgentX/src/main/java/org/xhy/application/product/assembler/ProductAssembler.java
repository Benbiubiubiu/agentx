package org.xhy.application.product.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.interfaces.dto.product.CreateProductRequest;

/**
 * 产品对象转换器
 */
public class ProductAssembler {

    /**
     * 将创建产品请求转换为产品实体
     * @param request 创建产品请求
     * @return 产品实体
     */
    public static ProductEntity toEntity(CreateProductRequest request) {
        ProductEntity entity = new ProductEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setEnabled(true);
        return entity;
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
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
} 
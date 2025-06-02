package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.ProductAssembler;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.service.ProductService;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.interfaces.dto.Page;
import org.xhy.interfaces.dto.billing.CreateProductRequest;
import org.xhy.interfaces.dto.billing.PageResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * agentx-ben
 * 2025/6/1 17:31
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@Service
public class ProductAppService {
    private final ProductService productService;

    public ProductAppService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 创建产品
     * @param request 创建产品请求
     */
    public void createProduct(CreateProductRequest request) {
        ProductEntity productEntity = ProductAssembler.toEntity(request);
        productService.createProduct(productEntity);
    }

    /**
     * 查询产品列表
     */
    public PageResult<ProductListDTO> queryProducts(Page page) {
        List<ProductEntity> products = productService.queryProducts(page.getPage(), page.getPageSize());
        List<ProductListDTO> dtoList = products.stream()
                .map(ProductAssembler::toDTO)
                .collect(Collectors.toList());
        return new PageResult<>(dtoList, products.size(), page.getPage(), page.getPageSize());
    }

    /**
     * 查询我的产品列表
     */
    @Transactional(readOnly = true)
    public PageResult<ProductListDTO> queryMyProducts(Page page) {
        String userId = UserContext.getCurrentUserId();
        List<ProductEntity> products = productService.queryMyProducts(userId, page.getPage(), page.getPageSize());
        List<ProductListDTO> dtoList = products.stream()
                .map(ProductAssembler::toDTO)
                .collect(Collectors.toList());
        return new PageResult<>(dtoList, products.size(), page.getPage(), page.getPageSize());
    }

    /**
     * 更新产品
     * @param id 产品ID
     * @param request 更新产品请求
     */
    @Transactional
    public void updateProduct(String id, CreateProductRequest request) {
        ProductEntity entity = ProductAssembler.toEntity(request);
        entity.setId(id);
        productService.updateProduct(entity);
    }
}

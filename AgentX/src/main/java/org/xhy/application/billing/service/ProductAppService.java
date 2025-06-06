package org.xhy.application.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.ProductAssembler;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.domain.billing.model.dto.ProductEntity;
import org.xhy.domain.billing.service.ProductDomainService;
import org.xhy.interfaces.dto.billing.CreateProductRequest;

import java.util.stream.Collectors;

/**
 * agentx-ben
 * 2025/6/1 17:31
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@Service
public class ProductAppService {
    private final ProductDomainService productDomainService;

    public ProductAppService(ProductDomainService productService) {
        this.productDomainService = productService;
    }

    /**
     * 创建产品
     * @param request 创建产品请求
     */
    @Transactional
    public void createProduct(CreateProductRequest request) {
        ProductEntity productEntity = ProductAssembler.toEntity(request);
        productDomainService.createProduct(productEntity);
    }

    /**
     * 查询产品列表
     * @param page 分页参数
     * @return 产品列表
     */
    public Page<ProductListDTO> getProducts(Page<ProductEntity> page) {
        // 调用领域服务查询产品列表
        Page<ProductEntity> productPage = productDomainService.getProducts(page);
        
        // 转换为DTO
        Page<ProductListDTO> dtoPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        dtoPage.setRecords(productPage.getRecords().stream()
                .map(ProductAssembler::toDTO)
                .collect(Collectors.toList()));
        
        return dtoPage;
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
        productDomainService.updateProduct(entity);
    }
}

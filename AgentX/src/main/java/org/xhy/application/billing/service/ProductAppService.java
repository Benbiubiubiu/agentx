package org.xhy.application.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.xhy.application.billing.assembler.ProductAssembler;
import org.xhy.application.billing.dto.ProductListDTO;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.service.ProductService;
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
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public PageResult<ProductListDTO> queryProducts(int page, int size) {
        Page<ProductEntity> result = productService.queryProducts(page, size);
        return convertToPageResult(result, page, size);
    }

    /**
     * 查询我的产品列表
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页大小
     * @return 产品列表分页结果
     */
    public PageResult<ProductListDTO> queryMyProducts(String userId, int page, int size) {
        Page<ProductEntity> result = productService.queryMyProducts(userId, page, size);
        return convertToPageResult(result, page, size);
    }

    /**
     * 将MyBatis-Plus的Page转换为PageResult
     */
    private PageResult<ProductListDTO> convertToPageResult(Page<ProductEntity> page, int pageNum, int pageSize) {
        List<ProductListDTO> dtoList = page.getRecords().stream()
                .map(ProductAssembler::toDTO)
                .collect(Collectors.toList());
        return new PageResult<>(dtoList, page.getTotal(), pageNum, pageSize);
    }

    /**
     * 更新产品
     * @param id 产品ID
     * @param request 更新产品请求
     */
    public void updateProduct(String id, CreateProductRequest request) {
        ProductEntity entity = ProductAssembler.toEntity(request);
        entity.setId(id);
        productService.updateProduct(entity);
    }
}

package org.xhy.interfaces.api.portal.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.application.product.service.ProductAppService;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.product.CreateProductRequest;

/**
 * 产品模块管理
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductAppService productAppService;

    public ProductController(ProductAppService productAppService) {
        this.productAppService = productAppService;
    }

    /**
     * 创建新产品
     * @param request 创建产品请求
     * @return 创建结果
     */
    @PostMapping("/create")
    public Result createProduct(@Validated @RequestBody CreateProductRequest request) {
        productAppService.createProduct(request, UserContext.getCurrentUserId());
        return Result.success();
    }

    /**
     * 查询产品列表
     * @param page 分页请求参数
     * @return 产品列表
     */
    @PostMapping("/list")
    public Result<Page<ProductListDTO>> queryProducts(@RequestBody Page<ProductEntity> page) {
        Page<ProductListDTO> result = productAppService.queryProducts(page);
        return Result.success(result);
    }

    /**
     * 更新产品信息
     * @param id 产品ID
     * @param request 更新产品请求
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result updateProduct(@PathVariable String id, @Validated @RequestBody CreateProductRequest request) {
        productAppService.updateProduct(id, request);
        return Result.success();
    }

    /**
     * 删除产品
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteProduct(@PathVariable String id) {
        productAppService.deleteProduct(id);
        return Result.success();
    }
} 
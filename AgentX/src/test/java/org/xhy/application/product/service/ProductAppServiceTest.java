package org.xhy.application.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.product.service.ProductDomainService;
import org.xhy.interfaces.dto.product.CreateProductRequest;
import org.xhy.interfaces.dto.product.UpdateProductRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductAppServiceTest {

    @Mock
    private ProductDomainService productDomainService;

    @InjectMocks
    private ProductAppService productAppService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldCallDomainService() {
        // 准备测试数据
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("TEST");
        request.setDescription("测试产品描述");
        request.setRuleId(UUID.randomUUID().toString());

        // 执行测试
        productAppService.createProduct(request, UUID.randomUUID().toString());

        // 验证结果
        verify(productDomainService).createProduct(any(ProductEntity.class));
    }

    @Test
    void queryProducts_ShouldReturnPaginatedResults() {
        // 准备测试数据
        Page<ProductEntity> page = new Page<>(1, 10);
        List<ProductEntity> products = Arrays.asList(
            createTestProduct("产品1", "TEST", "描述1"),
            createTestProduct("产品2", "TEST", "描述2")
        );
        page.setRecords(products);
        page.setTotal(2);

        when(productDomainService.queryProducts(any())).thenReturn(page);

        // 执行测试
        Page<ProductListDTO> result = productAppService.queryProducts(page);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getRecords().size());
        assertEquals(2, result.getTotal());
        verify(productDomainService).queryProducts(any());
    }

    @Test
    void updateProduct_ShouldCallDomainService() {
        // 准备测试数据
        String productId = UUID.randomUUID().toString();
        UpdateProductRequest request = new UpdateProductRequest();
        request.setProductName("更新产品");
        request.setProductType("TEST");
        request.setDescription("更新产品描述");

        // 执行测试
        productAppService.updateProduct(productId, request);

        // 验证结果
        verify(productDomainService).updateProduct(any(ProductEntity.class));
    }

    private ProductEntity createTestProduct(String name, String type, String description) {
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID().toString());
        product.setProductName(name);
        product.setProductType(type);
        product.setDescription(description);
        product.setEnabled(true);
        return product;
    }
} 
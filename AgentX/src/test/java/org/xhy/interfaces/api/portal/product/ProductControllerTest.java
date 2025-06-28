package org.xhy.interfaces.api.portal.product;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.application.product.service.ProductAppService;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.interfaces.api.common.Result;
import org.xhy.interfaces.dto.product.CreateProductRequest;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductAppService productAppService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldReturnSuccess() {
        // 准备测试数据
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("TEST");
        request.setDescription("测试产品描述");
        request.setRuleId(UUID.randomUUID().toString());

        // 执行测试
        Result result = productController.createProduct(request);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(productAppService).createProduct(any(CreateProductRequest.class), any(String.class));
    }

    @Test
    void queryProducts_ShouldReturnSuccess() {
        // 准备测试数据
        Page<ProductEntity> page = new Page<>(1, 10);
        Page<ProductListDTO> dtoPage = new Page<>(1, 10);
        dtoPage.setRecords(Arrays.asList(
            createTestProductDTO("产品1", "TEST", "描述1"),
            createTestProductDTO("产品2", "TEST", "描述2")
        ));
        dtoPage.setTotal(2);

        when(productAppService.queryProducts(any())).thenReturn(dtoPage);

        // 执行测试
        Result<Page<ProductListDTO>> result = productController.queryProducts(page);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getRecords().size());
        verify(productAppService).queryProducts(any());
    }

    @Test
    void updateProduct_ShouldReturnSuccess() {
        // 准备测试数据
        String productId = UUID.randomUUID().toString();
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("更新产品");
        request.setProductType("TEST");
        request.setDescription("更新产品描述");
        request.setRuleId(UUID.randomUUID().toString());

        // 执行测试
        Result result = productController.updateProduct(productId, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(productAppService).updateProduct(eq(productId), any(CreateProductRequest.class));
    }

    private ProductListDTO createTestProductDTO(String name, String type, String description) {
        ProductListDTO dto = new ProductListDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setProductName(name);
        dto.setProductType(type);
        dto.setDescription(description);
        dto.setEnabled(true);
        return dto;
    }
}
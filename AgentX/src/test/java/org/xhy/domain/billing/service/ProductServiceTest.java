package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.domain.user.model.UserEntity;
import org.xhy.domain.user.service.UserDomainService;
import org.xhy.interfaces.dto.billing.CreateProductRequest;
import org.xhy.interfaces.dto.billing.PageResult;
import org.xhy.interfaces.dto.billing.ProductListDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserDomainService userDomainService;
    
    private String userId;
    private List<ProductEntity> testProducts;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前创建一个用户
        UserEntity user = userDomainService.register(UUID.randomUUID().toString(), null, "password123");
        userId = user.getId();
        
        // 准备测试数据
        testProducts = Arrays.asList(
            // 用户1的产品
            createProductEntity(UUID.randomUUID().toString(), "聊天助手", "CHAT", "智能聊天助手", userId),
            createProductEntity(UUID.randomUUID().toString(), "智能代理", "AGENT", "自动化代理服务", userId),
            createProductEntity(UUID.randomUUID().toString(), "数据分析", "ANALYSIS", "数据分析和可视化", userId),
            
            // 用户2的产品
            createProductEntity(UUID.randomUUID().toString(), "客服机器人", "CHAT", "24/7在线客服", "user2"),
            createProductEntity(UUID.randomUUID().toString(), "营销助手", "AGENT", "自动化营销工具", "user2"),
            
            // 已禁用的产品
            createProductEntity(UUID.randomUUID().toString(), "旧版助手", "CHAT", "已停用的旧版本", "user3", false),
            
            // 带规则的产品
            createProductEntity(UUID.randomUUID().toString(), "高级分析", "ANALYSIS", "高级数据分析", "user4", true, "rule1"),
            createProductEntity(UUID.randomUUID().toString(), "定制代理", "AGENT", "定制化代理服务", "user4", true, "rule2")
        );

        // 保存测试数据到数据库
        testProducts.forEach(productRepository::insert);
    }

    @Test
    void createProduct_ShouldSaveProductWithAllFields() {
        // 准备测试数据
        String ruleId = UUID.randomUUID().toString();
        
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("CHAT");
        request.setDescription("这是一个测试产品");
        request.setUserId(userId);
        request.setRuleId(ruleId);

        // 执行测试
        productService.createProduct(request);

    }

    @Test
    void createProduct_ShouldSaveProductWithoutRuleId() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("AGENT");
        request.setDescription("这是一个测试产品");
        request.setUserId(userId);

        // 执行测试
        productService.createProduct(request);

        // 验证repository调用
        verify(productRepository).insert(any(ProductEntity.class));
    }

    @Test
    void queryProducts_ShouldReturnPaginatedResults() {
        // 执行测试
        PageResult<ProductListDTO> result = productService.queryProducts(1, 3);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(3, result.getPageSize());
        assertEquals(3, result.getList().size()); // 第一页应该有3个产品

        // 验证DTO转换
        ProductListDTO dto1 = result.getList().get(0);
        assertEquals("1", dto1.getId());
        assertEquals("聊天助手", dto1.getName());
        assertEquals("CHAT", dto1.getType());
        assertEquals("智能聊天助手", dto1.getDescription());
    }

    @Test
    void queryMyProducts_ShouldReturnUserProducts() {
        // 执行测试
        PageResult<ProductListDTO> result = productService.queryMyProducts(userId, 1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.getTotal()); // 用户1有3个产品
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(3, result.getList().size());

        // 验证DTO转换
        ProductListDTO dto1 = result.getList().get(0);
        assertEquals("1", dto1.getId());
        assertEquals("聊天助手", dto1.getName());
        assertEquals("CHAT", dto1.getType());
        assertEquals("智能聊天助手", dto1.getDescription());
    }

    @Test
    void queryProducts_ShouldReturnEmptyList_WhenNoProducts() {
        // 清空数据库中的产品
        productRepository.delete(null);

        // 执行测试
        PageResult<ProductListDTO> result = productService.queryProducts(1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertTrue(result.getList().isEmpty());
    }

    private ProductEntity createProductEntity(String id, String name, String type, String description, String userId) {
        return createProductEntity(id, name, type, description, userId, true, null);
    }

    private ProductEntity createProductEntity(String id, String name, String type, String description, String userId, boolean isEnabled) {
        return createProductEntity(id, name, type, description, userId, isEnabled, null);
    }

    private ProductEntity createProductEntity(String id, String name, String type, String description, String userId, boolean isEnabled, String ruleId) {
        ProductEntity entity = new ProductEntity();
        entity.setId(id);
        entity.setProductName(name);
        entity.setProductType(type);
        entity.setDescription(description);
        entity.setUserId(userId);
        entity.setIsEnabled(isEnabled);
        entity.setRuleId(ruleId);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
} 
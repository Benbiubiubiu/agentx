package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhy.application.billing.assembler.ProductAssembler;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.repository.ProductRepository;
import org.xhy.domain.user.model.UserEntity;
import org.xhy.domain.user.service.UserDomainService;
import org.xhy.interfaces.dto.billing.CreateProductRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
            createProductEntity("聊天助手", "CHAT", "智能聊天助手", userId),
            createProductEntity("智能代理", "AGENT", "自动化代理服务", userId),
            createProductEntity("数据分析", "ANALYSIS", "数据分析和可视化", userId),
            
            // 用户2的产品
            createProductEntity("客服机器人", "CHAT", "24/7在线客服", "user2"),
            createProductEntity("营销助手", "AGENT", "自动化营销工具", "user2"),
            
            // 已禁用的产品
            createProductEntity("旧版助手", "CHAT", "已停用的旧版本", "user3", false),
            
            // 带规则的产品
            createProductEntity("高级分析", "ANALYSIS", "高级数据分析", "user4", true, "rule1"),
            createProductEntity("定制代理", "AGENT", "定制化代理服务", "user4", true, "rule2")
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
        ProductEntity productEntity = ProductAssembler.toEntity(request);
        productService.createProduct(productEntity);

        // 验证结果
        ProductEntity savedProduct = productRepository.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getProductName, "测试产品")
                .eq(ProductEntity::getUserId, userId)
        );
        
        assertNotNull(savedProduct);
        assertEquals("测试产品", savedProduct.getProductName());
        assertEquals("CHAT", savedProduct.getProductType());
        assertEquals("这是一个测试产品", savedProduct.getDescription());
        assertEquals(userId, savedProduct.getUserId());
        assertEquals(ruleId, savedProduct.getRuleId());
        assertTrue(savedProduct.getIsEnabled());
    }

    @Test
    void createProduct_ShouldSaveProductWithoutRuleId() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("AGENT");
        request.setDescription("这是一个测试产品");
        request.setUserId(userId);

        // 执行测试
        ProductEntity productEntity = ProductAssembler.toEntity(request);
        productService.createProduct(productEntity);

        // 验证结果
        ProductEntity savedProduct = productRepository.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductEntity>()
                .eq(ProductEntity::getProductName, "测试产品")
                .eq(ProductEntity::getUserId, userId)
        );
        
        assertNotNull(savedProduct);
        assertEquals("测试产品", savedProduct.getProductName());
        assertEquals("AGENT", savedProduct.getProductType());
        assertEquals("这是一个测试产品", savedProduct.getDescription());
        assertEquals(userId, savedProduct.getUserId());
        assertNull(savedProduct.getRuleId());
        assertTrue(savedProduct.getIsEnabled());
    }

    @Test
    void queryProducts_ShouldReturnPaginatedResults() {
        // 执行测试
        Page<ProductEntity> result = productService.queryProducts(1, 3);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getCurrent());
        assertEquals(3, result.getSize());
        assertEquals(3, result.getRecords().size()); // 第一页应该有3个产品

        // 验证实体
        ProductEntity product1 = result.getRecords().get(0);
        assertNotNull(product1.getId());
        assertEquals("聊天助手", product1.getProductName());
        assertEquals("CHAT", product1.getProductType());
        assertEquals("智能聊天助手", product1.getDescription());
    }

    @Test
    void queryMyProducts_ShouldReturnUserProducts() {
        // 执行测试
        Page<ProductEntity> result = productService.queryMyProducts(userId, 1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.getTotal()); // 用户1有3个产品
        assertEquals(1, result.getCurrent());
        assertEquals(10, result.getSize());
        assertEquals(3, result.getRecords().size());

        // 验证实体
        ProductEntity product1 = result.getRecords().get(0);
        assertNotNull(product1.getId());
        assertEquals("聊天助手", product1.getProductName());
        assertEquals("CHAT", product1.getProductType());
        assertEquals("智能聊天助手", product1.getDescription());
    }

    @Test
    void queryProducts_ShouldReturnEmptyList_WhenNoProducts() {
        // 清空数据库中的产品
        productRepository.delete(null);

        // 执行测试
        Page<ProductEntity> result = productService.queryProducts(1, 10);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(1, result.getCurrent());
        assertEquals(10, result.getSize());
        assertTrue(result.getRecords().isEmpty());
    }

    private ProductEntity createProductEntity(String name, String type, String description, String userId) {
        return createProductEntity(name, type, description, userId, true, null);
    }

    private ProductEntity createProductEntity(String name, String type, String description, String userId, boolean isEnabled) {
        return createProductEntity(name, type, description, userId, isEnabled, null);
    }

    private ProductEntity createProductEntity(String name, String type, String description, String userId, boolean isEnabled, String ruleId) {
        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.randomUUID().toString());
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
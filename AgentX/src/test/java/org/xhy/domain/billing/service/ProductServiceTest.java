//package org.xhy.domain.billing.service;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.xhy.application.product.assembler.ProductAssembler;
//import org.xhy.domain.product.model.dto.ProductEntity;
//import org.xhy.domain.product.repository.ProductRepository;
//import org.xhy.domain.product.service.ProductDomainService;
//import org.xhy.domain.user.model.UserEntity;
//import org.xhy.domain.user.service.UserDomainService;
//import org.xhy.interfaces.dto.billing.CreateProductRequest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//
//@SpringBootTest
//class ProductServiceTest {
//
//    @Autowired
//    private ProductDomainService productService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserDomainService userDomainService;
//
//    private String userId;
//    private List<ProductEntity> testProducts;
//
//    @BeforeEach
//    void setUp() {
//        // 在每个测试方法执行前创建一个用户
//        UserEntity user = userDomainService.register(UUID.randomUUID().toString(), null, "password123");
//        userId = user.getId();
//
//        // 准备测试数据
//        testProducts = Arrays.asList(
//            // 用户1的产品
//            createProductEntity("聊天助手", "CHAT", "智能聊天助手", userId),
//            createProductEntity("智能代理", "AGENT", "自动化代理服务", userId),
//            createProductEntity("数据分析", "ANALYSIS", "数据分析和可视化", userId),
//
//            // 用户2的产品
//            createProductEntity("客服机器人", "CHAT", "24/7在线客服", "user2"),
//            createProductEntity("营销助手", "AGENT", "自动化营销工具", "user2"),
//
//            // 已禁用的产品
//            createProductEntity("旧版助手", "CHAT", "已停用的旧版本", "user3", false),
//
//            // 带规则的产品
//            createProductEntity("高级分析", "ANALYSIS", "高级数据分析", "user4", true, "rule1"),
//            createProductEntity("定制代理", "AGENT", "定制化代理服务", "user4", true, "rule2")
//        );
//
//        // 保存测试数据到数据库
//        testProducts.forEach(productRepository::insert);
//    }
//
//    @Test
//    void createProduct_ShouldSaveProductWithAllFields() {
//        // 准备测试数据
//        String ruleId = UUID.randomUUID().toString();
//
//        CreateProductRequest request = new CreateProductRequest();
//        request.setProductName("测试产品");
//        request.setProductType("CHAT");
//        request.setDescription("这是一个测试产品");
//        request.setUserId(userId);
//        request.setRuleId(ruleId);
//
//        // 执行测试
//        ProductEntity productEntity = ProductAssembler.toEntity(request);
//        productService.createProduct(productEntity);
//
//        // 验证结果
//        ProductEntity savedProduct = productRepository.selectOne(
//            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductEntity>()
//                .eq(ProductEntity::getProductName, "测试产品")
//                .eq(ProductEntity::getUserId, userId)
//        );
//
//        assertNotNull(savedProduct);
//        assertEquals("测试产品", savedProduct.getProductName());
//        assertEquals("CHAT", savedProduct.getProductType());
//        assertEquals("这是一个测试产品", savedProduct.getDescription());
//        assertEquals(userId, savedProduct.getUserId());
//        assertEquals(ruleId, savedProduct.getRuleId());
//        assertTrue(savedProduct.getEnabled());
//    }
//
//    @Test
//    void createProduct_ShouldSaveProductWithoutRuleId() {
//        CreateProductRequest request = new CreateProductRequest();
//        request.setProductName("测试产品");
//        request.setProductType("AGENT");
//        request.setDescription("这是一个测试产品");
//        request.setUserId(userId);
//
//        // 执行测试
//        ProductEntity productEntity = ProductAssembler.toEntity(request);
//        productService.createProduct(productEntity);
//
//        // 验证结果
//        ProductEntity savedProduct = productRepository.selectOne(
//            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductEntity>()
//                .eq(ProductEntity::getProductName, "测试产品")
//                .eq(ProductEntity::getUserId, userId)
//        );
//
//        assertNotNull(savedProduct);
//        assertEquals("测试产品", savedProduct.getProductName());
//        assertEquals("AGENT", savedProduct.getProductType());
//        assertEquals("这是一个测试产品", savedProduct.getDescription());
//        assertEquals(userId, savedProduct.getUserId());
//        assertNull(savedProduct.getRuleId());
//        assertTrue(savedProduct.getEnabled());
//    }
//
//    @Test
//    void queryProducts_ShouldReturnPaginatedResults() {
//        // 准备测试数据
//        Page<ProductEntity> page = new Page<>(1, 10);
//        List<ProductEntity> products = createTestProducts(5);
//        when(productRepository.selectPage(any(), any())).thenReturn(page.setRecords(products));
//
//        // 执行测试
//        Page<ProductEntity> result = productService.queryProducts(page);
//
//        // 验证结果
//        assertNotNull(result);
//        assertEquals(5, result.getRecords().size());
//        verify(productRepository).selectPage(eq(page), any());
//    }
//
//    @Test
//    void queryProducts_ShouldReturnEmptyList_WhenNoProducts() {
//        // 准备测试数据
//        Page<ProductEntity> page = new Page<>(1, 10);
//        when(productRepository.selectPage(any(), any())).thenReturn(page.setRecords(Collections.emptyList()));
//
//        // 执行测试
//        Page<ProductEntity> result = productService.queryProducts(page);
//
//        // 验证结果
//        assertNotNull(result);
//        assertTrue(result.getRecords().isEmpty());
//        verify(productRepository).selectPage(eq(page), any());
//    }
//
//    private ProductEntity createProductEntity(String name, String type, String description, String userId) {
//        return createProductEntity(name, type, description, userId, true, null);
//    }
//
//    private ProductEntity createProductEntity(String name, String type, String description, String userId, boolean enabled) {
//        return createProductEntity(name, type, description, userId, enabled, null);
//    }
//
//    private ProductEntity createProductEntity(String name, String type, String description, String userId, boolean enabled, String ruleId) {
//        ProductEntity entity = new ProductEntity();
//        entity.setId(UUID.randomUUID().toString());
//        entity.setProductName(name);
//        entity.setProductType(type);
//        entity.setDescription(description);
//        entity.setUserId(userId);
//        entity.setEnabled(enabled);
//        entity.setRuleId(ruleId);
//        entity.setCreatedAt(LocalDateTime.now());
//        entity.setUpdatedAt(LocalDateTime.now());
//        return entity;
//    }
//
//    /**
//     * 创建测试产品数据
//     * @param count 产品数量
//     * @return 产品列表
//     */
//    private List<ProductEntity> createTestProducts(int count) {
//        List<ProductEntity> products = new ArrayList<>();
//        for (int i = 0; i < count; i++) {
//            ProductEntity product = new ProductEntity();
//            product.setId(UUID.randomUUID().toString());
//            product.setProductName("测试产品" + (i + 1));
//            product.setProductType("TEST");
//            product.setDescription("测试产品描述" + (i + 1));
//            product.setEnabled(true);
//            product.setCreatedAt(LocalDateTime.now());
//            product.setUpdatedAt(LocalDateTime.now());
//            products.add(product);
//        }
//        return products;
//    }
//}
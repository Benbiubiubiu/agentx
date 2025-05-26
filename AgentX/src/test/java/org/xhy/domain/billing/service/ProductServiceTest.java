package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前创建一个用户
        UserEntity user = userDomainService.register(UUID.randomUUID().toString(), null, "password123");
        userId = user.getId();
    }

    @Test
    void createProduct_ShouldSaveProductWithAllFields() {
        // 准备测试数据
        String ruleId = UUID.randomUUID().toString();
        
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("CHAT");
        request.setDescription("这是一个测试产品");
        request.setUserId(userId);  // 使用在setUp中创建的用户ID
        request.setRuleId(ruleId);

        // 执行测试
        productService.createProduct(request);

        // 查询并验证结果
        ProductEntity product = productRepository.selectOne(new LambdaQueryWrapper<ProductEntity>().eq(ProductEntity::getUserId, userId));
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("测试产品", product.getProductName());
        assertEquals("CHAT", product.getProductType());
        assertEquals("这是一个测试产品", product.getDescription());
        assertEquals(userId, product.getUserId());
        assertEquals(ruleId, product.getRuleId());
    }

    @Test
    void createProduct_ShouldSaveProductWithoutRuleId() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品");
        request.setProductType("AGENT");
        request.setDescription("这是一个测试产品");
        request.setUserId(userId);  // 使用在setUp中创建的用户ID
        // 不设置ruleId

        // 执行测试
        productService.createProduct(request);

        // 查询并验证结果
        ProductEntity product = productRepository.selectOne(new LambdaQueryWrapper<ProductEntity>().eq(ProductEntity::getUserId, userId));
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("测试产品", product.getProductName());
        assertEquals("AGENT", product.getProductType());
        assertEquals("这是一个测试产品", product.getDescription());
        assertEquals(userId, product.getUserId());
        assertNull(product.getRuleId());
    }
} 
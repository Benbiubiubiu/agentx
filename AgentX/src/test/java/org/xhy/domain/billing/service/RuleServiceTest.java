//package org.xhy.domain.billing.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.xhy.domain.rule.model.dto.RuleEntity;
//import org.xhy.domain.rule.model.dto.RuleVersionEntity;
//import org.xhy.domain.product.model.dto.ProductEntity;
//import org.xhy.domain.rule.model.config.BillingRule;
//import org.xhy.domain.product.repository.ProductRepository;
//import org.xhy.domain.rule.service.RuleDomainService;
//import org.xhy.domain.user.model.UserEntity;
//import org.xhy.domain.user.service.UserDomainService;
//import org.xhy.interfaces.dto.billing.CreateRuleRequest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class RuleServiceTest {
//
//    @Autowired
//    private RuleDomainService ruleService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private UserDomainService userDomainService;
//
//    private String userId;
//    private String productId;
//
//    @BeforeEach
//    void setUp() {
//        // 创建测试用户
//        UserEntity user = userDomainService.register("ben1@example.com", null, "password123");
//        userId = user.getId();
//
//        // 创建测试产品
//        ProductEntity product = new ProductEntity();
//        product.setUserId(userId);
//        product.setProductName("测试产品");
//        product.setProductType("CHAT");
//        product.setDescription("这是一个测试产品");
//        product.setEnabled(true);
//        productRepository.insert(product);
//        productId = product.getId();
//    }
//
//    @Test
//    void createRule_ShouldSaveRuleAndUpdateProduct() {
//        // 准备测试数据
//        CreateRuleRequest request = new CreateRuleRequest();
//        request.setVersion("1.0.0");
//        request.setDescription("测试规则");
//        request.setProductId(productId);
//
//        BillingRule billingRule = new BillingRule();
//        billingRule.setInputToken(0.004);
//        billingRule.setOutputToken(0.007);
//        request.setRule(billingRule);
//
//        // 执行测试
//        RuleEntity rule = ruleService.createRule(request);
//
//        // 验证规则创建结果
//        assertNotNull(rule);
//        assertNotNull(rule.getId());
//        assertEquals("1.0.0", rule.getVersion());
//        assertEquals("测试规则", rule.getDescription());
//        assertNotNull(rule.getRule());
//        assertTrue(rule.getRule() instanceof BillingRule);
//        BillingRule savedRule = (BillingRule) rule.getRule();
//        assertEquals(0.004, savedRule.getInputToken());
//        assertEquals(0.007, savedRule.getOutputToken());
//
//
//        // 验证产品是否更新了规则ID
//        ProductEntity product = productRepository.selectById(productId);
//        assertNotNull(product);
//        assertEquals(rule.getId(), product.getRuleId());
//    }
//
//    @Test
//    void createRule_ShouldThrowExceptionForUnsupportedProductType() {
//        // 准备测试数据
//        ProductEntity product = new ProductEntity();
//        product.setUserId(userId);
//        product.setProductName("不支持的产品");
//        product.setProductType("OTHER");  // 不支持的产品类型
//        product.setDescription("这是一个不支持的产品");
//        product.setEnabled(true);
//        productRepository.insert(product);
//
//        CreateRuleRequest request = new CreateRuleRequest();
//        request.setVersion("1.0.0");
//        request.setDescription("测试规则");
//        request.setProductId(product.getId());
//
//        BillingRule billingRule = new BillingRule();
//        billingRule.setInputToken(0.004);
//        billingRule.setOutputToken(0.007);
//        request.setRule(billingRule);
//
//        // 执行测试并验证异常
//        assertThrows(IllegalArgumentException.class, () -> {
//            ruleService.createRule(request);
//        });
//    }
//
//    @Test
//    void createRuleVersion_ShouldSaveVersionWithEffectiveDates() {
//        // 先创建一个规则
//        CreateRuleRequest request = new CreateRuleRequest();
//        request.setVersion("1.0.0");
//        request.setDescription("测试规则");
//        request.setProductId(productId);
//
//        BillingRule billingRule = new BillingRule();
//        billingRule.setInputToken(0.004);
//        billingRule.setOutputToken(0.007);
//        request.setRule(billingRule);
//
//        RuleEntity rule = ruleService.createRule(request);
//
//        // 创建新版本
//        CreateRuleRequest versionRequest = new CreateRuleRequest();
//        versionRequest.setVersion("1.0.1");
//        versionRequest.setDescription("测试规则版本");
//        versionRequest.setRule(billingRule);
//
//        LocalDateTime effectiveAt = LocalDateTime.now();
//        LocalDateTime expiredAt = effectiveAt.plusMonths(1);
//
//        // 创建规则版本实体
//        RuleVersionEntity versionEntity = new RuleVersionEntity();
//        versionEntity.setRuleId(rule.getId());
//        versionEntity.setVersion(versionRequest.getVersion());
//        versionEntity.setDescription(versionRequest.getDescription());
//        versionEntity.setRule(versionRequest.getRule());
//        versionEntity.setEffectiveAt(effectiveAt);
//        versionEntity.setExpiredAt(expiredAt);
//
//        // 执行测试
//        RuleVersionEntity version = ruleService.createRuleVersion(versionEntity);
//
//        // 验证结果
//        assertNotNull(version);
//        assertNotNull(version.getId());
//        assertEquals(rule.getId(), version.getRuleId());
//        assertEquals("1.0.1", version.getVersion());
//        assertEquals("测试规则版本", version.getDescription());
//        assertEquals(effectiveAt, version.getEffectiveAt());
//        assertEquals(expiredAt, version.getExpiredAt());
//        assertNotNull(version.getRule());
//    }
//
//    @Test
//    void deleteRule_ShouldDeleteRuleAndVersions() {
//        // 先创建一个规则
//        CreateRuleRequest request = new CreateRuleRequest();
//        request.setVersion("1.0.0");
//        request.setDescription("测试规则");
//        request.setProductId(productId);
//
//        BillingRule billingRule = new BillingRule();
//        billingRule.setInputToken(0.004);
//        billingRule.setOutputToken(0.007);
//        request.setRule(billingRule);
//
//        RuleEntity rule = ruleService.createRule(request);
//        String ruleId = rule.getId();
//
//        // 创建规则版本
//        CreateRuleRequest versionRequest = new CreateRuleRequest();
//        versionRequest.setVersion("1.0.1");
//        versionRequest.setDescription("测试规则版本");
//        versionRequest.setRule(billingRule);
//
//        LocalDateTime effectiveAt = LocalDateTime.now();
//        LocalDateTime expiredAt = effectiveAt.plusMonths(1);
//
//        RuleVersionEntity versionEntity = new RuleVersionEntity();
//        versionEntity.setRuleId(ruleId);
//        versionEntity.setVersion(versionRequest.getVersion());
//        versionEntity.setDescription(versionRequest.getDescription());
//        versionEntity.setRule(versionRequest.getRule());
//        versionEntity.setEffectiveAt(effectiveAt);
//        versionEntity.setExpiredAt(expiredAt);
//
//        ruleService.createRuleVersion(versionEntity);
//
//        // 执行删除操作
//        ruleService.deleteRule(ruleId, productId);
//
//        // 验证规则已被删除
//        RuleEntity deletedRule = ruleService.getRule(ruleId);
//        assertNull(deletedRule);
//
//        // 验证规则版本已被删除
//        List<RuleVersionEntity> versions = ruleService.getRuleVersions(ruleId);
//        assertTrue(versions.isEmpty());
//
//        // 验证产品表中的规则ID已被清除
//        ProductEntity product = productRepository.selectById(productId);
//        assertNotNull(product);
//        assertNull(product.getRuleId());
//    }
//}
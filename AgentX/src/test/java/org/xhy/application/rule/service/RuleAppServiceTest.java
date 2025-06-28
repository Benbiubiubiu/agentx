package org.xhy.application.rule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.rule.dto.RuleDTO;
import org.xhy.domain.rule.constant.RuleType;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.product.service.ProductDomainService;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.model.dto.RuleEntity;
import java.util.List;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RuleAppServiceTest {

    @Autowired
    private RuleAppService ruleAppService;

    @Autowired
    private ProductDomainService productDomainService;

    private static final String TEST_PRODUCT_ID = UUID.randomUUID().toString();
    private static final String USER_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // 创建测试用的产品
        ProductEntity product = new ProductEntity();
        product.setId(TEST_PRODUCT_ID);
        product.setProductName("测试产品");
        product.setProductType("CHAT");  // 设置产品类型
        product.setEnabled(true);  // 设置启用状态
        product.setUserId(USER_ID);

        productDomainService.createProduct(product);
    }

    @Test
    void testCreateRule() {
        // 准备测试数据
        CreateRuleRequest request = new CreateRuleRequest();
        request.setVersion("1.0");
        request.setDescription("测试规则");
        request.setRelatedType(RuleType.PRODUCT);
        request.setRelatedId(TEST_PRODUCT_ID);
        request.setEffectiveAt(LocalDateTime.now());
        request.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        request.setRule(billingRule);

        // 执行测试
        RuleEntity ruleEntity = ruleAppService.createRule(request);
        assertNotNull(ruleEntity);
        assertNotNull(ruleEntity.getId());

        // 获取产品信息以获取规则ID
        ProductEntity product = productDomainService.getProduct(TEST_PRODUCT_ID);
        assertNotNull(product);
        assertNotNull(product.getRuleId());
        assertEquals(ruleEntity.getId(), product.getRuleId());

        // 验证结果
        RuleDTO result = ruleAppService.getRule(product.getRuleId());
        assertNotNull(result);
        assertEquals("1.0", result.getVersion());
        assertEquals("测试规则", result.getDescription());
        assertNotNull(result.getRule());
        assertTrue(result.getRule() instanceof BillingRule);
        BillingRule savedRule = (BillingRule) result.getRule();
        assertEquals(0.004, savedRule.getInputToken());
        assertEquals(0.007, savedRule.getOutputToken());


    }

    @Test
    void testCreateRuleWithoutProduct() {
        // 准备测试数据
        CreateRuleRequest request = new CreateRuleRequest();
        request.setVersion("1.0");
        request.setDescription("测试规则");
        request.setEffectiveAt(LocalDateTime.now());
        request.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        request.setRule(billingRule);

        // 执行测试
        RuleEntity ruleEntity = ruleAppService.createRule(request);
        assertNotNull(ruleEntity);
        assertNotNull(ruleEntity.getId());

        // 验证结果
        RuleDTO result = ruleAppService.getRule(ruleEntity.getId());
        assertNotNull(result);
        assertEquals("1.0", result.getVersion());
        assertEquals("测试规则", result.getDescription());
    }

    @Test
    void testGetRule() {
        // 准备测试数据
        CreateRuleRequest request = new CreateRuleRequest();
        request.setVersion("1.0");
        request.setDescription("测试规则");
        request.setRelatedType(RuleType.PRODUCT);
        request.setRelatedId(TEST_PRODUCT_ID);
        request.setEffectiveAt(LocalDateTime.now());
        request.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        request.setRule(billingRule);

        // 先创建一条规则
        RuleEntity ruleEntity = ruleAppService.createRule(request);
        assertNotNull(ruleEntity);

        // 执行测试
        RuleDTO result = ruleAppService.getRule(ruleEntity.getId());

        // 验证结果
        assertNotNull(result);
        assertEquals("1.0", result.getVersion());
        assertEquals("测试规则", result.getDescription());
        assertNotNull(result.getRule());
        assertTrue(result.getRule() instanceof BillingRule);
    }

    @Test
    void testUpdateRule() {
        // 准备测试数据
        CreateRuleRequest createRequest = new CreateRuleRequest();
        createRequest.setVersion("1.0");
        createRequest.setDescription("测试规则");
        createRequest.setRelatedType(RuleType.PRODUCT);
        createRequest.setRelatedId(TEST_PRODUCT_ID);
        createRequest.setEffectiveAt(LocalDateTime.now());
        createRequest.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        createRequest.setRule(billingRule);

        // 先创建一条规则
        RuleEntity ruleEntity = ruleAppService.createRule(createRequest);
        assertNotNull(ruleEntity);

        // 准备更新数据
        CreateRuleRequest updateRequest = new CreateRuleRequest();
        updateRequest.setVersion("2.0");
        updateRequest.setDescription("更新后的规则");
        updateRequest.setRelatedType(RuleType.PRODUCT);
        updateRequest.setRelatedId(TEST_PRODUCT_ID);
        updateRequest.setEffectiveAt(LocalDateTime.now());
        updateRequest.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置更新后的计费规则
        BillingRule updateBillingRule = new BillingRule();
        updateBillingRule.setInputToken(0.005);
        updateBillingRule.setOutputToken(0.008);
        updateRequest.setRule(updateBillingRule);

        // 执行测试
        ruleAppService.updateRule(ruleEntity.getId(), updateRequest);

        // 验证结果
        RuleDTO updatedRule = ruleAppService.getRule(ruleEntity.getId());
        assertNotNull(updatedRule);
        assertEquals("2.0", updatedRule.getVersion());
        assertEquals("更新后的规则", updatedRule.getDescription());
        assertNotNull(updatedRule.getRule());
        assertTrue(updatedRule.getRule() instanceof BillingRule);
        BillingRule savedRule = (BillingRule) updatedRule.getRule();
        assertEquals(0.005, savedRule.getInputToken());
        assertEquals(0.008, savedRule.getOutputToken());

    }

    @Test
    void testDeleteRule() {
        // 准备测试数据
        CreateRuleRequest request = new CreateRuleRequest();
        request.setVersion("1.0");
        request.setDescription("测试规则");
        request.setRelatedType(RuleType.PRODUCT);
        request.setRelatedId(TEST_PRODUCT_ID);
        request.setEffectiveAt(LocalDateTime.now());
        request.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        request.setRule(billingRule);

        // 先创建一条规则
        RuleEntity ruleEntity = ruleAppService.createRule(request);
        assertNotNull(ruleEntity);

        // 执行测试
        ruleAppService.deleteRule(ruleEntity.getId(), TEST_PRODUCT_ID);

        // 验证结果
        RuleDTO deletedRule = ruleAppService.getRule(ruleEntity.getId());
        assertNull(deletedRule);

        // 验证产品关联已清除
        ProductEntity product = productDomainService.getProduct(TEST_PRODUCT_ID);
        assertNotNull(product);
        assertNull(product.getRuleId());

//        // 验证规则版本已删除
//        assertThrows(Exception.class, () -> {
//            ruleAppService.getRuleVersions(ruleEntity.getId());
//        });
    }

    @Test
    void testCreateRuleVersion() {
        // 准备测试数据
        CreateRuleRequest request = new CreateRuleRequest();
        request.setVersion("1.0");
        request.setDescription("测试规则");
        request.setRelatedType(RuleType.PRODUCT);
        request.setRelatedId(TEST_PRODUCT_ID);
        request.setEffectiveAt(LocalDateTime.now());
        request.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        request.setRule(billingRule);

        // 先创建一条规则
        RuleEntity ruleEntity = ruleAppService.createRule(request);
        assertNotNull(ruleEntity);

        // 创建新版本
        CreateRuleRequest versionRequest = new CreateRuleRequest();
        versionRequest.setVersion("1.1");
        versionRequest.setDescription("测试规则版本");
        versionRequest.setRule(billingRule);
        versionRequest.setEffectiveAt(LocalDateTime.now());
        versionRequest.setExpiredAt(LocalDateTime.now().plusMonths(1));


    }
} 
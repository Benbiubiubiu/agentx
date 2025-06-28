package org.xhy.application.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import org.xhy.domain.rule.constant.RuleType;
import org.xhy.application.rule.service.RuleAppService;
import org.xhy.application.product.service.ProductAppService;
import org.xhy.interfaces.dto.product.CreateProductRequest;
import org.xhy.domain.rule.model.dto.RuleEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BillingRecordAppServiceTest {

    @Autowired
    private BillingRecordAppService billingRecordAppService;

    @Autowired
    private ProductAppService productAppService;

    @Autowired
    private RuleAppService ruleAppService;

    private static final String USER_ID = UUID.randomUUID().toString();
    private String productId;
    private String ruleId;

    @BeforeEach
    void setUp() {
        // 创建测试用的产品
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("测试产品222");
        request.setProductType("CHAT");
        productAppService.createProduct(request, USER_ID);

        // 通过产品名称查询获取实际创建的产品ID
        Page<ProductEntity> page = new Page<>(1, 10);
        Page<ProductListDTO> products = productAppService.queryProducts(page);
        ProductListDTO product = products.getRecords().stream()
            .filter(p -> "测试产品222".equals(p.getProductName()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("未找到创建的产品"));
        productId = product.getId();

        // 创建测试用的规则
        CreateRuleRequest ruleRequest = new CreateRuleRequest();
        ruleRequest.setVersion("1.0");
        ruleRequest.setDescription("测试规则");
        ruleRequest.setRelatedType(RuleType.PRODUCT);
        ruleRequest.setRelatedId(productId);
        ruleRequest.setEffectiveAt(LocalDateTime.now());
        ruleRequest.setExpiredAt(LocalDateTime.now().plusMonths(1));

        // 设置计费规则
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.004);
        billingRule.setOutputToken(0.007);
        ruleRequest.setRule(billingRule);

        // 创建规则并获取返回的规则实体
        RuleEntity ruleEntity = ruleAppService.createRule(ruleRequest);
        assertNotNull(ruleEntity);
        ruleId = ruleEntity.getId();
        
        // 获取规则版本ID
        String ruleVersionId = ruleAppService.getCurrentRuleVersion(ruleId).getId();

        // 创建测试用的计费记录
        billingRecordAppService.createRecord(
            USER_ID,
            productId,
            ruleVersionId,  // 使用规则版本ID
            new BigDecimal("100.0000"),
            new BigDecimal("80.0000")
        );
    }

    @Test
    void testQueryRecords() {
        // 准备测试数据
        Page<BillingUsageRecordEntity> page = new Page<>(1, 10);

        // 执行测试
        Page<BillingRecordDTO> result = billingRecordAppService.queryRecords(page, USER_ID);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.getRecords().isEmpty());

        BillingRecordDTO record = result.getRecords().get(0);
        assertEquals(USER_ID, record.getUserId());
        assertEquals(productId, record.getProductId());
        String expectedRuleVersionId = ruleAppService.getCurrentRuleVersion(ruleId).getId();
        assertEquals(expectedRuleVersionId, record.getRuleVersionId());
        assertEquals(new BigDecimal("100.0000"), record.getTotalAmount());
        assertEquals(new BigDecimal("80.0000"), record.getAmountLeft());
    }

    @Test
    void testQueryRecordsWithNoData() {
        // 准备测试数据
        Page<BillingUsageRecordEntity> page = new Page<>(1, 10);
        String nonExistentUserId = UUID.randomUUID().toString();

        // 执行测试
        Page<BillingRecordDTO> result = billingRecordAppService.queryRecords(page, nonExistentUserId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0, result.getTotal());
    }
}
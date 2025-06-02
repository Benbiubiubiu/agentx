package org.xhy.domain.billing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.entity.ProductEntity;
import org.xhy.domain.billing.entity.RuleEntity;
import org.xhy.domain.billing.entity.RuleVersionEntity;
import org.xhy.domain.billing.model.BillingRule;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.billing.repository.RuleRepository;
import org.xhy.domain.billing.repository.RuleVersionRepository;
import org.xhy.domain.billing.service.BillingRecordDomainService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 账单记录服务测试类
 */
@SpringBootTest

public class BillingRecordTest {

    @Autowired
    private BillingRecordDomainService billingRecordDomainService;

    @Autowired
    private BillingRecordRepository billingRecordRepository;

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private RuleVersionRepository ruleVersionRepository;

    @Test
    public void testCreateAndQueryRecord() {
        // 准备测试数据
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String ruleId = UUID.randomUUID().toString();
        String ruleVersionId = UUID.randomUUID().toString();
        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal amountLeft = new BigDecimal("100.00");

        // 1. 创建规则
        RuleEntity rule = new RuleEntity();
        rule.setId(ruleId);
        rule.setVersion("1.0");
        rule.setDescription("测试规则描述");
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.04);
        billingRule.setOutputToken(0.07);
        rule.setRule(billingRule);
        ruleRepository.insert(rule);

        // 2. 创建规则版本
        RuleVersionEntity ruleVersion = new RuleVersionEntity();
        ruleVersion.setId(ruleVersionId);
        ruleVersion.setRuleId(ruleId);  // 关联到规则表
        ruleVersion.setRule(billingRule);
        ruleVersion.setVersion("1.0");
        ruleVersion.setDescription("测试计费规则");
        ruleVersionRepository.insert(ruleVersion);

        // 3. 创建产品并关联规则
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setProductName("测试产品");
        product.setProductType("CHAT");
        product.setDescription("测试产品描述");
        product.setRuleId(ruleId);  // 关联到规则表
        product.setUserId(userId);
        product.setIsEnabled(true);

        // 4. 创建账单记录
        BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
            userId,
            productId,
            ruleVersionId,
            totalAmount,
            amountLeft
        );

        // 验证创建结果
        assertNotNull(record, "创建记录不应该返回null");
        assertNotNull(record.getId(), "记录ID不应该为null");
        assertEquals(userId, record.getUserId());
        assertEquals(productId, record.getProductId());
        assertEquals(ruleVersionId, record.getRuleVersionId());
        assertEquals("输入token计费：0.04/1k，输出token计费：0.07/1k", record.getPriceRule());
        assertEquals(totalAmount, record.getTotalAmount());
        assertEquals(amountLeft, record.getAmountLeft());

        // 5. 查询账单记录
        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(userId, 1, 10);

        // 验证查询结果
        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertEquals(1, records.size());

        // 验证总记录数
        long count = billingRecordDomainService.countRecords(userId);
        assertEquals(1, count);
    }

    @Test
    public void testCreateMultipleRecordsAndPagination() {
        // 准备测试数据
        String userId = "4a7dbafc4940428e06b1fec33e2bdb4a";
        String productId = UUID.randomUUID().toString();
        String ruleId = UUID.randomUUID().toString();
        String ruleVersionId = UUID.randomUUID().toString();
        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal amountLeft = new BigDecimal("100.00");

        // 1. 创建规则
        RuleEntity rule = new RuleEntity();
        rule.setId(ruleId);
        rule.setVersion("1.0");
        rule.setDescription("测试规则描述");
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.04);
        billingRule.setOutputToken(0.07);
        rule.setRule(billingRule);
        ruleRepository.insert(rule);

        // 2. 创建规则版本
        RuleVersionEntity ruleVersion = new RuleVersionEntity();
        ruleVersion.setId(ruleVersionId);
        ruleVersion.setRuleId(ruleId);
        ruleVersion.setRule(billingRule);
        ruleVersion.setVersion("1.0");
        ruleVersion.setDescription("测试计费规则");
        ruleVersionRepository.insert(ruleVersion);

        // 3. 创建产品并关联规则
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setProductName("测试产品");
        product.setProductType("CHAT");
        product.setDescription("测试产品描述");
        product.setRuleId(ruleId);
        product.setUserId(userId);
        product.setIsEnabled(true);

        // 创建10条记录
        for (int i = 0; i < 10; i++) {
            BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
                userId,
                productId,
                ruleVersionId,
                totalAmount,
                amountLeft
            );
            assertNotNull(record);
            assertNotNull(record.getId());
            assertEquals("输入token计费：0.04/1k，输出token计费：0.07/1k", record.getPriceRule());
        }

        // 测试分页查询
        List<BillingUsageRecordEntity> page1 = billingRecordDomainService.queryRecords(userId, 1, 3);
        List<BillingUsageRecordEntity> page2 = billingRecordDomainService.queryRecords(userId, 2, 3);
        List<BillingUsageRecordEntity> page3 = billingRecordDomainService.queryRecords(userId, 3, 3);

        assertNotNull(page1);
        assertNotNull(page2);
        assertNotNull(page3);
        assertEquals(3, page1.size());
        assertEquals(3, page2.size());
        assertEquals(3, page3.size());
    }

    @Test
    public void testQueryEmptyRecords() {
        // 查询不存在的用户记录
        String nonExistentUserId = UUID.randomUUID().toString();
        
        // 验证查询结果为空
        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(nonExistentUserId, 1, 10);
        assertNotNull(records);
        assertTrue(records.isEmpty());

        // 验证总记录数为0
        long count = billingRecordDomainService.countRecords(nonExistentUserId);
        assertEquals(0, count);
    }

    @Test
    public void testCreateRecordWithNullValues() {
        // 准备测试数据
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String ruleId = UUID.randomUUID().toString();
        String ruleVersionId = UUID.randomUUID().toString();

        // 1. 创建规则
        RuleEntity rule = new RuleEntity();
        rule.setId(ruleId);
        rule.setVersion("1.0");
        rule.setDescription("测试规则描述");
        BillingRule billingRule = new BillingRule();
        billingRule.setInputToken(0.04);
        billingRule.setOutputToken(0.07);
        rule.setRule(billingRule);
        ruleRepository.insert(rule);

        // 2. 创建规则版本
        RuleVersionEntity ruleVersion = new RuleVersionEntity();
        ruleVersion.setId(ruleVersionId);
        ruleVersion.setRuleId(ruleId);
        ruleVersion.setRule(billingRule);
        ruleVersion.setVersion("1.0");
        ruleVersion.setDescription("测试计费规则");
        ruleVersionRepository.insert(ruleVersion);

        // 3. 创建产品并关联规则
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        product.setProductName("测试产品");
        product.setProductType("CHAT");
        product.setDescription("测试产品描述");
        product.setRuleId(ruleId);
        product.setUserId(userId);
        product.setIsEnabled(true);

        // 创建账单记录，部分字段为null
        BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
            userId,
            productId,
            ruleVersionId,
            null,  // totalAmount
            null   // amountLeft
        );

        // 验证创建结果
        assertNotNull(record, "创建记录不应该返回null");
        assertNotNull(record.getId(), "记录ID不应该为null");
        assertEquals(userId, record.getUserId());
        assertEquals(productId, record.getProductId());
        assertEquals(ruleVersionId, record.getRuleVersionId());
        assertEquals("输入token计费：0.04/1k，输出token计费：0.07/1k", record.getPriceRule());
        assertNull(record.getTotalAmount());
        assertNull(record.getAmountLeft());
    }

    /**
     * 验证字符串是否为有效的UUID
     */
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
} 
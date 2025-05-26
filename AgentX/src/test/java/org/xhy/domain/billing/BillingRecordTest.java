package org.xhy.domain.billing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.service.BillingRecordDomainService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 账单记录服务测试类
 */
@SpringBootTest
@Transactional
public class BillingRecordTest {

    @Autowired
    private BillingRecordDomainService billingRecordDomainService;

    @Test
    public void testCreateAndQueryRecord() {
        // 准备测试数据
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String ruleVersionId = UUID.randomUUID().toString();
        String priceRule = "{\"type\":\"fixed\",\"amount\":100}";
        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal amountLeft = new BigDecimal("100.00");

        // 创建账单记录
        BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
            userId,
            productId,
            ruleVersionId,
            priceRule,
            totalAmount,
            amountLeft
        );

        // 验证创建结果
        assertNotNull(record, "创建记录不应该返回null");
        assertNotNull(record.getId(), "记录ID不应该为null");
        assertTrue(isValidUUID(record.getId()), "ID应该是有效的UUID");
        assertEquals(userId, record.getUserId());
        assertEquals(productId, record.getProductId());
        assertEquals(ruleVersionId, record.getRuleVersionId());
        assertEquals(priceRule, record.getPrice_rule());
        assertEquals(totalAmount, record.getTotalAmount());
        assertEquals(amountLeft, record.getAmountLeft());

        // 查询账单记录
        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(userId, 1, 10);

        // 验证查询结果
        assertNotNull(records);
        assertFalse(records.isEmpty());
        assertEquals(1, records.size());

        BillingUsageRecordEntity queriedRecord = records.get(0);
        assertEquals(record.getId(), queriedRecord.getId());
        assertEquals(userId, queriedRecord.getUserId());
        assertEquals(productId, queriedRecord.getProductId());
        assertEquals(ruleVersionId, queriedRecord.getRuleVersionId());
        assertEquals(priceRule, queriedRecord.getPrice_rule());
        assertEquals(totalAmount, queriedRecord.getTotalAmount());
        assertEquals(amountLeft, queriedRecord.getAmountLeft());

        // 验证总记录数
        long count = billingRecordDomainService.countRecords(userId);
        assertEquals(1, count);
    }

    @Test
    public void testCreateMultipleRecordsAndPagination() {
        // 准备测试数据
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();
        String ruleVersionId = UUID.randomUUID().toString();
        String priceRule = "{\"type\":\"fixed\",\"amount\":100}";
        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal amountLeft = new BigDecimal("100.00");

        // 创建10条记录
        for (int i = 0; i < 10; i++) {
            BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
                userId,
                productId,
                ruleVersionId,
                priceRule,
                totalAmount,
                amountLeft
            );
            assertNotNull(record);
            assertNotNull(record.getId());
        }

        // 测试第一页（每页3条记录）
        List<BillingUsageRecordEntity> page1 = billingRecordDomainService.queryRecords(userId, 1, 3);
        assertEquals(3, page1.size(), "第一页应该有3条记录");

        // 测试第二页
        List<BillingUsageRecordEntity> page2 = billingRecordDomainService.queryRecords(userId, 2, 3);
        assertEquals(3, page2.size(), "第二页应该有3条记录");

        // 测试第三页
        List<BillingUsageRecordEntity> page3 = billingRecordDomainService.queryRecords(userId, 3, 3);
        assertEquals(3, page3.size(), "第三页应该有3条记录");

        // 测试第四页（最后一页）
        List<BillingUsageRecordEntity> page4 = billingRecordDomainService.queryRecords(userId, 4, 3);
        assertEquals(1, page4.size(), "第四页应该有1条记录");

        // 验证总记录数
        long count = billingRecordDomainService.countRecords(userId);
        assertEquals(10, count, "总记录数应该是10条");

        // 验证不同页的记录不重复
        assertFalse(page1.stream().anyMatch(r1 -> 
            page2.stream().anyMatch(r2 -> r1.getId().equals(r2.getId()))), 
            "第一页和第二页的记录不应该重复");
        assertFalse(page2.stream().anyMatch(r1 -> 
            page3.stream().anyMatch(r2 -> r1.getId().equals(r2.getId()))), 
            "第二页和第三页的记录不应该重复");
        assertFalse(page3.stream().anyMatch(r1 -> 
            page4.stream().anyMatch(r2 -> r1.getId().equals(r2.getId()))), 
            "第三页和第四页的记录不应该重复");
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
        String ruleVersionId = UUID.randomUUID().toString();
        
        // 创建账单记录，部分字段为null
        BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
            userId,
            productId,
            ruleVersionId,
            null,  // priceRule
            null,  // totalAmount
            null   // amountLeft
        );

        // 验证创建结果
        assertNotNull(record, "创建记录不应该返回null");
        assertNotNull(record.getId(), "记录ID不应该为null");
        assertTrue(isValidUUID(record.getId()), "ID应该是有效的UUID");
        assertEquals(userId, record.getUserId());
        assertEquals(productId, record.getProductId());
        assertEquals(ruleVersionId, record.getRuleVersionId());
        assertNull(record.getPrice_rule());
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
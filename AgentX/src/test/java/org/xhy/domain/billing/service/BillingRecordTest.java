//package org.xhy.domain.billing.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
//import org.xhy.domain.billing.service.BillingRecordDomainService;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class BillingRecordTest {
//
//    @Autowired
//    private BillingRecordDomainService billingRecordDomainService;
//
//    @Test
//    void insertTestRecords() {
//        String userId = "4a7dbafc4940428e06b1fec33e2bdb4a";
//
//        // 创建第一条记录 - 聊天助手
//        String productId1 = UUID.randomUUID().toString();
//        String ruleVersionId1 = UUID.randomUUID().toString();
//        String priceRule1 = "{\"type\":\"fixed\",\"amount\":100}";
//        BigDecimal totalAmount1 = new BigDecimal("100.00");
//        BigDecimal amountLeft1 = new BigDecimal("75.00");
//
//        BillingUsageRecordEntity record1 = billingRecordDomainService.createRecord(
//            userId,
//            productId1,
//            ruleVersionId1,
//            priceRule1,
//            totalAmount1,
//            amountLeft1
//        );
//
//        // 创建第二条记录 - 智能代理
//        String productId2 = UUID.randomUUID().toString();
//        String ruleVersionId2 = UUID.randomUUID().toString();
//        String priceRule2 = "{\"type\":\"usage\",\"unit\":\"message\",\"price\":0.1}";
//        BigDecimal totalAmount2 = new BigDecimal("200.00");
//        BigDecimal amountLeft2 = new BigDecimal("150.00");
//
//        BillingUsageRecordEntity record2 = billingRecordDomainService.createRecord(
//            userId,
//            productId2,
//            ruleVersionId2,
//            priceRule2,
//            totalAmount2,
//            amountLeft2
//        );
//
//        // 创建第三条记录 - 数据分析
//        String productId3 = UUID.randomUUID().toString();
//        String ruleVersionId3 = UUID.randomUUID().toString();
//        String priceRule3 = "{\"type\":\"tiered\",\"tiers\":[{\"limit\":1000,\"price\":0.1},{\"limit\":5000,\"price\":0.08},{\"price\":0.05}]}";
//        BigDecimal totalAmount3 = new BigDecimal("500.00");
//        BigDecimal amountLeft3 = new BigDecimal("300.00");
//
//        BillingUsageRecordEntity record3 = billingRecordDomainService.createRecord(
//            userId,
//            productId3,
//            ruleVersionId3,
//            priceRule3,
//            totalAmount3,
//            amountLeft3
//        );
//
//        // 验证记录是否创建成功
//        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(userId, 1, 10);
//        assertEquals(3, records.size(), "应该创建了3条记录");
//
//        // 验证总记录数
//        long count = billingRecordDomainService.countRecords(userId);
//        assertEquals(3, count, "总记录数应该是3");
//    }
//}
package org.xhy.domain.billing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhy.domain.billing.model.dto.UserBillingCountEntity;
import org.xhy.domain.billing.repository.UserBillingCountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户账单统计测试类
 */
@SpringBootTest
public class BillingTest {
    @Autowired
    private UserBillingCountRepository billingCountRepository;

    @Test
    public void testBilling() {
        // 创建用户账单统计记录
        UserBillingCountEntity userBillingCountEntity = new UserBillingCountEntity();
        userBillingCountEntity.setUserId("4a7dbafc4940428e06b1fec33e2bdb4a");
        userBillingCountEntity.setBalance(new BigDecimal("1000.0000"));
        userBillingCountEntity.setCumulativeRechargeAmount(new BigDecimal("2000.0000"));
        userBillingCountEntity.setLastTransactionAt(LocalDateTime.now());

        // 保存记录
        billingCountRepository.insert(userBillingCountEntity);

        // 验证记录是否创建成功
        assertNotNull(userBillingCountEntity.getId(), "ID不应该为null");
        
        // 查询并验证记录
        UserBillingCountEntity queriedEntity = billingCountRepository.selectById(userBillingCountEntity.getId());
        assertNotNull(queriedEntity, "查询结果不应该为null");
        assertEquals("4a7dbafc4940428e06b1fec33e2bdb4a", queriedEntity.getUserId(), "用户ID应该匹配");
        assertEquals(new BigDecimal("1000.0000"), queriedEntity.getBalance(), "余额应该匹配");
        assertEquals(new BigDecimal("2000.0000"), queriedEntity.getCumulativeRechargeAmount(), "累计充值金额应该匹配");
        assertNotNull(queriedEntity.getLastTransactionAt(), "最后交易时间不应该为null");

        // 测试余额变动
        queriedEntity.setBalance(new BigDecimal("1500.5000"));
        queriedEntity.setCumulativeRechargeAmount(new BigDecimal("2500.7500"));
        queriedEntity.setLastTransactionAt(LocalDateTime.now());
        billingCountRepository.updateById(queriedEntity);

        // 验证更新后的记录
        UserBillingCountEntity updatedEntity = billingCountRepository.selectById(queriedEntity.getId());
        assertEquals(new BigDecimal("1500.5000"), updatedEntity.getBalance(), "更新后的余额应该匹配");
        assertEquals(new BigDecimal("2500.7500"), updatedEntity.getCumulativeRechargeAmount(), "更新后的累计充值金额应该匹配");

        // 测试精度计算
        BigDecimal newBalance = updatedEntity.getBalance()
            .add(new BigDecimal("123.4567"))
            .setScale(4, RoundingMode.HALF_UP);
        updatedEntity.setBalance(newBalance);
        billingCountRepository.updateById(updatedEntity);

        // 验证精度计算后的结果
        UserBillingCountEntity finalEntity = billingCountRepository.selectById(updatedEntity.getId());
        assertEquals(new BigDecimal("1623.9567"), finalEntity.getBalance(), "精度计算后的余额应该匹配");
    }
}

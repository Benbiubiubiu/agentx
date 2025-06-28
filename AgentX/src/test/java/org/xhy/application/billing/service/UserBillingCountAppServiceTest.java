package org.xhy.application.billing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.domain.billing.model.dto.UserBillingCountEntity;
import org.xhy.domain.billing.repository.UserBillingCountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserBillingCountAppServiceTest {

    @Autowired
    private UserBillingCountAppService userBillingCountAppService;

    @Autowired
    private UserBillingCountRepository userBillingCountRepository;

    private static final String TEST_USER_ID = "test-user-id";

    @BeforeEach
    void setUp() {
        // 创建测试用的用户账单统计记录
        UserBillingCountEntity entity = new UserBillingCountEntity();
        entity.setUserId(TEST_USER_ID);
        entity.setBalance(new BigDecimal("1000.00"));
        entity.setCumulativeRechargeAmount(new BigDecimal("2000.00"));
        entity.setLastTransactionAt(LocalDateTime.now());
        userBillingCountRepository.insert(entity);
    }

    @Test
    void testGetBalance() {
        // 执行查询
        BalanceDTO result = userBillingCountAppService.getBalance(TEST_USER_ID);

        // 验证结果
        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        assertEquals(new BigDecimal("2000.00"), result.getCumulativeRechargeAmount());
        assertNotNull(result.getLastTransactionAt());
    }

    @Test
    void testGetBalanceForNewUser() {
        // 执行查询
        BalanceDTO result = userBillingCountAppService.getBalance("new-user-id");

        // 验证结果
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertEquals(BigDecimal.ZERO, result.getCumulativeRechargeAmount());
        assertNotNull(result.getLastTransactionAt());
    }
} 
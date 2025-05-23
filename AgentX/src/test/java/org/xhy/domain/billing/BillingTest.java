package org.xhy.domain.billing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xhy.domain.billing.entity.UserBillingCountEntity;
import org.xhy.domain.billing.repository.UserBillingCountRepository;

import java.math.BigDecimal;

/**
 * XHY
 * 2025/5/17 22:12
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@SpringBootTest
public class BillingTest {
    @Autowired
    private UserBillingCountRepository billingCountRepository;

    @Test
    public void testBilling() {
        UserBillingCountEntity userBillingCountEntity = new UserBillingCountEntity();
        userBillingCountEntity.setUserId("123");
        userBillingCountEntity.setBalance(new BigDecimal("100"));
        billingCountRepository.insert(userBillingCountEntity);

    }
}

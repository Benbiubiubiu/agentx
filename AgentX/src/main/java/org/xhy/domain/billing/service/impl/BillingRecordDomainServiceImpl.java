package org.xhy.domain.billing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Service
public class BillingRecordDomainServiceImpl implements BillingRecordDomainService {

    @Autowired
    private BillingRecordRepository billingRecordRepository;

    @Override
    public List<BillingUsageRecordEntity> queryRecords(String userId,int pageNum, int pageSize) {
        return billingRecordRepository.queryRecords(userId, pageNum, pageSize);
    }

    @Override
    public long countRecords(String userId) {
        return billingRecordRepository.countRecords(userId);
    }

    @Override
    public BillingUsageRecordEntity createRecord(String userId, 
                                               String productId,
                                               String ruleVersionId,
                                               String priceRule,
                                               BigDecimal totalAmount,
                                               BigDecimal amountLeft) {
        BillingUsageRecordEntity record = new BillingUsageRecordEntity();
        record.setUserId(userId);
        record.setProductId(productId);
        record.setRuleVersionId(ruleVersionId);
        record.setPrice_rule(priceRule);
        record.setTotalAmount(totalAmount);
        record.setAmountLeft(amountLeft);
        
        int rows = billingRecordRepository.createRecord(record);
        if (rows > 0) {
            return record;
        }
        return null;
    }
} 
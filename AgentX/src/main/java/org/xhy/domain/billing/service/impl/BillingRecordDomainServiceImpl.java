package org.xhy.domain.billing.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingRecordDomainServiceImpl implements BillingRecordDomainService {

    @Autowired
    private BillingRecordRepository billingRecordRepository;

    @Override
    public List<BillingUsageRecordEntity> queryRecords(String userId,                                                       int pageNum, int pageSize) {
        return billingRecordRepository.queryRecords(userId, pageNum, pageSize);
    }

    @Override
    public long countRecords(String userId) {
        return billingRecordRepository.countRecords(userId);
    }
} 
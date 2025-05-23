package org.xhy.domain.billing.service;

import org.xhy.domain.billing.entity.BillingUsageRecordEntity;

import java.util.List;
import java.time.LocalDateTime;

public interface BillingRecordDomainService {
    /**
     * 查询账单记录
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 账单记录列表
     */
    List<BillingUsageRecordEntity> queryRecords(String userId,int pageNum, int pageSize);

    /**
     * 查询总记录数
     * @param userId 用户ID
     * @param productId 产品ID
     * @param ruleVersionId 规则版本ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总记录数
     */
    long countRecords(String userId);
} 
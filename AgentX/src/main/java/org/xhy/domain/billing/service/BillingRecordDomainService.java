package org.xhy.domain.billing.service;

import org.xhy.domain.billing.entity.BillingUsageRecordEntity;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;

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
     * @return 总记录数
     */
    long countRecords(String userId);

    /**
     * 创建账单记录
     * @param userId 用户ID
     * @param productId 产品ID
     * @param ruleVersionId 规则版本ID
     * @param priceRule 价格规则
     * @param totalAmount 总金额
     * @param amountLeft 剩余金额
     * @return 创建的账单记录
     */
    BillingUsageRecordEntity createRecord(
                                          String userId,
                                          String productId,
                                          String ruleVersionId,
                                          String priceRule,
                                          BigDecimal totalAmount,
                                          BigDecimal amountLeft);
} 
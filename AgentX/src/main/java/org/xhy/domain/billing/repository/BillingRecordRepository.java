package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

import java.util.List;
import java.time.LocalDateTime;
@Mapper
public interface BillingRecordRepository extends MyBatisPlusExtRepository<BillingUsageRecordEntity> {
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
     * @param record 账单记录实体
     * @return 影响的行数
     */
    int createRecord(BillingUsageRecordEntity record);
} 
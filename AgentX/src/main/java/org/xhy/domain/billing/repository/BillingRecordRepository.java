package org.xhy.domain.billing.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

@Mapper
public interface BillingRecordRepository extends MyBatisPlusExtRepository<BillingUsageRecordEntity> {
    
//    /**
//     * 分页查询账单记录
//     * @param userId 用户ID
//     * @param page 分页参数
//     * @return 分页结果
//     */
//    default Page<BillingUsageRecordEntity> queryRecords(String userId, Page<BillingUsageRecordEntity> page) {
//        LambdaQueryWrapper<BillingUsageRecordEntity> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(BillingUsageRecordEntity::getUserId, userId)
//               .orderByDesc(BillingUsageRecordEntity::getCreatedAt);
//        return selectPage(page, wrapper);
//    }

    /**
     * 查询总记录数
     * @param userId 用户ID
     * @return 记录数
     */
    default long countRecords(String userId) {
        LambdaQueryWrapper<BillingUsageRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillingUsageRecordEntity::getUserId, userId);
        return selectCount(wrapper);
    }

    /**
     * 创建账单记录
     * @param record 账单记录实体
     * @return 影响的行数
     */
    default int createRecord(BillingUsageRecordEntity record) {
        return insert(record);
    }
} 
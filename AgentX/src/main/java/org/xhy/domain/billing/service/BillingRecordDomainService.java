package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.billing.model.dto.BillingStatistics;
import org.xhy.domain.rule.model.config.BaseRule;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import com.alibaba.fastjson.JSON;
import org.xhy.infrastructure.exception.BusinessException;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

/**
 * 账单记录领域服务
 * 负责账单记录的核心业务逻辑，包括查询和创建账单记录
 */
@Service
public class BillingRecordDomainService {
    
    private final BillingRecordRepository billingRecordRepository;
    private final RuleVersionRepository ruleVersionRepository;

    public BillingRecordDomainService(
            BillingRecordRepository billingRecordRepository,
            RuleVersionRepository ruleVersionRepository) {
        this.billingRecordRepository = billingRecordRepository;
        this.ruleVersionRepository = ruleVersionRepository;
    }

    /**
     * 查询账单记录列表
     * @param userId 用户ID
     * @param page 分页参数
     * @return 账单记录列表
     */
    public Page<BillingUsageRecordEntity> queryRecords(String userId, Page<BillingUsageRecordEntity> page) {
        LambdaQueryWrapper<BillingUsageRecordEntity> queryWrapper = new LambdaQueryWrapper<BillingUsageRecordEntity>()
                .eq(BillingUsageRecordEntity::getUserId, userId)
                .orderByDesc(BillingUsageRecordEntity::getCreatedAt);
        return billingRecordRepository.selectPage(page, queryWrapper);
    }

    /**
     * 创建账单记录（领域层核心业务逻辑）
     * @param record 账单记录实体
     * @return 创建的账单记录实体
     */
    public BillingUsageRecordEntity createBillingRecord(BillingUsageRecordEntity record) {
        // 1. 验证规则版本是否存在
        RuleVersionEntity ruleVersion = ruleVersionRepository.selectById(record.getRuleVersionId());
        if (ruleVersion == null) {
            throw new BusinessException("规则版本不存在: " + record.getRuleVersionId());
        }
        
        // 2. 生成价格规则文本
        String priceRuleText = generatePriceRuleText(ruleVersion.getRule());
        record.setPriceRule(priceRuleText);
        
        // 3. 保存到数据库
        billingRecordRepository.checkInsert(record);
        
        return record;
    }

    /**
     * 创建账单记录
     * @param record 账单记录实体
     */
    public void createRecord(BillingUsageRecordEntity record) {
        billingRecordRepository.checkInsert(record);
    }

    /**
     * 生成价格规则文本
     * @param rule 计费规则
     * @return 价格规则文本
     */
    public String generatePriceRuleText(BaseRule rule) {
        if (rule instanceof BillingRule) {
            BillingRule billingRule = (BillingRule) rule;
            return String.format("输入token计费：%.3f/1k，输出token计费：%.3f/1k",
                    billingRule.getInputToken(),
                    billingRule.getOutputToken());
        }
        return "未知规则类型";
    }

    /**
     * 获取账单统计信息
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productId 产品ID（可选）
     * @param ruleVersionId 规则版本ID（可选）
     * @return 统计信息
     */
    public BillingStatistics getBillingStatistics(String userId, LocalDateTime startTime, LocalDateTime endTime,
                                                 String productId, String ruleVersionId) {
        // 1. 构建查询条件
        LambdaQueryWrapper<BillingUsageRecordEntity> queryWrapper = new LambdaQueryWrapper<BillingUsageRecordEntity>()
                .eq(BillingUsageRecordEntity::getUserId, userId)
                .between(BillingUsageRecordEntity::getCreatedAt, startTime, endTime);
        
        // 2. 添加可选条件
        if (productId != null && !productId.isEmpty()) {
            queryWrapper.eq(BillingUsageRecordEntity::getProductId, productId);
        }
        if (ruleVersionId != null && !ruleVersionId.isEmpty()) {
            queryWrapper.eq(BillingUsageRecordEntity::getRuleVersionId, ruleVersionId);
        }
        
        // 3. 查询所有符合条件的记录
        List<BillingUsageRecordEntity> records = billingRecordRepository.selectList(queryWrapper);
        
        // 4. 计算统计信息
        return calculateStatistics(records, startTime, endTime);
    }

    /**
     * 计算统计信息
     * @param records 账单记录列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计信息
     */
    private BillingStatistics calculateStatistics(List<BillingUsageRecordEntity> records, 
                                                 LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime monthStart = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal monthlyAmount = BigDecimal.ZERO;
        BigDecimal todayAmount = BigDecimal.ZERO;
        BigDecimal maxAmount = BigDecimal.ZERO;
        BigDecimal minAmount = records.isEmpty() ? BigDecimal.ZERO : records.get(0).getTotalAmount();
        
        for (BillingUsageRecordEntity record : records) {
            BigDecimal amount = record.getTotalAmount();
            totalAmount = totalAmount.add(amount);
            
            // 计算本月金额
            if (record.getCreatedAt().isAfter(monthStart)) {
                monthlyAmount = monthlyAmount.add(amount);
            }
            
            // 计算今日金额
            if (record.getCreatedAt().isAfter(todayStart)) {
                todayAmount = todayAmount.add(amount);
            }
            
            // 计算最大最小金额
            if (amount.compareTo(maxAmount) > 0) {
                maxAmount = amount;
            }
            if (amount.compareTo(minAmount) < 0) {
                minAmount = amount;
            }
        }
        
        // 计算平均金额
        BigDecimal averageAmount = records.isEmpty() ? BigDecimal.ZERO : 
                totalAmount.divide(BigDecimal.valueOf(records.size()), 4, BigDecimal.ROUND_HALF_UP);
        
        // 计算次数统计
        long totalCount = records.size();
        long monthlyCount = records.stream()
                .filter(record -> record.getCreatedAt().isAfter(monthStart))
                .count();
        long todayCount = records.stream()
                .filter(record -> record.getCreatedAt().isAfter(todayStart))
                .count();
        
        return new BillingStatistics(totalAmount, monthlyAmount, todayAmount,
                                   totalCount, monthlyCount, todayCount,
                                   averageAmount, maxAmount, minAmount,
                                   startTime, endTime);
    }
} 
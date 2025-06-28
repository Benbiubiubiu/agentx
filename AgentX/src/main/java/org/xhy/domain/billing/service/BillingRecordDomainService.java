package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.rule.model.config.BaseRule;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.math.BigDecimal;

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
} 
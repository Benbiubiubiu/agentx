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
     * 查询总记录数
     * @param userId 用户ID
     * @return 总记录数
     */
    public long countRecords(String userId) {
        LambdaQueryWrapper<BillingUsageRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillingUsageRecordEntity::getUserId, userId);
        return billingRecordRepository.selectCount(wrapper);
    }

    /**
     * 创建账单记录
     * @param userId 用户ID
     * @param productId 产品ID
     * @param ruleVersionId 规则版本ID
     * @param totalAmount 总金额
     * @param amountLeft 剩余金额
     * @return 创建的账单记录
     */
    public BillingUsageRecordEntity createRecord(
            String userId,
            String productId,
            String ruleVersionId,
            BigDecimal totalAmount,
            BigDecimal amountLeft) {
        
        // 获取规则版本信息
        RuleVersionEntity ruleVersion = ruleVersionRepository.selectById(ruleVersionId);
        if (ruleVersion == null) {
            throw new IllegalArgumentException("规则版本不存在");
        }
        System.out.println("rule_version entity:"+JSON.toJSONString(ruleVersion));
        // 生成价格规则文本
        String priceRuleText = generatePriceRuleText(ruleVersion.getRule());
        
        BillingUsageRecordEntity record = new BillingUsageRecordEntity();
        record.setUserId(userId);
        record.setProductId(productId);
        record.setRuleVersionId(ruleVersionId);
        record.setPriceRule(priceRuleText);
        record.setTotalAmount(totalAmount);
        record.setAmountLeft(amountLeft);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        
        billingRecordRepository.insert(record);
        return record;
    }

    /**
     * 生成价格规则文本
     * @param rule 计费规则
     * @return 价格规则文本
     */
    private String generatePriceRuleText(BaseRule rule) {
        if (rule instanceof BillingRule) {
            BillingRule billingRule = (BillingRule) rule;
            return String.format("输入token计费：%.3f/1k，输出token计费：%.3f/1k",
                    billingRule.getInputToken(),
                    billingRule.getOutputToken());
        }
        return "未知规则类型";
    }
} 
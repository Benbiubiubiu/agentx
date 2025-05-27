package org.xhy.domain.billing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.model.BaseRule;
import org.xhy.domain.billing.model.BillingRule;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import org.xhy.domain.billing.entity.RuleVersionEntity;
import org.xhy.domain.billing.repository.RuleVersionRepository;
import org.xhy.interfaces.dto.billing.PageRequest;
import org.xhy.interfaces.dto.billing.RecordListDTO;
import org.xhy.interfaces.dto.billing.PageResult;

import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * 账单记录领域服务
 * 负责账单记录的核心业务逻辑，包括查询和创建账单记录
 */
@Service
public class BillingRecordDomainService {
    
    @Autowired
    private BillingRecordRepository billingRecordRepository;

    @Autowired
    private RuleVersionRepository ruleVersionRepository;

    /**
     * 查询账单记录
     * @param userId 用户ID
     * @param request 分页请求
     * @return 分页结果
     */
    public PageResult<RecordListDTO> queryRecords(String userId, PageRequest request) {
        // 查询数据
        List<BillingUsageRecordEntity> records = queryRecords(userId, request.getPage(), request.getSize());
        // 查询总数
        long total = countRecords(userId);
        // 转换为DTO
        List<RecordListDTO> dtoList = records.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return new PageResult<>(dtoList, total, request.getPage(), request.getSize());
    }

    /**
     * 分页查询用户的账单记录
     * 
     * @param userId 用户ID
     * @param pageNum 页码，从1开始
     * @param pageSize 每页大小
     * @return 账单记录列表
     */
    public List<BillingUsageRecordEntity> queryRecords(String userId, int pageNum, int pageSize) {
        Page<BillingUsageRecordEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BillingUsageRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillingUsageRecordEntity::getUserId, userId)
               .orderByDesc(BillingUsageRecordEntity::getCreatedAt);
        return billingRecordRepository.selectPage(page, wrapper).getRecords();
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
            StringBuilder text = new StringBuilder();
            
            // 输入token计费规则
            if (billingRule.getInputToken() != null) {
                text.append("输入token计费：")
                    .append(billingRule.getInputToken().toString())
                    .append("/1k");
            }
            
            // 输出token计费规则
            if (billingRule.getOutputToken() != null) {
                if (text.length() > 0) {
                    text.append("，");
                }
                text.append("输出token计费：")
                    .append(billingRule.getOutputToken().toString())
                    .append("/1k");
            }
            
            return text.toString();
        }
        
        return "未知计费规则";
    }

    /**
     * 将实体转换为DTO
     */
    private RecordListDTO convertToDTO(BillingUsageRecordEntity record) {
        RecordListDTO dto = new RecordListDTO();
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setProductId(record.getProductId());
        dto.setRuleVersionId(record.getRuleVersionId());
        dto.setPriceRule(record.getPriceRule());
        dto.setTotalAmount(record.getTotalAmount());
        dto.setAmountLeft(record.getAmountLeft());
        dto.setCreateTime(record.getCreatedAt());
        return dto;
    }
} 
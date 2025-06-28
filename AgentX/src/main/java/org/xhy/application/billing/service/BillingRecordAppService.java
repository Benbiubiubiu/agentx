package org.xhy.application.billing.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.BillingRecordAssembler;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.tool.service.ToolAppService;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.service.RuleVersionDomainService;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * 账单记录应用服务
 * 用于适配领域层的账单记录服务
 * 职责：
 * 1. 接收和验证来自接口层的请求
 * 2. 将请求转换为领域对象或参数
 * 3. 调用领域服务执行业务逻辑
 * 4. 转换和返回结果给接口层
 */
@Service
public class BillingRecordAppService {

    private static final Logger logger = LoggerFactory.getLogger(BillingRecordAppService.class);

    private final BillingRecordDomainService billingRecordDomainService;
    private final RuleVersionDomainService ruleVersionDomainService;

    public BillingRecordAppService(BillingRecordDomainService billingRecordDomainService, RuleVersionDomainService ruleVersionDomainService) {
        this.billingRecordDomainService = billingRecordDomainService;
        this.ruleVersionDomainService = ruleVersionDomainService;
    }

    /**
     * 查询账单记录列表
     * @param page 分页参数
     * @return 账单记录列表
     */
    @Transactional(readOnly = true)
    public Page<BillingRecordDTO> queryRecords(Page<BillingUsageRecordEntity> page,String userId) {
        Page<BillingUsageRecordEntity> recordPage = billingRecordDomainService.queryRecords(userId, page);
        
        // 转换为DTO
        Page<BillingRecordDTO> dtoPage = new Page<>(recordPage.getCurrent(), recordPage.getSize(), recordPage.getTotal());
        dtoPage.setRecords(recordPage.getRecords().stream()
                .map(BillingRecordAssembler::toDTO)
                .collect(Collectors.toList()));
        
        return dtoPage;
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
    public BillingRecordDTO createRecord(
            String userId,
            String productId,
            String ruleVersionId,
            BigDecimal totalAmount,
            BigDecimal amountLeft) {

        RuleVersionEntity ruleVersion = ruleVersionDomainService.getRuleVersion(ruleVersionId);
        if (ruleVersion == null) {
            logger.error("规则版本不存在，ruleVersionId: {}", ruleVersionId);
            throw new IllegalArgumentException("规则版本不存在");
        }
        System.out.println("rule_version entity:"+ JSON.toJSONString(ruleVersion));
        // 生成价格规则文本
        String priceRuleText = billingRecordDomainService.generatePriceRuleText(ruleVersion.getRule());

        BillingUsageRecordEntity record = new BillingUsageRecordEntity();
        record.setUserId(userId);
        record.setProductId(productId);
        record.setRuleVersionId(ruleVersionId);
        record.setPriceRule(priceRuleText);
        record.setTotalAmount(totalAmount);
        record.setAmountLeft(amountLeft);
        billingRecordDomainService.createRecord(record);
        // 转换为DTO
        return BillingRecordAssembler.toDTO(record);
    }
} 
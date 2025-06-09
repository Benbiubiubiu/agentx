package org.xhy.application.billing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.BillingRecordAssembler;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.infrastructure.auth.UserContext;

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

    private final BillingRecordDomainService billingRecordDomainService;

    public BillingRecordAppService(BillingRecordDomainService billingRecordDomainService) {
        this.billingRecordDomainService = billingRecordDomainService;
    }

    /**
     * 查询账单记录列表
     * @param page 分页参数
     * @return 账单记录列表
     */
    @Transactional(readOnly = true)
    public Page<BillingRecordDTO> queryRecords(Page<BillingUsageRecordEntity> page) {
        String userId = UserContext.getCurrentUserId();
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
        
        // 调用领域服务创建记录
        BillingUsageRecordEntity record = billingRecordDomainService.createRecord(
            userId,
            productId,
            ruleVersionId,
            totalAmount,
            amountLeft
        );
        
        // 转换为DTO
        return BillingRecordAssembler.toDTO(record);
    }
} 
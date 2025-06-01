package org.xhy.application.billing.service;

import org.springframework.stereotype.Service;
import org.xhy.application.billing.assembler.BillingRecordAssembler;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.infrastructure.auth.UserContext;
import org.xhy.interfaces.dto.billing.PageRequest;
import org.xhy.interfaces.dto.billing.PageResult;

import java.math.BigDecimal;
import java.util.List;

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
     * 查询账单记录
     * @param request 分页请求
     * @return 分页结果
     */
    public PageResult<BillingRecordDTO> queryRecords(PageRequest request) {
        String userId = UserContext.getCurrentUserId();
        // 调用领域服务查询数据
        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(
            userId,
            request.getPage(), 
            request.getSize()
        );
        
        // 查询总数
        long total = billingRecordDomainService.countRecords(userId);
        
        // 转换为DTO
        List<BillingRecordDTO> dtoList = BillingRecordAssembler.toDTOs(records);
        
        return new PageResult<>(dtoList, total, request.getPage(), request.getSize());
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
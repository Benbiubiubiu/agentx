package org.xhy.application.billing.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.billing.assembler.BillingRecordAssembler;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.billing.dto.CreateBillingRecordRequest;
import org.xhy.application.billing.dto.BillingStatisticsDTO;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.billing.model.dto.BillingStatistics;
import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.service.RuleVersionDomainService;
import org.xhy.infrastructure.exception.BusinessException;
import org.xhy.interfaces.dto.billing.GetBillingStatisticsRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public BillingRecordAppService(BillingRecordDomainService billingRecordDomainService) {
        this.billingRecordDomainService = billingRecordDomainService;
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
     * 获取账单统计信息
     * @param userId 用户ID
     * @param request 统计请求
     * @return 账单统计信息
     */
    @Transactional(readOnly = true)
    public BillingStatisticsDTO getBillingStatistics(String userId, GetBillingStatisticsRequest request) {
        // 1. 参数验证
        validateStatisticsRequest(request);
        
        // 2. 调用领域服务获取统计信息
        BillingStatistics statistics = billingRecordDomainService.getBillingStatistics(
            userId, 
            request.getStartTime(), 
            request.getEndTime(),
            request.getProductId(),
            request.getRuleVersionId()
        );
        
        // 3. 转换为DTO
        return BillingRecordAssembler.toStatisticsDTO(statistics);
    }

    /**
     * 验证统计请求参数
     * @param request 统计请求
     */
    private void validateStatisticsRequest(GetBillingStatisticsRequest request) {
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new BusinessException("开始时间和结束时间不能为空");
        }
        
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BusinessException("开始时间不能晚于结束时间");
        }
        
        // 限制查询时间范围，防止查询时间过长
        LocalDateTime maxStartTime = LocalDateTime.now().minusYears(1);
        if (request.getStartTime().isBefore(maxStartTime)) {
            throw new BusinessException("查询时间范围不能超过一年");
        }
    }
} 
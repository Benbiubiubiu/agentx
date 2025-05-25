package org.xhy.application.billing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;

import org.xhy.domain.billing.service.BillingRecordDomainService;
import org.xhy.interfaces.dto.billing.RecordListDTO;
import org.xhy.interfaces.dto.billing.RecordQueryRequest;
import org.xhy.interfaces.dto.billing.PageResult;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingRecordApplicationService {

    @Autowired
    private BillingRecordDomainService billingRecordDomainService;

    public PageResult<RecordListDTO> queryRecords(RecordQueryRequest request) {
        // 调用领域服务查询数据
        List<BillingUsageRecordEntity> records = billingRecordDomainService.queryRecords(
            request.getUserId(),
            request.getPageNum(),
            request.getPageSize()
        );

        // 查询总数
        long total = billingRecordDomainService.countRecords(request.getUserId()
        );

        // 转换为DTO
        List<RecordListDTO> dtoList = records.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());

        return new PageResult<>(dtoList, total, request.getPageNum(), request.getPageSize());
    }

    private RecordListDTO convertToDTO(BillingUsageRecordEntity record) {
        RecordListDTO dto = new RecordListDTO();
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setProductId(record.getProductId());
        dto.setRuleVersionId(record.getRuleVersionId());
        dto.setPriceRule(record.getPrice_rule());
        dto.setTotalAmount(record.getTotalAmount());
        dto.setAmountLeft(record.getAmountLeft());
        dto.setCreateTime(record.getCreatedAt());
        return dto;
    }
} 
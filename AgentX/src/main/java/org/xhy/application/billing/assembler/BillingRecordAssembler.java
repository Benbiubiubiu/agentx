package org.xhy.application.billing.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.application.billing.dto.BillingStatisticsDTO;
import org.xhy.application.billing.dto.CreateBillingRecordRequest;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;
import org.xhy.domain.billing.model.dto.BillingStatistics;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账单记录领域对象组装器
 * 负责DTO和Entity之间的转换
 */
public class BillingRecordAssembler {

    /**
     * 将BillingUsageRecordEntity转换为BillingRecordDTO
     */
    public static BillingRecordDTO toDTO(BillingUsageRecordEntity entity) {
        if (entity == null) {
            return null;
        }
        BillingRecordDTO dto = new BillingRecordDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 将BillingUsageRecordEntity列表转换为BillingRecordDTO列表
     */
    public static List<BillingRecordDTO> toDTOs(List<BillingUsageRecordEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(BillingRecordAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 从CreateBillingRecordRequest创建BillingUsageRecordEntity
     * @param request 创建账单记录请求
     * @return 账单记录实体
     */
    public static BillingUsageRecordEntity toEntity(CreateBillingRecordRequest request) {
        if (request == null) {
            return null;
        }
        
        BillingUsageRecordEntity entity = new BillingUsageRecordEntity();
        entity.setUserId(request.getUserId());
        entity.setProductId(request.getProductId());
        entity.setRuleVersionId(request.getRuleVersionId());
        entity.setTotalAmount(request.getTotalAmount());
        entity.setAmountLeft(request.getAmountLeft());
        
        return entity;
    }

    /**
     * 将BillingStatistics转换为BillingStatisticsDTO
     * @param statistics 账单统计领域模型
     * @return 账单统计DTO
     */
    public static BillingStatisticsDTO toStatisticsDTO(BillingStatistics statistics) {
        if (statistics == null) {
            return null;
        }
        
        return new BillingStatisticsDTO(
            statistics.getTotalAmount(),
            statistics.getMonthlyAmount(),
            statistics.getTodayAmount(),
            statistics.getTotalCount(),
            statistics.getMonthlyCount(),
            statistics.getTodayCount(),
            statistics.getAverageAmount(),
            statistics.getMaxAmount(),
            statistics.getMinAmount(),
            statistics.getStartTime(),
            statistics.getEndTime()
        );
    }
} 
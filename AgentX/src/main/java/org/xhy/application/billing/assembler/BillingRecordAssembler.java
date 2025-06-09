package org.xhy.application.billing.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.application.billing.dto.BillingRecordDTO;
import org.xhy.domain.billing.model.dto.BillingUsageRecordEntity;

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
} 
package org.xhy.application.billing.assembler;

import org.xhy.application.billing.dto.RuleDTO;
import org.xhy.domain.billing.model.dto.RuleEntity;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则领域对象组装器
 * 负责DTO和Entity之间的转换
 */
public class RuleAssembler {

    /**
     * 将CreateRuleRequest转换为RuleEntity
     */
    public static RuleEntity toEntity(CreateRuleRequest request) {
        RuleEntity entity = new RuleEntity();
        entity.setVersion(request.getVersion());
        entity.setDescription(request.getDescription());
        entity.setRule(request.getRule());

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        return entity;
    }

    /**
     * 将RuleEntity转换为RuleDTO
     */
    public static RuleDTO toDTO(RuleEntity entity) {
        if (entity == null) {
            return null;
        }

        RuleDTO dto = new RuleDTO();
        dto.setId(entity.getId());
        dto.setVersion(entity.getVersion());
        dto.setDescription(entity.getDescription());
        dto.setRule(entity.getRule());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    /**
     * 将RuleEntity列表转换为RuleDTO列表
     */
    public static List<RuleDTO> toDTOs(List<RuleEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(RuleAssembler::toDTO)
                .collect(Collectors.toList());
    }
} 
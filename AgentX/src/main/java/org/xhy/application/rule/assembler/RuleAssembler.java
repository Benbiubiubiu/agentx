package org.xhy.application.rule.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.application.rule.dto.RuleDTO;
import org.xhy.domain.rule.model.dto.RuleEntity;
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
        BeanUtils.copyProperties(request, entity);


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
        BeanUtils.copyProperties(entity, dto);
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
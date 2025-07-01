package org.xhy.application.rule.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.application.rule.dto.RuleDTO;
import org.xhy.application.rule.dto.RuleVersionDTO;
import org.xhy.application.rule.dto.RuleAggregateDTO;
import org.xhy.domain.rule.model.dto.RuleEntity;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.interfaces.dto.rule.CreateRuleRequest;

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
    public static List<RuleDTO> toRuleDTOs(List<RuleEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(RuleAssembler::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将RuleVersionEntity转换为RuleVersionDTO
     */
    public static RuleVersionDTO toRuleVersionDTO(RuleVersionEntity entity) {
        if (entity == null) {
            return null;
        }
        RuleVersionDTO dto = new RuleVersionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 将RuleVersionEntity列表转换为RuleVersionDTO列表
     */
    public static List<RuleVersionDTO> toRuleVersionDTOs(List<RuleVersionEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(RuleAssembler::toRuleVersionDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将规则实体和版本实体列表转换为聚合根DTO
     */
    public static RuleAggregateDTO toAggregateDTO(RuleEntity ruleEntity, List<RuleVersionEntity> versionEntities) {
        RuleDTO ruleDTO = toDTO(ruleEntity);
        List<RuleVersionDTO> versionDTOs = toRuleVersionDTOs(versionEntities);
        return new RuleAggregateDTO(ruleDTO, versionDTOs);
    }
} 
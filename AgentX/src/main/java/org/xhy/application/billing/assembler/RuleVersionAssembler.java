package org.xhy.application.billing.assembler;

import org.springframework.beans.BeanUtils;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;

import java.time.LocalDateTime;

/**
 * 规则版本领域对象组装器
 * 负责DTO和Entity之间的转换
 */
public class RuleVersionAssembler {

    /**
     * 将CreateRuleRequest转换为RuleVersionEntity
     */
    public static RuleVersionEntity toEntity(String ruleId, CreateRuleRequest request, 
                                           LocalDateTime effectiveAt, LocalDateTime expiredAt) {
        RuleVersionEntity entity = new RuleVersionEntity();
        BeanUtils.copyProperties(request, entity);
        entity.setRuleId(ruleId);
        entity.setEffectiveAt(effectiveAt);
        entity.setExpiredAt(expiredAt);
        return entity;
    }
} 
package org.xhy.domain.rule.constant;

import org.xhy.infrastructure.exception.BusinessException;

/**
 * agentx-ben
 * 2025/6/10 13:00
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
public enum RuleType {
    /** PRODUCT */
    PRODUCT(0, "PRODUCT");

    private final Integer code;
    private final String description;

    RuleType (Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RuleType fromCode(Integer code) {
        for (RuleType ruleType : values()) {
            if (ruleType.getCode().equals(code)) {
                return ruleType;
            }
        }
        throw new BusinessException("RULE_TYPE_NOT_FOUND", "规则类型不存在");
    }
}

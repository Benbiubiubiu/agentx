package org.xhy.domain.billing.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.JdbcType;
import org.xhy.domain.billing.model.config.BaseRule;
import org.xhy.infrastructure.converter.BillingRuleConverter;
import org.xhy.infrastructure.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 规则版本实体类
 */
@TableName(value = "rule_version",autoResultMap = true)
public class RuleVersionEntity extends BaseEntity {
    
    /** 版本ID */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 规则ID */
    @TableField("rule_id")
    private String ruleId;

    /** 版本描述 */
    @TableField("description")
    private String description;

    /** 版本号 */
    @TableField("version")
    private String version;

    /** 规则内容 */
    @TableField(value = "rule", typeHandler = BillingRuleConverter.class, jdbcType = JdbcType.OTHER)
    private BaseRule rule;

    /** 生效时间 */
    @TableField("effective_at")
    private LocalDateTime effectiveAt;

    /** 过期时间 */
    @TableField("expired_at")
    private LocalDateTime expiredAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BaseRule getRule() {
        return rule;
    }

    public void setRule(BaseRule rule) {
        this.rule = rule;
    }

    public LocalDateTime getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(LocalDateTime effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
} 
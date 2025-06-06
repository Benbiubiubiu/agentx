package org.xhy.domain.billing.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.xhy.domain.billing.model.config.BaseRule;
import org.xhy.infrastructure.converter.BillingRuleConverter;
import org.xhy.infrastructure.entity.BaseEntity;

/**
 * 规则实体类
 */
@TableName(value = "rule",autoResultMap = true)
public class RuleEntity extends BaseEntity {
    
    /** 规则ID */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 版本号 */
    @TableField("version")
    private String version;

    /** 规则描述 */
    @TableField("description")
    private String description;

    /** 规则内容 */
    @TableField(value = "rule", typeHandler = BillingRuleConverter.class)
    private BaseRule rule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BaseRule getRule() {
        return rule;
    }

    public void setRule(BaseRule rule) {
        this.rule = rule;
    }
} 
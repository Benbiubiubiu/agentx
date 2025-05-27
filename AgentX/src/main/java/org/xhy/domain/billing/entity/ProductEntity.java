package org.xhy.domain.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.xhy.infrastructure.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体
 */
@TableName("product")
public class ProductEntity extends BaseEntity {
    
    /** 产品ID */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /** 用户ID */
    @TableField("user_id")
    private String userId;

    /** 产品名称 */
    @TableField("product_name")
    private String productName;

    /** 产品类型 */
    @TableField("product_type")
    private String productType;

    /** 产品描述 */
    @TableField("description")
    private String description;

    /** 是否启用 */
    @TableField("is_enabled")
    private Boolean isEnabled;

    /** 规则ID */
    @TableField("rule_id")
    private String ruleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}

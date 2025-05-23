package org.xhy.domain.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.xhy.infrastructure.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * XHY
 * 2025/5/17 22:24
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@TableName("billing_usage_record")
public class BillingUsageRecordEntity extends BaseEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("product_id")
    private String productId;

    @TableField("rule_version_id")
    private String ruleVersionId;

    @TableField("price_rule")
    private String price_rule;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("total_token")
    private BigDecimal totalToken;
    @TableField("amount_left")
    private BigDecimal amountLeft;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRuleVersionId() {
        return ruleVersionId;
    }

    public void setRuleVersionId(String ruleVersionId) {
        this.ruleVersionId = ruleVersionId;
    }

    public String getPrice_rule() {
        return price_rule;
    }

    public void setPrice_rule(String price_rule) {
        this.price_rule = price_rule;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalToken() {
        return totalToken;
    }

    public void setTotalToken(BigDecimal totalToken) {
        this.totalToken = totalToken;
    }

    public BigDecimal getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(BigDecimal amountLeft) {
        this.amountLeft = amountLeft;
    }
}

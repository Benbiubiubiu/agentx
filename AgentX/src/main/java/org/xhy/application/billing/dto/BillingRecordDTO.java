package org.xhy.application.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单记录数据传输对象，用于表示层和应用层之间传递账单记录数据
 */
public class BillingRecordDTO {
    /** 记录ID */
    private String id;
    
    /** 用户ID */
    private String userId;
    
    /** 产品ID */
    private String productId;
    
    /** 规则版本ID */
    private String ruleVersionId;
    
    /** 价格规则文本 */
    private String priceRule;
    
    /** 总金额 */
    private BigDecimal totalAmount;
    
    /** 剩余金额 */
    private BigDecimal amountLeft;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    private LocalDateTime updatedAt;

    // Getter和Setter方法
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

    public String getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(String priceRule) {
        this.priceRule = priceRule;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(BigDecimal amountLeft) {
        this.amountLeft = amountLeft;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 
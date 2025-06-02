package org.xhy.interfaces.dto.billing;

import java.math.BigDecimal;

/**
 * 创建账单记录请求
 */
public class CreateBillingRecordRequest {
    /** 用户ID */
    private String userId;
    
    /** 产品ID */
    private String productId;
    
    /** 规则版本ID */
    private String ruleVersionId;
    
    /** 总金额 */
    private BigDecimal totalAmount;
    
    /** 剩余金额 */
    private BigDecimal amountLeft;

    // Getter和Setter方法
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
} 
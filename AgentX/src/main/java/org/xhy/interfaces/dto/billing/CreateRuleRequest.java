package org.xhy.interfaces.dto.billing;

import org.xhy.domain.rule.model.config.BaseRule;
import org.xhy.domain.rule.model.config.BillingRule;

/**
 * 创建规则请求DTO
 */
public class CreateRuleRequest {
    /**
     * 产品ID
     */
    private String productId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则内容
     */
    private BaseRule rule;

    /**
     * 计费规则（用于前端传参）
     */
    private BillingRule billingRule;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public BillingRule getBillingRule() {
        return billingRule;
    }

    public void setBillingRule(BillingRule billingRule) {
        this.billingRule = billingRule;
        // 将 billingRule 转换为 rule
        if (billingRule != null) {
            this.rule = billingRule;
        }
    }
}

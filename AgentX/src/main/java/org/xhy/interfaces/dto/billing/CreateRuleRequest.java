package org.xhy.interfaces.dto.billing;

import org.xhy.domain.billing.model.config.BaseRule;

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
}

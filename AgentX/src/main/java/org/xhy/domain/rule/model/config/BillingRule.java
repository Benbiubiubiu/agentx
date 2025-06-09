package org.xhy.domain.rule.model.config;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 计费规则模型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingRule extends BaseRule {
    /**
     * 输入token单价（每1000个token）
     */
    private Double inputToken;

    /**
     * 输出token单价（每1000个token）
     */
    private Double outputToken;


    public Double getInputToken() {
        return inputToken;
    }

    public void setInputToken(Double inputToken) {
        this.inputToken = inputToken;
    }

    public Double getOutputToken() {
        return outputToken;
    }

    public void setOutputToken(Double outputToken) {
        this.outputToken = outputToken;
    }
} 
package org.xhy.domain.product.constant;

import org.xhy.infrastructure.exception.BusinessException;

/**
 * 产品类型枚举
 */
public enum ProductType {
    /** Chat */
    CHAT(0, "CHAT"),
    /** Agent */
    AGENT(1, "AGENT");

    private final Integer code;
    private final String description;
    ProductType (Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    public static ProductType fromCode(Integer code) {
        for (ProductType productType : values()) {
            if (productType.getCode().equals(code)) {
                return productType;
            }
        }
        throw new BusinessException("PRODUCT_TYPE_NOT_FOUND", "产品类型不存在");
    }
}

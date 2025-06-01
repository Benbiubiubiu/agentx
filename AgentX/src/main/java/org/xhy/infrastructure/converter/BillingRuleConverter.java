package org.xhy.infrastructure.converter;

import org.apache.ibatis.type.MappedTypes;
import org.xhy.domain.billing.model.BillingRule;

/** BillingRule JSON转换器 */
@MappedTypes(BillingRule.class)
public class BillingRuleConverter extends JsonToStringConverter<BillingRule> {

    public BillingRuleConverter() {
        super(BillingRule.class);
    }
} 
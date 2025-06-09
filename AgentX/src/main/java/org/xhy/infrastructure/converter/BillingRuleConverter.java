package org.xhy.infrastructure.converter;

import org.apache.ibatis.type.MappedTypes;
import org.xhy.domain.rule.model.config.BillingRule;


/**
 * agentx-ben
 * 2025/6/2 17:37
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
@MappedTypes(BillingRule.class)
public class BillingRuleConverter extends JsonToStringConverter<BillingRule>{
    public BillingRuleConverter() {
	    super(BillingRule.class);
    }
}

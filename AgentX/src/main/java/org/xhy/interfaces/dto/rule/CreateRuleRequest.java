package org.xhy.interfaces.dto.rule;

import org.xhy.domain.rule.constant.RuleType;
import org.xhy.domain.rule.model.config.BaseRule;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.infrastructure.exception.ParamValidationException;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * 创建规则请求DTO
 */
public class CreateRuleRequest {
    /**
     * 关联类型
     */
    private RuleType relatedType;

    /**
     * 关联ID
     */
    private String relatedId;

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

    /**
     * 生效时间
     */
    private LocalDateTime effectiveAt;

    /**
     * 过期时间
     */
    private LocalDateTime expiredAt;

    private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");

    public RuleType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(RuleType relatedType) {
        this.relatedType = relatedType;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
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

    public LocalDateTime getEffectiveAt() {
        return effectiveAt;
    }

    public void setEffectiveAt(LocalDateTime effectiveAt) {
        this.effectiveAt = effectiveAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    /**
     * 获取产品ID（兼容旧版本）
     */
    public String getProductId() {
        return RuleType.PRODUCT.equals(relatedType) ? relatedId : null;
    }

    /**
     * 设置产品ID（兼容旧版本）
     */
    public void setProductId(String productId) {
        this.relatedType = RuleType.PRODUCT;
        this.relatedId = productId;
    }

    /**
     * 比较版本号是否大于给定的版本号
     * @param lastVersion 上一个版本号
     * @return 如果当前版本号大于lastVersion则返回true，否则返回false
     */
    public boolean isVersionGreaterThan(String lastVersion) {
        if (lastVersion == null || lastVersion.trim().isEmpty()) {
            return true; // 如果没有上一个版本，当前版本肯定更大
        }

        // 确保两个版本号都符合格式
        if (!VERSION_PATTERN.matcher(version).matches() || !VERSION_PATTERN.matcher(lastVersion).matches()) {
            throw new ParamValidationException("version", "版本号必须遵循 x.y.z 格式");
        }

        // 分割版本号
        String[] current = version.split("\\.");
        String[] last = lastVersion.split("\\.");

        // 比较主版本号
        int currentMajor = Integer.parseInt(current[0]);
        int lastMajor = Integer.parseInt(last[0]);
        if (currentMajor > lastMajor)
            return true;
        if (currentMajor < lastMajor)
            return false;

        // 主版本号相同，比较次版本号
        int currentMinor = Integer.parseInt(current[1]);
        int lastMinor = Integer.parseInt(last[1]);
        if (currentMinor > lastMinor)
            return true;
        if (currentMinor < lastMinor)
            return false;

        // 主版本号和次版本号都相同，比较修订版本号
        int currentPatch = Integer.parseInt(current[2]);
        int lastPatch = Integer.parseInt(last[2]);

        return currentPatch > lastPatch;
    }

    /**
     * 验证版本号格式
     */
    public void validateVersion() {
        if (version == null || version.trim().isEmpty()) {
            throw new ParamValidationException("version", "版本号不能为空");
        }
        if (!VERSION_PATTERN.matcher(version).matches()) {
            throw new ParamValidationException("version", "版本号必须遵循 x.y.z 格式");
        }
    }
}

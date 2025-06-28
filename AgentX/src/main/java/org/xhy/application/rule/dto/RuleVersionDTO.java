package org.xhy.application.rule.dto;

import org.xhy.domain.rule.model.config.BaseRule;

import java.time.LocalDateTime;

/**
 * agentx-ben
 * 2025/6/28 15:03
 *
 * @author Ben，微信：wz_Fung_Ben，邮箱：842609063@qq.con <br/>
 **/
public class RuleVersionDTO {
    /** 规则ID */
    private String id;

    /** 版本号 */
    private String version;

    /** 规则描述 */
    private String description;

    /** 规则内容 */
    private BaseRule rule;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 版本号 */
    private String Version;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

package org.xhy.application.rule.dto;

import java.util.List;

/**
 * 规则聚合根DTO
 * 包含规则信息和所有版本信息
 */
public class RuleAggregateDTO {
    
    /**
     * 规则信息
     */
    private RuleDTO rule;
    
    /**
     * 规则版本列表
     */
    private List<RuleVersionDTO> versions;
    
    public RuleAggregateDTO() {
    }
    
    public RuleAggregateDTO(RuleDTO rule, List<RuleVersionDTO> versions) {
        this.rule = rule;
        this.versions = versions;
    }
    
    public RuleDTO getRule() {
        return rule;
    }
    
    public void setRule(RuleDTO rule) {
        this.rule = rule;
    }
    
    public List<RuleVersionDTO> getVersions() {
        return versions;
    }
    
    public void setVersions(List<RuleVersionDTO> versions) {
        this.versions = versions;
    }
} 
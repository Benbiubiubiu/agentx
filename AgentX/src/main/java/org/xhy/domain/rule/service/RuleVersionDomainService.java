package org.xhy.domain.rule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 规则版本领域服务类
 **/
@Service
public class RuleVersionDomainService {
    private final RuleVersionRepository ruleVersionRepository;


    public RuleVersionDomainService(RuleVersionRepository ruleVersionRepository) {
        this.ruleVersionRepository = ruleVersionRepository;
    }
    /**
     * 创建规则版本
     * @param versionEntity 规则版本实体
     * @return 创建的规则版本实体
     */
    public RuleVersionEntity createRuleVersion(RuleVersionEntity versionEntity) {
        ruleVersionRepository.insert(versionEntity);
        return versionEntity;
    }
    /**
     * 获取规则版本
     * @param id 版本ID
     * @return 规则版本实体
     */
    public RuleVersionEntity getRuleVersion(String id) {
        return ruleVersionRepository.selectById(id);
    }

    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionEntity> getRuleVersions(String ruleId) {
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId)
                .orderByDesc(RuleVersionEntity::getCreatedAt);
        return ruleVersionRepository.selectList(wrapper);
    }

    /**
     * 获取当前有效的规则版本
     * @param ruleId 规则ID
     * @return 当前有效的规则版本
     */
    public RuleVersionEntity getCurrentRuleVersion(String ruleId) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId)
                .le(RuleVersionEntity::getEffectiveAt, now)
                .ge(RuleVersionEntity::getExpiredAt, now)
                .orderByDesc(RuleVersionEntity::getCreatedAt)
                .last("LIMIT 1");
        return ruleVersionRepository.selectOne(wrapper);
    }

    public void deleteRuleVersions(String id) {
        // 删除相关的规则版本
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, id);
        ruleVersionRepository.delete(wrapper);
    }
}

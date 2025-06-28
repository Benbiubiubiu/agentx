package org.xhy.domain.rule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.repository.RuleVersionRepository;
import org.xhy.infrastructure.exception.BusinessException;
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
        ruleVersionRepository.checkInsert(versionEntity);
        return versionEntity;
    }
    /**
     * 获取规则版本
     * @param id 版本ID
     * @return 规则版本实体
     * @throws BusinessException 规则版本不存在时抛出异常
     */
    public RuleVersionEntity getRuleVersion(String id) {
        RuleVersionEntity version = ruleVersionRepository.selectById(id);
        if (version == null) {
            throw new BusinessException("规则版本不存在: " + id);
        }
        return version;
    }

    /**
     * 删除规则版本
     * @param id 规则版本ID
     * @throws BusinessException 规则版本不存在时抛出异常
     */
    public void deleteRuleVersion(String id) {
        // 检查规则版本是否存在
        RuleVersionEntity existingVersion = ruleVersionRepository.selectById(id);
        if (existingVersion == null) {
            throw new BusinessException("规则版本不存在: " + id);
        }
        
        // 删除规则版本
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getId, id);
        ruleVersionRepository.checkedDelete(wrapper);
    }

    /**
     * 删除规则的所有版本
     * @param ruleId
     */
    public void deleteRuleVersionByRuleId(String ruleId) {
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId);
        ruleVersionRepository.checkedDelete(wrapper);
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
     * @throws BusinessException 当前没有有效的规则版本时抛出异常
     */
    public RuleVersionEntity getLatestRuleVersion(String ruleId) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<RuleVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RuleVersionEntity::getRuleId, ruleId)
               .orderByDesc(RuleVersionEntity::getCreatedAt)
               .last("LIMIT 1");
        RuleVersionEntity version = ruleVersionRepository.selectOne(wrapper);
        if (version == null) {
            throw new BusinessException("当前没有有效的规则版本: " + ruleId);
        }
        return version;
    }
}

package org.xhy.domain.rule.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

/**
 * 规则版本仓储接口
 */
@Mapper
public interface RuleVersionRepository extends MyBatisPlusExtRepository<RuleVersionEntity> {
} 
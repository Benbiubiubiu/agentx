package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.entity.RuleVersionEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

/**
 * 规则版本仓储接口
 */
@Mapper
public interface RuleVersionRepository extends MyBatisPlusExtRepository<RuleVersionEntity> {
} 
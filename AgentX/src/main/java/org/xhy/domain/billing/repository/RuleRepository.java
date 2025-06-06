package org.xhy.domain.billing.repository;

import org.apache.ibatis.annotations.Mapper;
import org.xhy.domain.billing.model.dto.RuleEntity;
import org.xhy.infrastructure.repository.MyBatisPlusExtRepository;

/**
 * 规则仓储接口
 */
@Mapper
public interface RuleRepository extends MyBatisPlusExtRepository<RuleEntity> {
} 
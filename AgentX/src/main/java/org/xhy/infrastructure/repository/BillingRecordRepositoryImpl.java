package org.xhy.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xhy.domain.billing.entity.BillingUsageRecordEntity;
import org.xhy.domain.billing.repository.BillingRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BillingRecordRepositoryImpl implements BillingRecordRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<BillingUsageRecordEntity> queryRecords(String userId,
                                                     int pageNum, int pageSize) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BillingUsageRecordEntity> query = cb.createQuery(BillingUsageRecordEntity.class);
        Root<BillingUsageRecordEntity> root = query.from(BillingUsageRecordEntity.class);

        // 构建查询条件
        List<Predicate> predicates = new ArrayList<>();
        
        if (userId != null && !userId.isEmpty()) {
            predicates.add(cb.equal(root.get("userId"), userId));
        }


        // 添加查询条件
        query.where(predicates.toArray(new Predicate[0]));
        
        // 按创建时间倒序排序
        query.orderBy(cb.desc(root.get("createTime")));

        // 创建查询
        TypedQuery<BillingUsageRecordEntity> typedQuery = entityManager.createQuery(query);
        
        // 设置分页
        typedQuery.setFirstResult((pageNum - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    @Override
    public long countRecords(String userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<BillingUsageRecordEntity> root = countQuery.from(BillingUsageRecordEntity.class);

        // 构建查询条件
        List<Predicate> predicates = new ArrayList<>();
        
        if (userId != null && !userId.isEmpty()) {
            predicates.add(cb.equal(root.get("userId"), userId));
        }
        countQuery.select(cb.count(root));
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
} 
package org.xhy.application.rule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.rule.assembler.RuleAssembler;
import org.xhy.application.billing.assembler.RuleVersionAssembler;
import org.xhy.application.rule.dto.RuleDTO;
import org.xhy.application.rule.dto.RuleVersionDTO;
import org.xhy.application.rule.dto.RuleAggregateDTO;
import org.xhy.domain.product.constant.ProductType;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.rule.model.config.BaseRule;
import org.xhy.domain.rule.model.config.BillingRule;
import org.xhy.domain.rule.model.dto.RuleEntity;
import org.xhy.domain.rule.model.dto.RuleVersionEntity;
import org.xhy.domain.rule.service.RuleDomainService;
import org.xhy.domain.rule.service.RuleVersionDomainService;
import org.xhy.interfaces.dto.billing.CreateRuleRequest;
import org.xhy.domain.product.service.ProductDomainService;
import org.xhy.infrastructure.exception.BusinessException;
import org.xhy.infrastructure.exception.ParamValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 规则应用服务
 * 用于适配领域层的规则服务
 * 职责：
 * 1. 接收和验证来自接口层的请求
 * 2. 将请求转换为领域对象或参数
 * 3. 调用领域服务执行业务逻辑
 * 4. 转换和返回结果给接口层
 */
@Service
public class RuleAppService {

    private static final Logger logger = LoggerFactory.getLogger(RuleAppService.class);

    private final RuleDomainService ruleDomainService;
    private final ProductDomainService productDomainService;
    private final RuleVersionDomainService ruleVersionDomainService;

    public RuleAppService(RuleDomainService ruleDomainService,
                          ProductDomainService productDomainService, RuleVersionDomainService ruleVersionDomainService) {
        this.ruleDomainService = ruleDomainService;
        this.productDomainService = productDomainService;
        this.ruleVersionDomainService = ruleVersionDomainService;
    }

    /**
     * 创建规则并关联到实体
     * @param request 创建规则请求
     * @return 创建的规则实体
     */
    @Transactional
    public RuleEntity createRule(CreateRuleRequest request) {
        // 1. 验证规则类型
        validateRuleType(request.getRule());

        // 2. 验证版本号格式
        request.validateVersion();

        // 3. 创建新规则
        RuleEntity ruleEntity = RuleAssembler.toEntity(request);
        ruleEntity = ruleDomainService.createRule(ruleEntity);

        // 4. 处理关联关系
        handleRelatedEntity(request, ruleEntity);

        // 5. 创建规则版本
        RuleVersionEntity versionEntity = RuleVersionAssembler.toEntity(
            ruleEntity.getId(), 
            request, 
            request.getEffectiveAt() != null ? request.getEffectiveAt() : LocalDateTime.now(),
            request.getExpiredAt()
        );
        ruleVersionDomainService.createRuleVersion(versionEntity);

        return ruleEntity;
    }

    /**
     * 验证规则类型
     */
    private void validateRuleType(BaseRule rule) {
        if (!(rule instanceof BillingRule)) {
            throw new IllegalArgumentException("不支持的规则类型");
        }
    }

    /**
     * 验证版本号（用于更新规则时）
     */
    private void validateVersionNumber(String ruleId, CreateRuleRequest request) {
        try {
            // 获取当前规则的最新版本
            RuleVersionEntity latestVersion = ruleVersionDomainService.getLatestRuleVersion(ruleId);
            
            // 检查版本号是否大于当前最新版本
            if (!request.isVersionGreaterThan(latestVersion.getVersion())) {
                throw new BusinessException("version",
                        "新版本号(" + request.getVersion() + ")必须大于当前最新版本号(" + latestVersion.getVersion() + ")");
            }
        } catch (BusinessException e) {
            // 如果是版本号比较失败，直接抛出异常
            if (e.getMessage().contains("新版本号") && e.getMessage().contains("必须大于")) {
                throw e;
            }
            // 如果是"当前没有有效的规则版本"异常，说明是第一次创建，跳过版本号比较
            if (e.getMessage().contains("当前没有有效的规则版本")) {
                logger.info("未找到现有规则版本，跳过版本号比较: {}", e.getMessage());
                return;
            }
            // 其他业务异常直接抛出
            throw e;
        }
    }

    /**
     * 处理关联实体
     */
    private void handleRelatedEntity(CreateRuleRequest request, RuleEntity ruleEntity) {
        if (!isValidRelatedInfo(request)) {
            return;
        }

        Optional.ofNullable(request.getRelatedType())
                .ifPresent(type -> {
                    switch (type) {
                        case PRODUCT:
                            logger.info("处理产品计费关联,计费规则为：{}", request.getRule() != null ? request.getRule().toString() : "null");
                            handleProductRelation(request, ruleEntity);
                            break;
                        // 可以在这里添加其他类型的处理
                        default:
                            // 暂时不处理其他类型
                            break;
                    }
                });
    }

    /**
     * 处理产品关联
     */
    private void handleProductRelation(CreateRuleRequest request, RuleEntity ruleEntity) {
        ProductEntity product = productDomainService.getProduct(request.getRelatedId());
        if (product != null) {
            validateProductType(product);
            productDomainService.updateProductRuleId(request.getRelatedId(), ruleEntity.getId());
        }
    }

    /**
     * 验证产品类型
     */
    private void validateProductType(ProductEntity product) {
        if (!ProductType.AGENT.name().equals(product.getProductType()) &&
            !ProductType.CHAT.name().equals(product.getProductType())) {
            throw new IllegalArgumentException("该产品类型不支持计费规则");
        }
    }

    /**
     * 验证关联信息是否有效
     */
    private boolean isValidRelatedInfo(CreateRuleRequest request) {
        return request.getRelatedType() != null 
            && request.getRelatedId() != null 
            && !request.getRelatedId().isEmpty();
    }

    /**
     * 获取规则信息
     * @param ruleId 规则ID
     * @return 规则信息
     */
    public RuleDTO getRule(String ruleId) {
        RuleEntity rule = ruleDomainService.getRule(ruleId);
        return RuleAssembler.toDTO(rule);
    }

    /**
     * 更新规则
     * @param ruleId 规则ID
     * @param request 更新规则请求
     */
    @Transactional
    public void updateRule(String ruleId, CreateRuleRequest request) {
        // 1. 验证版本号格式
        request.validateVersion();
        
        // 2. 检查版本号是否大于当前最新版本
        validateVersionNumber(ruleId, request);
        
        // 3. 将请求转换为领域实体
        RuleEntity ruleEntity = RuleAssembler.toEntity(request);
        ruleEntity.setId(ruleId);
        
        // 4. 调用领域服务更新规则
        ruleDomainService.updateRule(ruleEntity);
        
        // 5. 创建新的规则版本
        RuleVersionEntity ruleVersion = new RuleVersionEntity();
        BeanUtils.copyProperties(request, ruleVersion);
        ruleVersion.setRuleId(ruleId);
        ruleVersionDomainService.createRuleVersion(ruleVersion);
    }

    /**
     * 删除规则，与产品关联的规则
     * @param ruleId 规则ID
     * @param productId 产品ID
     */
    @Transactional
    public void deleteRule(String ruleId, String productId) {
        // 删除规则
        ruleDomainService.deleteRule(ruleId);
        // 删除规则版本
        ruleVersionDomainService.deleteRuleVersionByRuleId(ruleId);

        // 清除产品表中的rule_id字段
        if (productId != null && !productId.isEmpty()) {
            productDomainService.updateProductRuleId(productId, null);
        }
    }

    /**
     * 删除规则
     * @param ruleId
     */
    @Transactional
    public void deleteRule(String ruleId){
        // 删除规则
        ruleDomainService.deleteRule(ruleId);
        // 删除规则版本
        ruleVersionDomainService.deleteRuleVersionByRuleId(ruleId);

    }


    /**
     * 获取规则的所有版本
     * @param ruleId 规则ID
     * @return 规则版本列表
     */
    public List<RuleVersionDTO> getRuleVersions(String ruleId) {
        // 先检查规则是否存在
        ruleDomainService.getRule(ruleId);
        
        // 获取规则的所有版本
        List<RuleVersionEntity> versions = ruleVersionDomainService.getRuleVersions(ruleId);
        
        // 转换为DTO
        return RuleAssembler.toRuleVersionDTOs(versions);
    }

    /**
     * 删除规则版本
     * @param ruleVersionId 规则版本ID
     */
    @Transactional
    public void deleteRuleVersion(String ruleVersionId) {
        // 先检查规则版本是否存在
        ruleVersionDomainService.getRuleVersion(ruleVersionId);
        
        // 删除规则版本
        ruleVersionDomainService.deleteRuleVersion(ruleVersionId);
    }

    /**
     * 获取规则聚合根信息（包含规则和所有版本）
     * @param ruleId 规则ID
     * @return 规则聚合根信息
     */
    public RuleAggregateDTO getRuleAggregate(String ruleId) {
        // 获取规则信息
        RuleEntity ruleEntity = ruleDomainService.getRule(ruleId);
        
        // 获取规则的所有版本
        List<RuleVersionEntity> versionEntities = ruleVersionDomainService.getRuleVersions(ruleId);
        
        // 转换为聚合根DTO
        return RuleAssembler.toAggregateDTO(ruleEntity, versionEntities);
    }

} 
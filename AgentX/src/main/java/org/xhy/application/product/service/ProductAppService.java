package org.xhy.application.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhy.application.product.assembler.ProductAssembler;
import org.xhy.application.product.dto.ProductListDTO;
import org.xhy.domain.product.model.dto.ProductEntity;
import org.xhy.domain.product.service.ProductDomainService;
import org.xhy.domain.rule.service.RuleDomainService;
import org.xhy.domain.rule.service.RuleVersionDomainService;
import org.xhy.interfaces.dto.product.CreateProductRequest;

import java.util.stream.Collectors;

/**
 * 产品应用服务
 */
@Service
public class ProductAppService {
    private final ProductDomainService productDomainService;
    private final RuleDomainService ruleDomainService;
    private final RuleVersionDomainService ruleVersionDomainService;

    public ProductAppService(ProductDomainService productService, RuleDomainService ruleDomainService, RuleVersionDomainService ruleVersionDomainService) {
        this.productDomainService = productService;
        this.ruleDomainService = ruleDomainService;
        this.ruleVersionDomainService = ruleVersionDomainService;
    }

    /**
     * 创建产品
     * @param request 创建产品请求
     */
    @Transactional
    public void createProduct(CreateProductRequest request,String userId) {
        ProductEntity productEntity = ProductAssembler.toEntity(request);
        productEntity.setUserId(userId);
        productDomainService.createProduct(productEntity);
    }

    /**
     * 查询产品列表
     * @param page 分页参数
     * @return 产品列表
     */
    public Page<ProductListDTO> queryProducts(Page<ProductEntity> page) {
        // 调用领域服务查询产品列表
        Page<ProductEntity> productPage = productDomainService.queryProducts(page);
        
        // 转换为DTO
        Page<ProductListDTO> dtoPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        dtoPage.setRecords(productPage.getRecords().stream()
                .map(ProductAssembler::toDTO)
                .collect(Collectors.toList()));
        
        return dtoPage;
    }

    /**
     * 更新产品
     * @param id 产品ID
     * @param request 更新产品请求
     */
    @Transactional
    public void updateProduct(String id, CreateProductRequest request) {
        ProductEntity entity = ProductAssembler.toEntity(request);
        entity.setId(id);
        productDomainService.updateProduct(entity);
    }

    /**
     * 根据ID查询产品
     * @param id 产品ID
     * @return 产品信息
     */
    public ProductListDTO getProductById(String id) {
        ProductEntity product = productDomainService.getProduct(id);
        return ProductAssembler.toDTO(product);
    }

    /**
     * 删除产品
     * @param id 产品ID
     */
    @Transactional
    public void deleteProduct(String id) {
        // 查询产品信息
        ProductEntity product = productDomainService.getProduct(id);
        
        // 如果产品有关联的规则，先删除规则相关数据
        if (product.getRuleId() != null && !product.getRuleId().isEmpty()) {
            // 先删除规则版本
            ruleVersionDomainService.deleteRuleVersionByRuleId(product.getRuleId());
            // 再删除规则
            ruleDomainService.deleteRule(product.getRuleId());
        }
        
        // 删除产品
        productDomainService.deleteProduct(id);
    }
}
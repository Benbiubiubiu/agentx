package org.xhy.application.billing.assembler;

import org.xhy.application.billing.dto.BalanceDTO;
import org.xhy.domain.billing.entity.UserBillingCountEntity;

/**
 * 余额领域对象组装器
 * 负责DTO和Entity之间的转换
 */
public class BalanceAssembler {

    /**
     * 将UserBillingCountEntity转换为BalanceDTO
     */
    public static BalanceDTO toDTO(UserBillingCountEntity entity) {
        if (entity == null) {
            return null;
        }

        BalanceDTO dto = new BalanceDTO();
        dto.setBalance(entity.getBalance());
        dto.setCumulativeRechargeAmount(entity.getCumulativeRechargeAmount());
        dto.setLastTransactionAt(entity.getLastTransactionAt());

        return dto;
    }
} 
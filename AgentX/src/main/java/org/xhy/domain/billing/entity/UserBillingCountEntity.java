package org.xhy.domain.billing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.xhy.infrastructure.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户账单统计实体类
 */
@TableName("user_billing_count")
public class UserBillingCountEntity extends BaseEntity {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    @TableField("user_id")
    private String userId;

    @TableField("balance")
    private BigDecimal balance;

    @TableField("cumulative_recharge_amount")
    private BigDecimal cumulativeRechargeAmount;

    @TableField("last_transaction_at")
    private LocalDateTime lastTransactionAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCumulativeRechargeAmount() {
        return cumulativeRechargeAmount;
    }

    public void setCumulativeRechargeAmount(BigDecimal cumulativeRechargeAmount) {
        this.cumulativeRechargeAmount = cumulativeRechargeAmount;
    }

    public LocalDateTime getLastTransactionAt() {
        return lastTransactionAt;
    }

    public void setLastTransactionAt(LocalDateTime lastTransactionAt) {
        this.lastTransactionAt = lastTransactionAt;
    }
}

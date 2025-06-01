package org.xhy.application.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 余额查询数据传输对象
 * 用于表示层和应用层之间传递余额数据
 */
public class BalanceDTO {
    /** 当前余额 */
    private BigDecimal balance;
    
    /** 累计充值金额 */
    private BigDecimal cumulativeRechargeAmount;
    
    /** 最后交易时间 */
    private LocalDateTime lastTransactionAt;

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
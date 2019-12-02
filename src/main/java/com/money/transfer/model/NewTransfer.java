package com.money.transfer.model;

import java.math.BigDecimal;

/**
 * @author Mayank
 */
public class NewTransfer {

    private BigDecimal amount;
    private long fromAccount;
    private long toAccount;

    public NewTransfer(BigDecimal amount, long fromAccount, long toAccount) {
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public long getToAccount() {
        return toAccount;
    }

    public void setToAccount(long toAccount) {
        this.toAccount = toAccount;
    }
}

package com.revolut.money.transfer.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mayank
 */
public class Transfer {

    private long id;
    private Date timestamp;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;

    public Transfer(long id, Date timestamp, Account fromAccount, Account toAccount, BigDecimal amount) {
        this.id = id;
        this.timestamp = timestamp;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package com.money.transfer.model;

import java.math.BigDecimal;

/**
 * @author Mayank
 */
public class Account {

    private long id;
    private String number;
    private BigDecimal balance;

    public Account(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Account(long id, String number, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

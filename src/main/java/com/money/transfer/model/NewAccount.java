package com.money.transfer.model;

import java.math.BigDecimal;

/**
 * @author Mayank
 */
public class NewAccount {

    private String number;
    private BigDecimal balance;

    public NewAccount(String number, BigDecimal balance) {
        this.number = number;
        this.balance = balance;
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

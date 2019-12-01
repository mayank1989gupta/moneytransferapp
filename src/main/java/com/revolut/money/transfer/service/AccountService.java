package com.revolut.money.transfer.service;

import com.revolut.money.transfer.database.InMemoryDatabase;
import com.revolut.money.transfer.model.Account;
import com.revolut.money.transfer.model.NewAccount;
import com.revolut.money.transfer.model.Transfer;
import org.jooq.impl.DSL;

import java.util.List;

import db.tables.records.AccountRecord;
import static db.tables.Account.ACCOUNT;
import static db.tables.Transfer.TRANSFER;

/**
 * @author Mayank
 *
 * Account Service class to serve the API's with required operations<br>
 */
public class AccountService {

    private final InMemoryDatabase db;

    public AccountService(InMemoryDatabase db) {
        this.db = db;
    }

    /**
     * Method to create a new Account.<br>
     * @param acc
     * @return
     */
    public Account create(NewAccount acc) {
        return db.context().transactionResult(configuration -> {
            AccountRecord accRec = DSL.using(configuration).insertInto(ACCOUNT, ACCOUNT.NUMBER, ACCOUNT.BALANCE)
                    .values(acc.getNumber(), acc.getBalance())
                    .returning(ACCOUNT.ID)
                    .fetchOne();

            return DSL.using(configuration).selectFrom(ACCOUNT)
                    .where(ACCOUNT.ID.eq(accRec.getId()))
                    .fetchSingle().into(Account.class);
        });
    }

    /**
     * Method to create all accounts from DB.<br>
     *
     * @return
     */
    public List<Account> fetchAllAccounts() {
        return db.context().selectFrom(ACCOUNT).fetchInto(Account.class);
    }

    /**
     * Method to fetch the account for the given ID.<br>
     *
     * @param accId
     * @return
     */
    public Account fetch(long accId) {
        return db.context().selectFrom(ACCOUNT).where(ACCOUNT.ID.eq(accId)).fetchSingleInto(Account.class);
    }

    /**
     * Method to fetch al transfers for a given ID.<br>
     *
     * @param accId
     * @return
     */
    public List<Transfer> fetchTransfers(long accId) {
        db.tables.Account fromAcc = ACCOUNT.as("fromAccount");
        db.tables.Account toAcc = ACCOUNT.as("toAccount");

        Account acc = fetch(accId);

        return db.context().selectFrom(TRANSFER.join(fromAcc).onKey(TRANSFER.FROM_ACCOUNT).join(toAcc).onKey(TRANSFER.TO_ACCOUNT))
                .where(fromAcc.ID.eq(accId).or(toAcc.ID.eq(acc.getId())))
                .orderBy(TRANSFER.DATE.desc())
                .fetch(record -> {
                    Account fromAccount = new Account(record.get(fromAcc.ID), record.get(fromAcc.NUMBER));
                    Account toAccount = new Account(record.get(toAcc.ID), record.get(toAcc.NUMBER));
                    Transfer transfer = new Transfer(
                            record.get(TRANSFER.ID),
                            record.get(TRANSFER.DATE),
                            fromAccount,
                            toAccount,
                            record.get(TRANSFER.AMOUNT));
                    return transfer;
                });
    }
}

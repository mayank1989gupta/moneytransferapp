package com.revolut.money.transfer.router;

import com.revolut.money.transfer.database.InMemoryDatabase;
import com.revolut.money.transfer.model.NewTransfer;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import db.tables.records.AccountRecord;
import static db.tables.Account.ACCOUNT;
import static db.tables.Transfer.TRANSFER;

/**
 * @author Mayank
 *
 * Test class to test concurrent transactions<br>
 */
public class ConcurrencyTest {


    private InMemoryDatabase database;

    @BeforeEach
    void setUp() throws Exception {
        database = new InMemoryDatabase();
    }

    @AfterEach
    void tearDown() {
        database.context().execute("SHUTDOWN");
    }

    @Test
    void testConcurrentTransfers_FinalBalancesAsForSerialTransfers() throws Exception {
        DSLContext context = database.context();
        BigDecimal amount1 = context.select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(1001L)).fetchOne(ACCOUNT.BALANCE);
        BigDecimal amount2 = context.select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(1002L)).fetchOne(ACCOUNT.BALANCE);
        assertEquals(BigDecimal.valueOf(100000, 2), amount1);
        assertEquals(BigDecimal.valueOf(200000, 2), amount2);

        Thread t1 = new Thread(() -> {
            NewTransfer tr = new NewTransfer(BigDecimal.valueOf(1000), 1001, 1002);
            transferAmount("t1", tr);
        });

        Thread t2 = new Thread(() -> {
            NewTransfer tr = new NewTransfer(BigDecimal.valueOf(2000), 1002, 1001);
            transferAmount("t2", tr);
        });

        t1.start(); //starting t1
        try {
            Thread.sleep(100);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        t2.start(); //t2

        t1.join();
        t2.join();

        amount1 = database.context().select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(1001L)).fetchOne(ACCOUNT.BALANCE);
        amount2 = database.context().select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(1002L)).fetchOne(ACCOUNT.BALANCE);
        assertEquals(BigDecimal.valueOf(200000, 2), amount1);
        assertEquals(BigDecimal.valueOf(100000, 2), amount2);
    }

    //Private method for transfer amount between accounts
    private void transferAmount(String thread, NewTransfer transfer) {
        System.out.println(thread + " " + System.currentTimeMillis() + " : transaction started");

        database.context().transaction(configuration -> {
            System.out.println(thread + " " + System.currentTimeMillis() + ": transfer " + transfer.getAmount() + " " +
                    "from = " + DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getFromAccount())).fetchOne(ACCOUNT.BALANCE) + " " +
                    "to = " + DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getToAccount())).fetchOne(ACCOUNT.BALANCE));

            DSL.using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.minus(transfer.getAmount()))
                    .where(ACCOUNT.ID.eq(transfer.getFromAccount()))
                    .execute();
            System.out.println(thread + " " + System.currentTimeMillis() + " : after updated from = " +
                    DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getFromAccount())).fetchOne(ACCOUNT.BALANCE));

            Thread.sleep(2000);

            DSL.using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.plus(transfer.getAmount()))
                    .where(ACCOUNT.ID.eq(transfer.getToAccount()))
                    .execute();
            System.out.println(thread + " " + System.currentTimeMillis() + " : after updated to = " +
                    DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getToAccount())).fetchOne(ACCOUNT.BALANCE));


            System.out.println(thread + " " + System.currentTimeMillis() + ": final balances " +
                    "from = " + DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getFromAccount())).fetchOne(ACCOUNT.BALANCE) + " " +
                    "to = " + DSL.using(configuration).select(ACCOUNT.BALANCE).from(ACCOUNT).where(ACCOUNT.ID.eq(transfer.getToAccount())).fetchOne(ACCOUNT.BALANCE));
        });

        System.out.println(thread + " " + System.currentTimeMillis() + " : transaction finished");
    }
}

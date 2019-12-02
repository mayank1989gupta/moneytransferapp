package com.money.transfer.service;

import com.money.transfer.database.InMemoryDatabase;
import com.money.transfer.model.NewTransfer;
import com.money.transfer.model.Transfer;
import db.tables.Account;
import db.tables.records.TransferRecord;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;

import java.util.List;

import static db.tables.Account.ACCOUNT;
import static db.tables.Transfer.TRANSFER;

/**
 * @author Mayank
 *
 * Transfer Service class to serve the API's with required operations<br>
 */
public class TransferService {

    private final InMemoryDatabase db;
    private final Account fromAccount = ACCOUNT.as("fromAccount");
    private final Account toAccount = ACCOUNT.as("toAccount");

    public TransferService(InMemoryDatabase db) {
        this.db = db;
    }

    /**
     * Method to record a new transfer.<br>
     *
     * @param trReq
     * @return
     */
    public Transfer transfer(NewTransfer trReq) {
        return db.context().transactionResult(configuration -> {
            DSL.using(configuration)
                    .selectFrom(ACCOUNT).where(ACCOUNT.ID.eq(trReq.getFromAccount()).or(ACCOUNT.ID.eq(trReq.getToAccount())))
                    .forUpdate().fetchInto(com.money.transfer.model.Account.class);

            DSL.using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.minus(trReq.getAmount()))
                    .where(ACCOUNT.ID.eq(trReq.getFromAccount()))
                    .execute();

            DSL.using(configuration)
                    .update(ACCOUNT)
                    .set(ACCOUNT.BALANCE, ACCOUNT.BALANCE.plus(trReq.getAmount()))
                    .where(ACCOUNT.ID.eq(trReq.getToAccount()))
                    .execute();

            TransferRecord trRec = DSL.using(configuration)
                    .insertInto(TRANSFER, TRANSFER.FROM_ACCOUNT, TRANSFER.TO_ACCOUNT, TRANSFER.AMOUNT)
                    .values(trReq.getFromAccount(), trReq.getToAccount(), trReq.getAmount())
                    .returning(TRANSFER.ID).fetchOne();

            return DSL.using(configuration).selectFrom(TRANSFER.join(fromAccount).onKey(TRANSFER.FROM_ACCOUNT).join(toAccount).onKey(TRANSFER.TO_ACCOUNT))
                    .where(TRANSFER.ID.eq(trRec.getId()))
                    .fetchSingle(new TransferRecordMapper());
        });
    }

    /**
     * Method to fetch all transfers from DB.<br>
     * @return
     */
    public List<Transfer> fetchAllTransfers() {
        return db.context().selectFrom(TRANSFER
                .join(fromAccount).onKey(TRANSFER.FROM_ACCOUNT)
                .join(toAccount).onKey(TRANSFER.TO_ACCOUNT))
                .orderBy(TRANSFER.DATE.desc())
                .fetch(new TransferRecordMapper());
    }

    /**
     * Method to fetch a transfer for the given ID.<br>
     *
     * @param transferId
     * @return
     */
    public Transfer fetch(long transferId) {
        return db.context().selectFrom(TRANSFER
                .join(fromAccount).onKey(TRANSFER.FROM_ACCOUNT)
                .join(toAccount).onKey(TRANSFER.TO_ACCOUNT))
                .where(TRANSFER.ID.eq(transferId))
                .fetchSingle(new TransferRecordMapper());
    }



    private class TransferRecordMapper implements RecordMapper<Record, Transfer> {
        @Override
        public Transfer map(Record record) {
            com.money.transfer.model.Account fromAcc =
                    new com.money.transfer.model.Account(record.get(fromAccount.ID), record.get(fromAccount.NUMBER));
            com.money.transfer.model.Account toAcc =
                    new com.money.transfer.model.Account(record.get(toAccount.ID), record.get(toAccount.NUMBER));
            Transfer transfer = new Transfer(
                    record.get(TRANSFER.ID),
                    record.get(TRANSFER.DATE),
                    fromAcc,
                    toAcc,
                    record.get(TRANSFER.AMOUNT));

            return transfer;
        }
    }
}

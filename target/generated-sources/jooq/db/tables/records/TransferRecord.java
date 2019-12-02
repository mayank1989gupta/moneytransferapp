/*
 * This file is generated by jOOQ.
 */
package db.tables.records;


import db.tables.Transfer;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransferRecord extends UpdatableRecordImpl<TransferRecord> implements Record5<Long, BigDecimal, Long, Long, Timestamp> {

    private static final long serialVersionUID = 264306593;

    /**
     * Setter for <code>PUBLIC.TRANSFER.ID</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.ID</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.AMOUNT</code>.
     */
    public void setAmount(BigDecimal value) {
        set(1, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.AMOUNT</code>.
     */
    public BigDecimal getAmount() {
        return (BigDecimal) get(1);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.FROM_ACCOUNT</code>.
     */
    public void setFromAccount(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.FROM_ACCOUNT</code>.
     */
    public Long getFromAccount() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.TO_ACCOUNT</code>.
     */
    public void setToAccount(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.TO_ACCOUNT</code>.
     */
    public Long getToAccount() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>PUBLIC.TRANSFER.DATE</code>.
     */
    public void setDate(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>PUBLIC.TRANSFER.DATE</code>.
     */
    public Timestamp getDate() {
        return (Timestamp) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, BigDecimal, Long, Long, Timestamp> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, BigDecimal, Long, Long, Timestamp> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Transfer.TRANSFER.ID;
    }

    @Override
    public Field<BigDecimal> field2() {
        return Transfer.TRANSFER.AMOUNT;
    }

    @Override
    public Field<Long> field3() {
        return Transfer.TRANSFER.FROM_ACCOUNT;
    }

    @Override
    public Field<Long> field4() {
        return Transfer.TRANSFER.TO_ACCOUNT;
    }

    @Override
    public Field<Timestamp> field5() {
        return Transfer.TRANSFER.DATE;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public BigDecimal component2() {
        return getAmount();
    }

    @Override
    public Long component3() {
        return getFromAccount();
    }

    @Override
    public Long component4() {
        return getToAccount();
    }

    @Override
    public Timestamp component5() {
        return getDate();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public BigDecimal value2() {
        return getAmount();
    }

    @Override
    public Long value3() {
        return getFromAccount();
    }

    @Override
    public Long value4() {
        return getToAccount();
    }

    @Override
    public Timestamp value5() {
        return getDate();
    }

    @Override
    public TransferRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public TransferRecord value2(BigDecimal value) {
        setAmount(value);
        return this;
    }

    @Override
    public TransferRecord value3(Long value) {
        setFromAccount(value);
        return this;
    }

    @Override
    public TransferRecord value4(Long value) {
        setToAccount(value);
        return this;
    }

    @Override
    public TransferRecord value5(Timestamp value) {
        setDate(value);
        return this;
    }

    @Override
    public TransferRecord values(Long value1, BigDecimal value2, Long value3, Long value4, Timestamp value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TransferRecord
     */
    public TransferRecord() {
        super(Transfer.TRANSFER);
    }

    /**
     * Create a detached, initialised TransferRecord
     */
    public TransferRecord(Long id, BigDecimal amount, Long fromAccount, Long toAccount, Timestamp date) {
        super(Transfer.TRANSFER);

        set(0, id);
        set(1, amount);
        set(2, fromAccount);
        set(3, toAccount);
        set(4, date);
    }
}

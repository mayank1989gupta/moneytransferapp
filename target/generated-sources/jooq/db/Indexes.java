/*
 * This file is generated by jOOQ.
 */
package db;


import db.tables.Account;
import db.tables.Transfer;

import javax.annotation.processing.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>PUBLIC</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index SYS_IDX_SYS_PK_10092_10095 = Indexes0.SYS_IDX_SYS_PK_10092_10095;
    public static final Index SYS_IDX_SYS_FK_10102_10107 = Indexes0.SYS_IDX_SYS_FK_10102_10107;
    public static final Index SYS_IDX_SYS_FK_10103_10109 = Indexes0.SYS_IDX_SYS_FK_10103_10109;
    public static final Index SYS_IDX_SYS_PK_10100_10104 = Indexes0.SYS_IDX_SYS_PK_10100_10104;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index SYS_IDX_SYS_PK_10092_10095 = Internal.createIndex("SYS_IDX_SYS_PK_10092_10095", Account.ACCOUNT, new OrderField[] { Account.ACCOUNT.ID }, true);
        public static Index SYS_IDX_SYS_FK_10102_10107 = Internal.createIndex("SYS_IDX_SYS_FK_10102_10107", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.FROM_ACCOUNT }, false);
        public static Index SYS_IDX_SYS_FK_10103_10109 = Internal.createIndex("SYS_IDX_SYS_FK_10103_10109", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.TO_ACCOUNT }, false);
        public static Index SYS_IDX_SYS_PK_10100_10104 = Internal.createIndex("SYS_IDX_SYS_PK_10100_10104", Transfer.TRANSFER, new OrderField[] { Transfer.TRANSFER.ID }, true);
    }
}

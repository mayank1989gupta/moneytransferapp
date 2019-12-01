package com.revolut.money.transfer.database;

import org.hsqldb.jdbc.JDBCPool;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * @author Mayank
 *
 * Class to set up the InMemory DB - HSQL<br>
 */
public class InMemoryDatabase {

    private static final String URL = "jdbc:hsqldb:res:/hsqldb/transfers";
    private final DSLContext context;

    public InMemoryDatabase() {
        JDBCPool pool = new JDBCPool();
        pool.setUrl(URL);
        context = DSL.using(pool, SQLDialect.HSQLDB);
    }

    public DSLContext context() {
        return context;
    }
}

package com.revolut.money.transfer.config;

import com.revolut.money.transfer.database.InMemoryDatabase;
import org.jooq.DSLContext;

public class TestMemoryDatabase extends InMemoryDatabase {

    private InMemoryDatabase wrappedInstance;

    public TestMemoryDatabase() {
    }

    public TestMemoryDatabase(InMemoryDatabase memoryDatabase) {
        wrappedInstance = memoryDatabase;
    }

    public void wrap(InMemoryDatabase memoryDatabase) {
        this.wrappedInstance = memoryDatabase;
    }

    public void shutdown() {
        wrappedInstance.context().execute("SHUTDOWN");
    }

    @Override
    public DSLContext context() {
        return wrappedInstance.context();
    }
}
package com.money.transfer.config;

import com.money.transfer.application.MoneyTransferApp;
import com.money.transfer.database.InMemoryDatabase;
import org.eclipse.jetty.client.HttpClient;
import spark.Spark;

public class TestConfig {

    private final TestMemoryDatabase database = new TestMemoryDatabase();
    private final MoneyTransferApp application = new MoneyTransferApp(database);
    private final HttpClient httpClient = new HttpClient();

    public void setUpAll() throws Exception {
        httpClient.start();

        application.start();
        Spark.awaitInitialization();
    }

    public void setUp() throws Exception {
        database.wrap(new InMemoryDatabase());
    }

    public void tearDown() throws Exception {
        database.shutdown();
    }

    public void tearDownAll() throws Exception {
        httpClient.stop();
        Spark.stop();
        Thread.sleep(2000);
    }

    public HttpClient httpClient() {
        return httpClient;
    }

}

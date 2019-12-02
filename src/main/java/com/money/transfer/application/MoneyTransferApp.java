package com.money.transfer.application;

import com.money.transfer.database.InMemoryDatabase;
import com.money.transfer.handlers.DataHandler;
import com.money.transfer.handlers.ErrorHandler;
import com.money.transfer.router.AccountRouter;
import com.money.transfer.router.TransferRouter;
import com.money.transfer.service.AccountService;
import com.money.transfer.service.TransferService;
import com.money.transfer.transformer.JSONTransformer;

import static spark.Spark.*;
import static spark.Spark.exception;

/**
 * @author Mayank
 *
 * Main class to register the routes &, handlers.<br>
 * The App uses the Spark Framework exposing the Rest API's<br>
 *
 * DB Used: <br>
 *     HSQL
 *
 */
public class MoneyTransferApp {
    private final ErrorHandler errorsHandler = new ErrorHandler();
    private final DataHandler dataHandlers = new DataHandler();
    private final JSONTransformer json = new JSONTransformer();

    private final AccountRouter account;
    private final TransferRouter transfer;

    public MoneyTransferApp(InMemoryDatabase database) {
        account = new AccountRouter(new AccountService(database));
        transfer = new TransferRouter(new TransferService(database));
    }


    // Main method to start the app
    public static void main(String[] args) {
        InMemoryDatabase db = new InMemoryDatabase();
        MoneyTransferApp aaplication = new MoneyTransferApp(db);

        aaplication.start();
    }

    /**
     * Method to routers/api's.<br>
     *
     * API is made public to be used from test config class.<br>
     */
    public void start() {
        port(8080); // to set port - default port 4567 for spark java
        setUpAccountAPIsPath();
        setUpTransferAPIsPath();

        registerResponseDataHandler();
        registerErrorHandler();
    }

    /************************************ Private Methods ********************************************/

    /**
     * Method to build route for Account API's.<br>
     */
    private void setUpAccountAPIsPath() {
        post("/account", account.create(), json);
        get("/accounts", account.fetchAccounts(), json);
        get("/account/:id", account.fetchAccountById(), json);
        get("/account/:id/transfers", account.fetchTransfersById(), json);
    }

    /**
     * Method to build route for Transfer API's.<br>
     */
    private void setUpTransferAPIsPath() {
        post("/transfer", transfer.transfer(), json);
        get("/transfers", transfer.fetchTransfers(), json);
        get("/transfer/:id", transfer.fetchTransferById(), json);
    }

    /**
     * Method to register the Response Data Handler<br>
     */
    private void registerResponseDataHandler() {
        after(dataHandlers.fetch());
    }

    /**
     * Method to register the Error Handler<br>
     *
     */
    private void registerErrorHandler() {
        exception(Exception.class, errorsHandler.exceptionsHandler());
    }
}

package com.money.transfer.router;

import com.google.gson.Gson;
import com.money.transfer.model.Account;
import com.money.transfer.model.NewAccount;
import com.money.transfer.validator.Validator;
import com.money.transfer.service.AccountService;
import spark.Route;

/**
 * @author Mayank
 *
 * Class acts as the Router for all the requests related to Account Services<br>
 */
public class AccountRouter {

    private final AccountService accountsService;

    public AccountRouter(AccountService accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * API to create a new account with the given details.<br>
     * Validates the given request and performs the action.<br>
     *
     * @return
     */
    public Route create() {
        return (request, response) -> {
            Gson gson = new Gson();
            NewAccount acc = gson.fromJson(request.body(), NewAccount.class);
            Validator.validateAccountCreation(acc);

            Account account = accountsService.create(acc);
            response.status(201); // Account created

            return account;

        };
    }

    /**
     * API to fetch all the registered accounts.<br>
     *
     * @return
     */
    public Route fetchAccounts() {
        return (request, response) -> accountsService.fetchAllAccounts();
    }

    /**
     * API to fetch Account by the given ID.<br>
     * API validates the given request and performs the action.<br>
     *
     * @return
     */
    public Route fetchAccountById() {
        return (request, response) -> {
            String id = request.params(":id");
            Validator.validateNumber(id);

            long accId = Long.parseLong(id);
            Validator.validateId(accId);

            return accountsService.fetch(accId);
        };
    }

    /**
     * API to fetch the account transfers for the given account id<br>
     * Validates the given request and performs the action.<br>
     *
     * @return
     */
    public Route fetchTransfersById() {
        return (request, response) ->  {
            String id = request.params(":id");
            Validator.validateNumber(id);

            long accId = Long.parseLong(id);
            Validator.validateId(accId);

            return accountsService.fetchTransfers(accId);
        };
    }
}

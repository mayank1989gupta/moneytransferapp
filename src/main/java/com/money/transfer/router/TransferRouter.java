package com.money.transfer.router;


import com.google.gson.Gson;
import com.money.transfer.model.NewTransfer;
import com.money.transfer.model.Transfer;
import com.money.transfer.validator.Validator;
import com.money.transfer.service.TransferService;
import spark.Route;

/**
 * @author Mayank
 *
 * Class acts as the Router for all the requests related to Transfer Services<br>
 */
public class TransferRouter {

    private final TransferService transfersService;

    public TransferRouter(TransferService transfersService) {
        this.transfersService = transfersService;
    }

    /**
     * API to register a new transfer.<br>
     *
     * @return
     */
    public Route transfer() {
        return (request, response) -> {
            Gson gson = new Gson();
            NewTransfer newTransfer = gson.fromJson(request.body(), NewTransfer.class);
            Validator.validateTransferRequest(newTransfer);

            Transfer transfer = transfersService.transfer(newTransfer);

            response.status(201);

            return transfer;
        };
    }

    /**
     * API to fetch all the transfers.<br>
     *
     * @return
     */
    public Route fetchTransfers() {
        return (request, response) -> transfersService.fetchAllTransfers();
    }

    /**
     * API to fetch a transfer detail by ID.<br>
     * @return
     */
    public Route fetchTransferById() {
        return (request, response) -> {
            String id = request.params(":id");
            Validator.validateNumber(id);

            long trId = Long.parseLong(id);
            Validator.validateId(trId);

            return transfersService.fetch(trId);
        };
    }
}

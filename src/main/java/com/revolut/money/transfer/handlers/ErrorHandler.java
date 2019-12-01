package com.revolut.money.transfer.handlers;

import com.revolut.money.transfer.model.Error;
import com.revolut.money.transfer.validator.Validator;
import com.revolut.money.transfer.transformer.JSONTransformer;
import org.jooq.exception.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ExceptionHandler;

/**
 * @author Mayank
 *
 * Error Handler Class<br>
 */
public class ErrorHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);
    private final JSONTransformer json = new JSONTransformer();

    public ExceptionHandler<? super Exception> exceptionsHandler() {
        return (exception, request, response) -> {
            Error error;

            if (exception instanceof Validator.ValidationException) {
                response.status(422);
                error = new Error("Validation error", exception.getMessage());
            } else if (exception instanceof NoDataFoundException) {
                response.status(404);
                error = new Error("No Data Found", exception.getMessage());
            } else {
                response.status(500);
                error = new Error("Internal server error", exception.getMessage());
            }

            LOGGER.error("Handled server exception", exception);
            response.body(json.render(error));
        };
    }
}

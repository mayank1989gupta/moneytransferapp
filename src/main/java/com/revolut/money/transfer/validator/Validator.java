package com.revolut.money.transfer.validator;

import com.revolut.money.transfer.model.NewAccount;
import com.revolut.money.transfer.model.NewTransfer;
import spark.utils.StringUtils;

import java.math.BigDecimal;

/**
 * @author Mayank
 *
 * Validator class for the requests<br>
 */
public class Validator {
    /**
     * Method to valudate the new transfer request<br>
     *
     * @param newTransfer
     */
    public static void validateTransferRequest(NewTransfer newTransfer) {
        if (null == newTransfer)
            throw new IllegalStateException("Transfer amount can not be null");

        if (newTransfer.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Transfer amount: " + newTransfer.getAmount() + " must be greater than 0");

        if (newTransfer.getFromAccount() == newTransfer.getToAccount())
            throw new ValidationException(
                    "Destination account: "
                            + newTransfer.getFromAccount()
                            + " must be different than source account: "
                            + newTransfer.getToAccount());
    }

    /**
     * Method to validate the new account creation request<br>
     * @param account
     */
    public static void validateAccountCreation(NewAccount account) {
        if (null == account)
            throw new IllegalStateException("Transfer amount can not be null");

        if (StringUtils.isEmpty(account.getNumber())) throw new ValidationException("Account Number can not be empty");

        if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("Account balance must zero or greater");
    }

    /**
     * Method to validate for invalid ID<br>
     *
     * @param id
     */
    public static void validateId(long id) {
        if (id <= 0)
            throw new ValidationException("Reference id: " + id + " must be positive");
    }

    /**
     * Number validator method<br<
     *
     * @param number
     */
    public static void validateNumber(String number) {
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new ValidationException("String: " + number + " is not a number");
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}

package com.money.transfer.validator;

import com.money.transfer.model.NewAccount;
import com.money.transfer.model.NewTransfer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Mayank
 *
 * Test class for Vaidator
 */
public class ValidatorTest {

    @Test
    void testValidateTransferRequest_ForNullNewTransfer_ThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> Validator.validateTransferRequest(null));
    }

    @Test
    void testValidateTransferRequest_ValidNewTransfer() {
        NewTransfer transfer = new NewTransfer(BigDecimal.valueOf(100), 1002, 1003);
        Validator.validateTransferRequest(transfer);
    }

    @Test
    void testValidateTransferRequest_ForNegativeAmount_ThrowValidationException() {
        NewTransfer transfer = new NewTransfer(BigDecimal.valueOf(-400), 1001, 1002);
        assertThrows(Validator.ValidationException.class, () -> Validator.validateTransferRequest(transfer));
    }

    @Test
    void testValidateTransferRequest_ForSameAccounts_ThrowValidationException() {
        NewTransfer transfer = new NewTransfer(BigDecimal.valueOf(400), 1001, 1001);
        assertThrows(Validator.ValidationException.class, () -> Validator.validateTransferRequest(transfer));
    }

    @Test
    void testValidateTransferRequest_InvalidAccountCreation_ThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> Validator.validateAccountCreation(null));
    }

    @Test
    void testValidateTransferRequest_ValidAccount() {
        NewAccount account = new NewAccount("Acccount 12", BigDecimal.valueOf(100));
        Validator.validateAccountCreation(account);
    }

    @Test
    void validateAccountCreation_InvalidAccountNumber_ThrowValidationException() {
        NewAccount ac = new NewAccount(null, BigDecimal.valueOf(100));
        assertThrows(Validator.ValidationException.class, () -> Validator.validateAccountCreation(ac));
    }

    @Test
    void validateAccountCreation_ForEmptyAccountNumber_ThrowValidationException() {
        NewAccount ac = new NewAccount("", BigDecimal.valueOf(100));
        assertThrows(Validator.ValidationException.class, () -> Validator.validateAccountCreation(ac));
    }

    @Test
    void validateAccountCreation_ForNegativeAccountBalance_ThrowValidationException() {
        NewAccount ac = new NewAccount("Account 12", BigDecimal.valueOf(-100));
        assertThrows(Validator.ValidationException.class, () -> Validator.validateAccountCreation(ac));
    }

    @Test
    void validateId_ValidId() {
        Validator.validateId(100);
    }

    @Test
    void validateId_ForNegativeId_ThrowValidationException() {
        assertThrows(Validator.ValidationException.class, () -> Validator.validateId(-100));
    }

    @Test
    void validateId_ForZeroId_ThrowValidationEx() {
        assertThrows(Validator.ValidationException.class, () -> Validator.validateId(0));
    }

    @Test
    void validateId_ForNumberAsString() {
        Validator.validateNumber("100");
    }

    @Test
    void validateNumber_ForUnparsableNumber_ThrowValidationException() {
        assertThrows(Validator.ValidationException.class, () -> Validator.validateNumber("123abc"));
    }
}

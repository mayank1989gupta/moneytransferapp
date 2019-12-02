package com.money.transfer.router;

import com.google.gson.Gson;
import com.money.transfer.config.TestConfig;
import com.money.transfer.model.Error;
import com.money.transfer.model.Transfer;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mayank
 *
 * Tester class for Account Router<br>
 */
public class TransferRouterTest {

    private static final TestConfig CONFIG = new TestConfig();

    @BeforeAll
    static void setUpAll() throws Exception {
        CONFIG.setUpAll();
    }

    @BeforeEach
    void setUp() throws Exception {
        CONFIG.setUp();
    }

    @AfterEach
    void tearDown() throws Exception {
        CONFIG.tearDown();
    }

    @AfterAll
    static void tearDownAll() throws Exception {
        CONFIG.tearDownAll();
    }

    @Test
    void testTransfer_ReturnSuccess() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAccount\":1001,\"toAccount\":1002,\"amount\":100}"));

        assertEquals(HttpStatus.CREATED_201, request.send().getStatus());
    }


    @Test
    void testTransfer_UpdatedBalances() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":1001,\"toAcc\":1002,\"amount\":100}"));
        request.send();

        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/account/1001");
        assertEquals("{\"id\":1001,\"number\":\"Account 1\",\"balance\":1000.00}",
                response.getContentAsString());

        response = CONFIG.httpClient().GET("http://localhost:8080/account/1002");
        assertEquals("{\"id\":1002,\"number\":\"Account 2\",\"balance\":2000.00}",
                response.getContentAsString());
    }

    @Test
    void testTransfer_ForTransferAllMoney() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":1001,\"toAcc\":1002,\"amount\":300}"));
        request.send();

        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/account/1001");
        assertEquals("{\"id\":1001,\"number\":\"Account 1\",\"balance\":1000.00}",
                res.getContentAsString());
    }

    @Test
    void testTransfer_FromTransferFromInvalidAccount_ReturnError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":25452,\"toAcc\":2,\"amount\":100}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);
        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testTransfer_ForTransferToInvalidAccount_ReturnError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":1,\"toAcc\":98787502,\"amount\":100}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);

        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testTransfer_ForInsufficientBalance_ReturnError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":1,\"toAcc\":2,\"amount\":10000}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);

        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testTransfer_ForTransferToSelf_ReturnError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAcc\":1,\"toAcc\":1,\"amount\":100}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, request.send().getStatus());
        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testTransfer_ForNegativeAmount_ReturnValidationError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAccount\":1,\"toAccount\":1,\"amount\":-20}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, request.send().getStatus());
        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testTransfer_ForZEROAmount_ReturnValidationError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/transfer");
        request.content(new StringContentProvider("{\"fromAccount\":1,\"toAccount\":1,\"amount\":0}"));
        ContentResponse response = request.send();

        Error error = new Gson().fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, request.send().getStatus());
        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testFetchAllTransfers() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/transfers");
        assertEquals("[{\"id\":3,\"timestamp\":\"Nov 25, 2019, 1:00:00 PM\",\"fromAccount\":{\"id\":1002,\"number\":\"Account 2\"},\"toAccount\":{\"id\":1004,\"number\":\"Account 4\"},\"amount\":300.00},{\"id\":2,\"timestamp\":\"Nov 23, 2019, 12:00:00 PM\",\"fromAccount\":{\"id\":1002,\"number\":\"Account 2\"},\"toAccount\":{\"id\":1003,\"number\":\"Account 3\"},\"amount\":200.00},{\"id\":1,\"timestamp\":\"Nov 21, 2019, 11:00:00 AM\",\"fromAccount\":{\"id\":1001,\"number\":\"Account 1\"},\"toAccount\":{\"id\":1002,\"number\":\"Account 2\"},\"amount\":100.00}]",
                response.getContentAsString());
    }

    @Test
    void testFetchTransfers_ReturnTransfersSortedByDateDesc() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/transfers");
        Transfer[] transfers = new Gson().fromJson(res.getContentAsString(), Transfer[].class);

        assertTrue(transfers[0].getTimestamp().compareTo(transfers[1].getTimestamp()) >= 0);
        assertTrue(transfers[1].getTimestamp().compareTo(transfers[2].getTimestamp()) >= 0);
    }

    @Test
    void testFetchTransferById() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/transfer/2");

        assertEquals("{\"id\":2,\"timestamp\":\"Nov 23, 2019, 12:00:00 PM\",\"fromAccount\":{\"id\":1002,\"number\":\"Account 2\"},\"toAccount\":{\"id\":1003,\"number\":\"Account 3\"},\"amount\":200.00}",
                res.getContentAsString());
    }

    @Test
    void testFetchTransferById_ForInvlaidId_ReturnErrorMessage() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/transfer/999999");
        Error error = new Gson().fromJson(res.getContentAsString(), Error.class);

        assertEquals(HttpStatus.NOT_FOUND_404, res.getStatus());
        assertEquals("No Data Found", error.getMsg());
    }

    @Test
    void testFetchTransferById_ForNonNumberId_ReturnErrorMessage() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/transfer/xyz");
        Error error = new Gson().fromJson(res.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, res.getStatus());
        assertEquals("Validation error", error.getMsg());
    }
}

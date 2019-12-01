package com.revolut.money.transfer.router;

import com.google.gson.Gson;
import com.revolut.money.transfer.config.TestConfig;
import com.revolut.money.transfer.model.Error;
import com.revolut.money.transfer.model.Transfer;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mayank
 *
 * Tester class for Account Router<br>
 */
public class AccountRouterTest {

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
    void testFetchAccounts() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/accounts");
        assertEquals("[{\"id\":1001,\"number\":\"Account 1\",\"balance\":1000.00}," +
                        "{\"id\":1002,\"number\":\"Account 2\",\"balance\":2000.00}," +
                        "{\"id\":1003,\"number\":\"Account 3\",\"balance\":3000.00}," +
                        "{\"id\":1004,\"number\":\"Account 4\",\"balance\":4000.00}]",
                response.getContentAsString());
    }

    @Test
    void testCreate_ReturnSuccess() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/account");
        request.content(new StringContentProvider("{\"number\":\"Account 1010\", \"balance\":10101}"));
        ContentResponse response = request.send();

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
    }

    @Test
    void testCreate_ForZeroBalance() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/account");
        request.content(new StringContentProvider("{\"number\":\"Account 1214\", \"balance\":0}"));
        ContentResponse response = request.send();

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
    }

    @Test
    void testCreate_ForNegativeBalance_ReturnError() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/account");
        request.content(new StringContentProvider("{\"balance\":-100}"));
        ContentResponse response = request.send();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.getStatus());
    }


    @Test
    void testCreate_ForEmptyName_ReturnErrorStatus() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/account");
        request.content(new StringContentProvider("{\"number\":\"\",\"balance\":1000}"));
        ContentResponse response = request.send();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.getStatus());
    }

    @Test
    void testFetchAccountById() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/account/1001");
        assertEquals("{\"id\":1001,\"number\":\"Account 1\",\"balance\":1000.00}", res.getContentAsString());
    }

    @Test
    void testFetchAccountById_ForIDNotPresent() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/account/99999");
        Gson gson = new Gson();
        Error error = gson.fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("No Data Found", error.getMsg());
    }

    @Test
    void testFetchAccountById_ForNonNumberId_ReturnResponseStatus() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/account/abc");
        Gson gson = new Gson();
        Error error = gson.fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.getStatus());
        assertEquals("Validation error", error.getMsg());

    }

    @Test
    void testFetchTransfersById() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/account/1004/transfers");
        assertNotNull(response.getContentAsString());
    }

    @Test
    void testFetchTransfersById_ReturnTransfersSortedByDateDesc() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/account/1002/transfers");
        Gson gson = new Gson();
        Transfer[] transfers = gson.fromJson(res.getContentAsString(), Transfer[].class);

        assertTrue(transfers[0].getTimestamp().compareTo(transfers[1].getTimestamp()) >= 0);
        assertTrue(transfers[1].getTimestamp().compareTo(transfers[2].getTimestamp()) >= 0);
    }

    @Test
    void testFetchTransfersById_ForInvalidID_ReturnStatusCode() throws Exception {
        ContentResponse response = CONFIG.httpClient().GET("http://localhost:8080/account/11212112/transfers");
        Gson gson = new Gson();
        Error error = gson.fromJson(response.getContentAsString(), Error.class);

        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("No Data Found", error.getMsg());
    }

    @Test
    void testFetchTransfersById_ForNonNumberId_ReturnErrorStatus() throws Exception {
        ContentResponse res = CONFIG.httpClient().GET("http://localhost:8080/account/xyz/transfers");
        Gson gson = new Gson();
        Error error = gson.fromJson(res.getContentAsString(), Error.class);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, res.getStatus());
        assertEquals("Validation error", error.getMsg());
    }

    @Test
    void testCreate() throws Exception {
        Request request = CONFIG.httpClient().POST("http://localhost:8080/account");
        request.content(new StringContentProvider("{\"number\":\"Account 10\", \"balance\":10000}"));
        ContentResponse response = request.send();

        assertTrue(null != response.getContentAsString());
    }
}

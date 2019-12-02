package com.money.transfer.handlers;

import spark.Filter;

/**
 * @author Mayank
 *
 * Class to set up the Data Handler for request-response<br>
 */
public class DataHandler {

    public Filter fetch() {
        return (request, response) -> {
            response.header("Content-Encoding", "gzip");
            response.type("application/json");
        };
    }
}

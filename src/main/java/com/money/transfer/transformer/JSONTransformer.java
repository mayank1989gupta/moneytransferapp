package com.money.transfer.transformer;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * @author Mayank
 *
 * Class to override the Response Transofmer<br>
 */
public class JSONTransformer implements ResponseTransformer {

    @Override
    public String render(Object o) {
        return new Gson().toJson(o);
    }
}

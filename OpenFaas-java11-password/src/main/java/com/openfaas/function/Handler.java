package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import java.util.HashMap;
import java.util.Map;

public class Handler extends com.openfaas.model.AbstractHandler {

    @Override
    public Response Handle(IRequest req) {

        Map<String, String> query = req.getQuery();
        String password = query.get("password");
        String passwordSha1 = HashService.generateSHA1(password);
        String queryResult = HashService.queryHIBP(passwordSha1);
        Map<String, Object> resJson = new HashMap<>();

        resJson.put("sha1", passwordSha1);
        resJson.put("query-result", queryResult);

        Response res = new Response();
        res.setBody(resJson.toString());
        res.setContentType("application/json");
        return res;
    }
}

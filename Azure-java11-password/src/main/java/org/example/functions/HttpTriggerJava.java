package org.example.functions;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerJava {
    /**
     * This function listens at endpoint "/api/HttpTriggerJava". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJava
     * 2. curl {your host}/api/HttpTriggerJava?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerJava")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        Map<String, String> query = request.getQueryParameters();
        String password = query.get("password");


        String passwordSha1 = HashService.generateSHA1(password);
        String queryResult = HashService.queryHIBP(passwordSha1);


        Map<String, Object> resJson = new HashMap<>();
        resJson.put("sha1", passwordSha1);
        resJson.put("query-result", queryResult);


        return request.createResponseBuilder(HttpStatus.OK).body(resJson.toString()).build();

    }
}

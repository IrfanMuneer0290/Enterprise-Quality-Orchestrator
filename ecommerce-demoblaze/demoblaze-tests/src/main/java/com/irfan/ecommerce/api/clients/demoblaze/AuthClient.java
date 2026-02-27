package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.LoginRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * AuthClient: Dedicated Authentication for DemoBlaze.
 * 2026-02-27: Implemented surgical string sanitization for malformed tokens.
 */
public class AuthClient extends BaseApiClient {

    public AuthClient() {
        super("demoblaze");
    }

  public String getAuthToken(String username, String password) {
        LoginRequest payload = new LoginRequest(username, password);
        String loginEndpoint = getProperty("api.endpoint.login");

        Response response = given().spec(getRequestSpec()).body(payload).post(loginEndpoint);
        String body = response.asString();

        if (body.contains("User does not exist") || body.isEmpty()) {
            given().spec(getRequestSpec()).body(payload).post("/signup");
            body = given().spec(getRequestSpec()).body(payload).post(loginEndpoint).asString();
        }

        // Nuclear Cleanup: Extract only the alphanumeric token part
        String token = body.contains("Auth_token:") ? body.split("Auth_token:")[1] : body;
        token = token.replaceAll("[^a-zA-Z0-9_-]", "").trim(); 

        if (token.isEmpty()) throw new RuntimeException("🛑 AUTH_FAILURE: Token extraction failed.");
        return token;
    }
}
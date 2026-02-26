package com.irfan.ecommerce.api.clients;

import com.irfan.ecommerce.api.payloads.LoginRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthClient extends BaseApiClient {
    
  public String getAuthToken(String username, String password) {
    LoginRequest payload = new LoginRequest(username, password);
    String endpoint = getProp("api.endpoint.login");

    Response response = given()
            .spec(getRequestSpec())
            .body(payload)
        .post(endpoint);

    // 🛡️ THE WALMART HARD GATE: Intercept failures before they poison the UI
    handleApiFailure(response, endpoint);

    String body = response.asString();
    
    // Validate that we actually have a token string and not HTML/Empty body
    if (body == null || !body.contains("Auth_token:")) {
        logger.error("🚨 TOKEN_PARSE_ERROR: Expected 'Auth_token:' but received: {}", body);
        throw new RuntimeException("🛑 API DATA BREACH: Response successful but token key is missing.");
    }

    // Now it's safe to parse
    return body.replace("Auth_token: ", "").trim();
}
}

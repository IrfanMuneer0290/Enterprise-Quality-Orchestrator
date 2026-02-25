package com.irfan.ecommerce.api.clients;

import com.irfan.ecommerce.api.payloads.LoginRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthClient extends BaseApiClient {
    public String getAuthToken(String username, String password) {
        // 🛠️ THE FIX: Use standard Constructor instead of Builder
        LoginRequest payload = new LoginRequest(username, password);

        Response response = given()
                .spec(getRequestSpec())
                .body(payload)
            .when()
                .post(getProp("api.endpoint.login"))
            .then()
                .spec(responseSpec)
                .extract().response();
        
        // 🛡️ THE WALMART HARD GATE: Catch the 500 HTML before it poisons the UI
    if (response.getStatusCode() != 200) {
        logger.error("🚨 AUTH FAILURE: Server returned [{}]. Body: {}", 
            response.getStatusCode(), response.asString());
        throw new RuntimeException("🛑 API BREAKDOWN: Cannot retrieve token. Server returned 500.");
    }

        return response.asString().replace("Auth_token: ", "").trim();
    }
}

package com.irfan.ecommerce.api.clients.demoblaze;

import com.irfan.ecommerce.api.clients.BaseApiClient;
import com.irfan.ecommerce.api.payloads.demoblaze.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

/**
 * AuthClient: Dedicated Authentication for DemoBlaze.
 * 2026-02-27: Fixed property key alignment to prevent null-path errors.
 */
public class AuthClient extends BaseApiClient {

    public AuthClient() {
        // Bridges to the "demoblaze" prefix in config.properties
        super("demoblaze");
    }

   public String getAuthToken(String username, String password) {
        LoginRequest payload = new LoginRequest(username, password);
        String loginEndpoint = getProperty("api.endpoint.login");

        // 1. ATTEMPT LOGIN
        Response response = given().spec(getRequestSpec()).body(payload).post(loginEndpoint);
        
        // 🚀 FAIL-FAST CHECK: Terminate if Server is Down (500)
        if (response.getStatusCode() >= 500) {
            logger.error("🛑 CRITICAL: Server error (HTTP {}) on Login. Terminating Test.", response.getStatusCode());
            throw new RuntimeException("🛑 ENVIRONMENT_FAILURE: API returned 5xx. Possible Server Down.");
        }

        String body = response.asString();

        // 2. SELF-HEALING: If user doesn't exist, try to register
        if (body.contains("User does not exist") || body.isEmpty()) {
            logger.warn("⚠️ AUTH: User missing. Registering: {}", username);
            Response signupRes = given().spec(getRequestSpec()).body(payload).post("/signup");
            
            // Check signup status too
            if (signupRes.getStatusCode() >= 500) {
                throw new RuntimeException("🛑 ENVIRONMENT_FAILURE: API returned 5xx on Signup.");
            }

            // Retry Login after signup
            response = given().spec(getRequestSpec()).body(payload).post(loginEndpoint);
            body = response.asString();
        }

        // 3. ROBUST EXTRACTION
        String token = "";
        if (body.contains("Auth_token:")) {
            token = body.replace("Auth_token:", "").trim();
        } else if (!body.contains("errorMessage") && body.length() > 20) {
            token = body.trim();
        }

        if (token.isEmpty()) {
            logger.error("🛑 CRITICAL: API Response Body: [{}]", body);
            throw new RuntimeException("🛑 AUTH_FAILURE: No Auth_token found in response.");
        }

        logger.info("✅ AUTH_SUCCESS: Token acquired.");
        return token;
    }
}
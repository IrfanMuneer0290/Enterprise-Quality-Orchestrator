package com.irfan.ecommerce.api.managers.restfulbooker;

import com.irfan.ecommerce.api.clients.restfulbooker.AuthClient;

public class AuthManager {
    private static String token;
    private static final AuthClient authClient = new AuthClient();

    public static synchronized String getToken() {
        if (token == null) {
            // THE WALMART RESUME REF: "Reduced Auth-Latency by 95% using 
            // a Memoized Token Provider."
            token = authClient.getAuthToken("admin", "password123");
        }
        return token;
    }
}
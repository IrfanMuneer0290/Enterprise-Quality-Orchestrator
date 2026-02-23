package com.irfan.ecommerce.api.clients;

import com.irfan.ecommerce.api.payloads.AddToCartRequest;
import static io.restassured.RestAssured.given;

public class CartClient extends BaseApiClient {
    public void addToCart(String productId, String token) {
        // 🛠️ THE FIX: Use standard Constructor
        AddToCartRequest payload = new AddToCartRequest(productId, token, true);

        given()
            .spec(getRequestSpec())
            .body(payload)
        .when()
            .post(getProp("api.endpoint.addtocart"))
        .then()
            .spec(responseSpec);
    }
}

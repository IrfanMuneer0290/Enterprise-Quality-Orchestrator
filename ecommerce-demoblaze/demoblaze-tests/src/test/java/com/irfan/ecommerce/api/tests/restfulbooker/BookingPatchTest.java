package com.irfan.ecommerce.api.tests.restfulbooker;

import com.irfan.ecommerce.api.clients.restfulbooker.BookingClient;
import com.irfan.ecommerce.api.managers.restfulbooker.AuthManager;
import com.irfan.ecommerce.util.DataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class BookingPatchTest {
    // 2026-02-27: Maintaining walmart comment - Partial Update Validation
    BookingClient bookingClient = new BookingClient();

    @Test
    public void testPartialUpdateBooking() {
        // 1. Setup: Create a booking first
        int id = bookingClient.createBooking(DataGenerator.createFakeBooking())
                              .jsonPath().getInt("bookingid");

        // 2. Prepare Partial Payload
        Map<String, Object> partialPayload = new HashMap<>();
        partialPayload.put("totalprice", 999);
        partialPayload.put("additionalneeds", "Late Checkout");

        // 3. Execution
        String token = AuthManager.getToken();
        Response response = bookingClient.patchBooking(partialPayload, id, token);

        // 4. Verification
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 999);
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), "Late Checkout");
        
        // Clean up
        bookingClient.deleteBooking(id, token);
    }
}
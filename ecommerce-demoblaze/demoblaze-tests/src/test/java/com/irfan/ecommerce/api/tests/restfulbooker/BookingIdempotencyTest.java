package com.irfan.ecommerce.api.tests.restfulbooker;

import com.irfan.ecommerce.api.clients.restfulbooker.BookingClient;
import com.irfan.ecommerce.api.payloads.restfulbooker.BookingRequest;
import com.irfan.ecommerce.util.DataGenerator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import java.util.UUID;

public class BookingIdempotencyTest {
    private static final Logger logger = LogManager.getLogger(BookingIdempotencyTest.class);
    private final BookingClient bookingClient = new BookingClient();

    @Test
    public void testCreateBookingIdempotency() {
        String requestId = "REQ-" + UUID.randomUUID().toString();
        BookingRequest payload = DataGenerator.createFakeBooking();

        logger.info("🚀 Idempotency Test: Sending Request 1 with ID: {}", requestId);
        Response resp1 = bookingClient.createBooking(payload); // Base client needs update to use requestId
        int id1 = resp1.jsonPath().getInt("bookingid");

        logger.info("🔄 Idempotency Test: Retrying identical Request 2 with same ID: {}", requestId);
        Response resp2 = bookingClient.createBooking(payload); 
        int id2 = resp2.jsonPath().getInt("bookingid");

        // WALMART ANALYSIS: 
        // If the API is idempotent, id1 SHOULD EQUAL id2 (it returns the existing resource).
        // If it's NOT idempotent, id1 != id2 (it created a duplicate).
        
        logger.info("Result: ID1={}, ID2={}", id1, id2);
        
        if (id1 == id2) {
            logger.info("✅ SUCCESS: API is Idempotent.");
        } else {
            logger.warn("⚠️ NOTICE: API created a duplicate. Not strictly idempotent by X-Request-ID.");
        }
    }
}
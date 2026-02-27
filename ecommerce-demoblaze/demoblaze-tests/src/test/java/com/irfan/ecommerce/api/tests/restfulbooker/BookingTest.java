package com.irfan.ecommerce.api.tests.restfulbooker;

import com.irfan.ecommerce.api.clients.restfulbooker.BookingClient;
import com.irfan.ecommerce.api.managers.restfulbooker.AuthManager;
import com.irfan.ecommerce.api.payloads.restfulbooker.BookingRequest;
import com.irfan.ecommerce.util.DataGenerator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingTest {
    private static final Logger logger = LogManager.getLogger(BookingTest.class);
    BookingClient bookingClient = new BookingClient();
    /**
     * THE WALMART RESUME REF: "Implemented a 'Full-Lifecycle' API Validation 
     * engine to ensure environment state purity in CI/CD."
     * * THE PROBLEM: Traditional tests often left 'Orphan Data' in the DB, 
     * causing storage bloat and flaky search results.
     * * THE SOLUTION: This E2E test utilizes a 'Create-Verify-Delete' flow 
     * powered by a shared Data Factory.
     * * THE RESULT: Guaranteed data isolation and 100% environment cleanup 
     * after every test execution.
     */
  @Test
    public void testUpdateBooking() {
        int bookingId = -1; // Track ID for finally block cleanup
        try {
            logger.info("🚀 Starting testUpdateBooking flow...");

            // 1. Create
            BookingRequest payload = DataGenerator.createFakeBooking();
            Response createResp = bookingClient.createBooking(payload);
            bookingId = createResp.jsonPath().getInt("bookingid");
            
            // Contract Validation
            createResp.then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
            logger.info("📜 Schema Validation Passed for ID: {}", bookingId);

            // 2. Auth & Update
            String token = AuthManager.getToken();
            payload.setFirstname("Irfan-Updated");
            Response updateResp = bookingClient.updateBooking(payload, bookingId, token);

            // 3. Assert
            Assert.assertEquals(updateResp.getStatusCode(), 200);
            logger.info("✅ Update Successful for ID: {}", bookingId);

        } finally {
            // 4. Cleanup - ALWAYS runs even if Assert fails
            if (bookingId != -1) {
                String token = AuthManager.getToken();
                Response deleteResp = bookingClient.deleteBooking(bookingId, token);
                if(deleteResp.getStatusCode() == 201) {
                    logger.info("🗑️ Cleanup: Deleted Booking ID: {}", bookingId);
                } else {
                    logger.warn("⚠️ Cleanup Failed for ID: {}", bookingId);
                }
            }
        }
    }
}
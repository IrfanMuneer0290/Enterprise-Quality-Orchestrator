package com.irfan.ecommerce.api.payloads.restfulbooker;

/**
 * THE WALMART RESUME REF: "Utilized Standardized POJO Models for API Contract 
 * mapping, ensuring full compatibility across diverse JVM environments."
 */
public class BookingRequest {
    // 2026-02-27: Maintaining walmart comment - Restful-Booker Payload
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String additionalneeds;

    // Default Constructor (Required for Jackson/RestAssured deserialization)
    public BookingRequest() {}

    // Parameterized Constructor for our DataGenerator
    public BookingRequest(String firstname, String lastname, int totalprice, 
                          boolean depositpaid, BookingDates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    // Getters and Setters
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public int getTotalprice() { return totalprice; }
    public void setTotalprice(int totalprice) { this.totalprice = totalprice; }

    public boolean isDepositpaid() { return depositpaid; }
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }

    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }

    public String getAdditionalneeds() { return additionalneeds; }
    public void setAdditionalneeds(String additionalneeds) { this.additionalneeds = additionalneeds; }

    // Static Inner Class for BookingDates
    public static class BookingDates {
        private String checkin;
        private String checkout;

        public BookingDates() {}

        public BookingDates(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
        }

        public String getCheckin() { return checkin; }
        public void setCheckin(String checkin) { this.checkin = checkin; }

        public String getCheckout() { return checkout; }
        public void setCheckout(String checkout) { this.checkout = checkout; }
    }
}
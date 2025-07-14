package BookingPOJO.PojoClasses;

import com.github.javafaker.Faker;

import java.util.concurrent.TimeUnit;

public class CreateBooking {

    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private Object bookingdates;
    private String additionalneeds;
    static Faker faker = new Faker();

    public static Object setRandomPayload() {
        CreateBooking payload = new CreateBooking();
        BookingDates bookingDates = new BookingDates();
        payload.setFirstname(faker.name().firstName());
        payload.setLastname(faker.name().lastName());
        payload.setTotalprice(faker.number().numberBetween(100, 1000));
        payload.setDepositpaid(faker.bool().bool());
        bookingDates.setCheckin(String.valueOf(faker.date().future(1, TimeUnit.DAYS)));
        bookingDates.setCheckout(String.valueOf(faker.date().future(10, TimeUnit.DAYS)));
        payload.setBookingdates(bookingDates);
        payload.setAdditionalneeds(faker.witcher().quote());
        return payload;

    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public boolean isDepositpaid() {
        return depositpaid;
    }

    public Object getBookingdates() {
        return bookingdates;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public void setDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public void setBookingdates(Object bookingdates) {
        this.bookingdates = bookingdates;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }
}

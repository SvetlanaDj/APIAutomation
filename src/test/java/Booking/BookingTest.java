package Booking;

import Files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class BookingTest {

    @Test
    public void healthCheck() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        given().log().all().  // da vidimo poslat zahtev
                when().get("/ping").
                then().log().all(). // da vidimo sta smo dobili u odgovoru
                assertThat().statusCode(201);

    }

    @Test
    public void getBookingIds() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        given().log().all()
                .when().get("/booking")
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void createBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        given().log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"firstname\" : \"Svetlana\",\n" +
                        "    \"lastname\" : \"Djolovic\",\n" +
                        "    \"totalprice\" : 120,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2018-01-01\",\n" +
                        "        \"checkout\" : \"2019-01-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .when().post("/booking")
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void getBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        String response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.bookingBody1())
                        .when().post("/booking")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        System.out.println("RESPONSE" + response);
        JsonPath js1 = new JsonPath(response);

        String bookingID = js1.getString("bookingid");
        System.out.println("ID: " + bookingID);
        Assert.assertTrue(response.contains("bookingid"));

        String responseAfterGetMethod = given().log().all()
                .when().get("/booking/" + bookingID)
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js2 = new JsonPath(responseAfterGetMethod);
        Assert.assertFalse(js2.getString("firstname").isBlank());
        Assert.assertFalse(js2.getString("lastname").isBlank());
        Assert.assertFalse(js2.getString("totalprice").isBlank());
        Assert.assertFalse(js2.getString("depositpaid").isBlank());
        Assert.assertFalse(js2.getString("bookingdates.checkin").isBlank());
        Assert.assertFalse(js2.getString("bookingdates.checkout").isBlank());
        Assert.assertFalse(js2.getString("additionalneeds").isBlank());

    }

    @Test
    public void updateBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Kreiram booking

        String response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.bookingBody1())
                        .when().post("/booking")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js1 = new JsonPath(response);
        String bookingID = js1.getString("bookingid");

        String tokenResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.tokenBody())
                        .when().post("/auth")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js2 = new JsonPath(tokenResponse);
        String token = js2.getString("token");

        // Kreiram update

        // Kod requestova gde je potreban token mozemo upisati vise od jednog header-a
        String updateResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .header("Cookie", "token="+token)
                        .body(Payload.bookingBody2())
                        .when().put("/booking/"+bookingID)
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js3 = new JsonPath(updateResponse);
        Assert.assertEquals(js3.getString("totalprice"), "50");
        Assert.assertEquals(js3.getString("depositpaid"), "false");
        Assert.assertEquals(js3.getString("bookingdates.checkout"), "2020-01-01");
        Assert.assertEquals(js3.getString("additionalneeds"), "Breakfast");
    }

    @Test
    public void partialUpdateBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Kreiram booking

        String response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.bookingBody1())
                        .when().post("/booking")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js1 = new JsonPath(response);
        String bookingID = js1.getString("bookingid");

        // Kreiram token

        String tokenResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.tokenBody())
                        .when().post("/auth")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js2 = new JsonPath(tokenResponse);
        String token = js2.getString("token");

        // Kreiram update

        String updateResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .header("Cookie", "token="+token)
                        .body("{\n" +
                                "    \"totalprice\" : 499\n" +
                                "}")
                        .when().patch("/booking/"+bookingID)
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js3 = new JsonPath(updateResponse);
        Assert.assertEquals(js3.getString("totalprice"), "499");
    }

    @Test
    public void deleteBooking() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        String response =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.bookingBody1())
                        .when().post("/booking")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js1 = new JsonPath(response);
        String bookingID = js1.getString("bookingid");

        /*String tokenResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(Payload.tokenBody())
                        .when().post("/auth")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js2 = new JsonPath(tokenResponse);
        String token = js2.getString("token");*/

        // Token izvlacim iz Payload klase, da ne bih morao da posebno pozivam token request

        given().log().all()
                .header("Cookie", "token="+Payload.token())
                .when().delete("/booking/"+bookingID)
                .then().log().all()
                .assertThat().statusCode(201);
    }


}
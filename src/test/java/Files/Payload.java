package Files;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class Payload {
    public static String bookingBody1() {
        return "{\n" +
                "{\n" +
                "    \"firstname\" : \"Svetlana\",\n" +
                "    \"lastname\" : \"Djolovic\",\n" +
                "    \"totalprice\" : 120,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";
    }

    public static String bookingBody2() {
        return "{\n" +
                "{\n" +
                "    \"firstname\" : \"Svetlana\",\n" +
                "    \"lastname\" : \"Djolovic\",\n" +
                "    \"totalprice\" : 120,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "}";
    }
    public static String tokenBody() {
        return "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";
    }

    public static String tokenBody2() {
        return "{\n" +
                "  \"userName\": \"{{usename}}\",\n" +
                "  \"password\": \"{{password}}\"\n" +
                "}";
    }

    public static String token() {
        String tokenResponse =
                given().log().all()
                        .header("Content-Type", "application/json")
                        .body(tokenBody())
                        .when().post("/auth")
                        .then().log().all()
                        .assertThat().statusCode(200)
                        .extract().response().asString();

        JsonPath js2 = new JsonPath(tokenResponse);
        return js2.getString("token");
    }


}
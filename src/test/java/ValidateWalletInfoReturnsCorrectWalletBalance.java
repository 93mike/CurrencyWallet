import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateWalletInfoReturnsCorrectWalletBalance {

    @Test
    public void testWalletReturnsCorrectBalance() {
        String baseUrl = Config.getBaseUrl();
        String token = Config.getToken();
        String address = Config.getAddress();
        int amount = Config.getAmount();

        Response response = given()
                .when()
                .get(baseUrl + "/addrs/" + address + "?token=" + token)
                .then()
                .extract().response();

        int beforeBalance = response.jsonPath().getInt("balance");

        String faucetUrl = baseUrl + "/faucet?token=" + token;
        String requestBody = "{\"address\": \"" + address + "\",\"amount\": " + amount + "}";

        Response response2 = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(faucetUrl);

        response = given()
                .when()
                .get(baseUrl + "/addrs/" + address + "?token=" + token)
                .then()
                .extract().response();

        int afterBalance = response.jsonPath().getInt("final_balance");

        assertEquals((beforeBalance + amount), afterBalance);
    }
}
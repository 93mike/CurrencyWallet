import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidateWalletReturnsCorrectListOfTransactionsTest {

    @Test
    public void walletReturnsCcorrectTransaction() {
        String baseUrl = Config.getBaseUrl();
        String token = Config.getToken();
        String address = Config.getAddress();
        int amount = Config.getAmount();

        String apiUrl2 = baseUrl + "/faucet?token=" + token;
        String requestBody = "{\"address\": \"" + address + "\",\"amount\": " + amount + "}";

        Response response2 = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(apiUrl2);

        JsonObject jsonResponse = JsonParser.parseString(response2.getBody().asString()).getAsJsonObject();
        String txRef = jsonResponse.get("tx_ref").getAsString();

        Response response = given()
                .when()
                .get(baseUrl + "/addrs/" + address + "?token=" + token)
                .then()
                .extract().response();

        List<String> txHashes = response.jsonPath().getList("unconfirmed_txrefs.tx_hash");
        assertTrue(txHashes.contains(txRef));
        String txAdress = response.jsonPath().get("address");
        assertEquals(address,txAdress);
    }
}
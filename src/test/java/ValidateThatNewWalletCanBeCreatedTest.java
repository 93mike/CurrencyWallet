import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ValidateThatNewWalletCanBeCreatedTest {
    private static final String TOKEN = Config.getToken();
    private static final String ADDRESS = Config.getAddress();
    private static final String ExistingWalletName = Config.getExistingWalletName();

    @BeforeAll
    public static void beforeTest() {
        RestAssured.baseURI = "https://api.blockcypher.com/v1/bcy/test";
    }

    @Test
    public void testCreateWalletAndAccessInfo() {
        Faker faker = new Faker();
        String randomString = faker.lorem().characters(5, 10);
        String apiUrl1 = "/wallets?token=" + TOKEN;
        String jsonBody = "{\"name\": \"" + randomString + "\",\"addresses\": [\"" + ADDRESS + "\"]}";

        given()
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .when()
                .post(apiUrl1)
                .then()
                .assertThat()
                .statusCode(201);

        String apiUrl2 = "/addrs/" + ADDRESS;

        given()
                .when()
                .get(apiUrl2)
                .then()
                .assertThat()
                .statusCode(200)
                .body("address", equalTo(ADDRESS))
                .body("total_received", notNullValue());
    }

    @Test
    public void testWalletListContainsMikeWallet() {
        Response response = given()
                .queryParam("token", TOKEN)
                .when()
                .get("/wallets")
                .then()
                .statusCode(200)
                .extract()
                .response();
        response.then().body("wallet_names", hasItem(ExistingWalletName));
    }
}

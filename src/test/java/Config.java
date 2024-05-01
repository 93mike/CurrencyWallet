import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream("src/test/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseURI() {
        return properties.getProperty("baseURI");
    }

    public static String getContentType() {
        return properties.getProperty("contentType");
    }

    public static String getToken() {
        return properties.getProperty("token");
    }

    public static String getAddress() {
        return properties.getProperty("address");
    }

    public static String getExistingWalletName() {
        return properties.getProperty("existingWalletName");
    }

    public static int getAmount() {
        return Integer.parseInt(properties.getProperty("amount"));
    }

    public static String getBaseUrl() {
        return "https://api.blockcypher.com/v1/bcy/test";
    }
}

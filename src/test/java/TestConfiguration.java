import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class TestConfiguration {

    private static Properties properties = new Properties();

    public static String serviceCode;
    public static String username;
    public static String password;
    public static String recipientEmail;
    public static String resolveUrl;

    public static void loadConfiguration() throws IOException {
        InputStream stream = ClassLoader.getSystemResourceAsStream("unittest.properties");
        properties.load(stream);

        System.out.println("Enumerating All Keys Found In Properties File");
        Enumeration<?> e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            //NO PASSWORDS IN TERMINAL
            if(key.toLowerCase().contains("password")){
                value = "********";
            }
            System.out.println("Key: " + key + ", Value: " + value);
        }
        System.out.println("Enumeration Complete. Now Assigning Values");

        serviceCode = properties.getProperty("servicecode");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        recipientEmail = properties.getProperty("recipientemail");
        resolveUrl = properties.getProperty("resolveurl");
    }





}

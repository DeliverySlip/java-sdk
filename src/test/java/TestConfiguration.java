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

        //try reading environment variables for values
        if(System.getenv("SM_SERVICE_CODE") != null){
            System.out.println("SM_SERVICE_CODE Environment Variable Detected. Setting serviceCode Value");
            serviceCode = System.getenv("SM_SERVICE_CODE");
        }

        if(System.getenv("SM_USERNAME") != null){
            System.out.println("SM_USERNAME Environment Variable Detected. Setting username Value");
            username = System.getenv("SM_USERNAME");
        }

        if(System.getenv("SM_PASSWORD") != null){
            System.out.println("SM_PASSWORD Environment Variable Detected. Setting password Value");
            password = System.getenv("SM_PASSWORD");
        }

        if(System.getenv("SM_RECIPIENT_EMAIL") != null){
            System.out.println("SM_RECIPIENT_EMAIL Environment Variable Detected. Setting recipientEmail Value");
            recipientEmail = System.getenv("SM_RECIPIENT_EMAIL");
        }

        if(System.getenv("SM_RESOLVE_URL") != null){
            System.out.println("SM_RESOLVE_URL Environment Variable Detected. Setting resolveUrl Value");
            resolveUrl = System.getenv("SM_RESOLVE_URL");
        }



        try{
            InputStream stream = ClassLoader.getSystemResourceAsStream("unittest.properties");
            properties.load(stream);

            serviceCode = properties.getProperty("servicecode");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            recipientEmail = properties.getProperty("recipientemail");
            resolveUrl = properties.getProperty("resolveurl");
        }catch(Exception e){
            System.out.println("Failed To Grab Settings From unittest.properties File. Tests Will Fail If Environment " +
                    "Variables Are Not Defined");
        }

    }





}

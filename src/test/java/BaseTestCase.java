import com.securemessaging.sm.auth.ServiceCodeResolver;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.rules.ExpectedException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTestCase{

    protected String serviceCode;
    protected String username;
    protected String password;

    protected String recipientEmail;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @BeforeAll
    protected void beforeClass(){

        try {

            System.out.println("Loading Test Configuration");
            TestConfiguration.loadConfiguration();
            System.out.println("Loading Test Configuration Complete");
        }catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }

        this.serviceCode = TestConfiguration.serviceCode;
        this.username = TestConfiguration.username;
        this.password = TestConfiguration.password;
        this.recipientEmail = TestConfiguration.recipientEmail;

        if(TestConfiguration.resolveUrl != null){
            System.out.println("A Resolve URL Was Specified In The Configuration. Changing ServiceCodeResolver's " +
                    "Endpoint");
            ServiceCodeResolver.setResolverUrl(TestConfiguration.resolveUrl);
        }


        System.out.println("Test Settings Loaded Successfully. They Are Set As Follows");
        System.out.println("Service Code: " + this.serviceCode);
        System.out.println("Username: " + this.username);
        System.out.println("Password: ********");
        System.out.println("Recipient Email: " + this.recipientEmail);
        System.out.println("Resolve URL: "  + TestConfiguration.resolveUrl);
        System.out.println("Setup Complete. Now Running Tests");


    }
}

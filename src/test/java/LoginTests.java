import com.fasterxml.jackson.databind.ser.Serializers;
import com.securemessaging.SecureMessenger;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.sm.auth.ServiceCodeResolver;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseTestCase {

    @Test
    @Ignore
    public void testLoginWithNetworkProxy() throws SecureMessengerException, SecureMessengerClientException{

        /*
            CCC Proxy Logic:

            1) The proxy should redirect all calls from the Java Client to the CCC. The CCC's endpoint can be found
            by printing the Endpoints.CCC constant as so:

                System.out.println(Endpoints.CCC);

            Messaging Proxy Logic:

            1) The proxy needs to lookup from the CCC each time for the Messaging API url so as to ensure it
            has the latest one. This can be done with the following code logic via the serviceCode

                String messagingApiUrl = ServiceCodeResolver.resolve(serviceCode);

            Java Client Logic:

            1) Configure the client to use the proxy servers location for CCC calls. This is done by calling this
            static method

                ServiceCodeResolver.setResolverUrl("https://myproxy.redirectingtoccc.local");

            2) Configure the client to use the proxy server location for Messaging API calls. This is done by
            instantiating the SecureMessenger the following way

                SecureMessenger messenger = new SecureMessenger("https://myproxy.redirectingtomessagingapi.local");

            3) Make sure the code in step one is called before any SecureMessenger is instantiated. Unexpected
            results if you do not!

         */

    }

    @Test
    public void testBasicLogin()throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);
    }

    @Test
    public void testLoginWithUrl() throws SecureMessengerException, SecureMessengerClientException{

        String messagingApiUrl = ServiceCodeResolver.resolve(serviceCode);
        SecureMessenger messenger = new SecureMessenger(messagingApiUrl);

        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

    }

    @Test
    public void testLoginWithClient() throws SecureMessengerException, SecureMessengerClientException{

        String messagingApiUrl = ServiceCodeResolver.resolve(serviceCode);
        ClientRequestHandler client = new ClientRequestHandler(messagingApiUrl);
        SecureMessenger messenger = new SecureMessenger(client);

        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

    }

    @Test
    public void testLoginWithAuthenticationToken() throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        String authenticationToken = messenger.getAuthenticationToken(2);
        Credentials authToken = new Credentials(authenticationToken);
        messenger.login(authToken);

    }
}

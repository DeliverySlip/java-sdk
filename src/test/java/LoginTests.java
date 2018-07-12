import com.fasterxml.jackson.databind.ser.Serializers;
import com.securemessaging.javamessenger.SecureMessenger;
import com.securemessaging.javamessenger.client.ClientRequestHandler;
import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.ex.SecureMessengerException;
import com.securemessaging.javamessenger.sm.Credentials;
import com.securemessaging.javamessenger.sm.auth.ServiceCodeResolver;
import org.junit.jupiter.api.Test;

public class LoginTests extends BaseTestCase {

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

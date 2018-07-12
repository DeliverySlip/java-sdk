import com.securemessaging.javamessenger.SecureMessenger;
import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.ex.SecureMessengerException;
import com.securemessaging.javamessenger.sm.Credentials;
import com.securemessaging.javamessenger.utils.BuildVersion;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


public class BuildVersionTests extends BaseTestCase{

    @Test
    @Disabled
    public void testBasicVersionFetch(){
        System.out.println(BuildVersion.getBuildVersion());
    }

    @Test
    @Disabled
    public void testMessengerInstanceVersionFetch() throws SecureMessengerException, SecureMessengerClientException {

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        System.out.println(messenger.getClientVersion());
    }
}

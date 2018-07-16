import com.securemessaging.SecureMessenger;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.utils.BuildVersion;

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

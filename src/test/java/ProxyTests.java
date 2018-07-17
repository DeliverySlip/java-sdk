import com.securemessaging.SecureMessenger;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.sm.auth.ServiceCodeResolver;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class ProxyTests extends BaseTestCase{

    @AfterAll
    public static void cleanup(){
        ClientRequestHandler.setOnProxyInterceptionEventListenerInterface(null);
    }

    @Test
    public void testLoginWithProxyInterceptor() throws SecureMessengerException, SecureMessengerClientException{


        //need to define this first so that it is picked up by all future instances of ClientRequestHandler
        ClientRequestHandler.setOnProxyInterceptionEventListenerInterface(new ClientRequestHandler.OnProxyInterceptionEventListenerInterface() {
            @Override
            public boolean interceptRequests() {
                System.out.println("Asking Permission To Intercept Requests");
                return true;
            }

            @Override
            public URI intercept(URI uri) {
                System.out.println("Intercepted Request Being Made To: " + uri);
                return uri;
            }
        });

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);

        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);


    }

    @Test
    @Disabled
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

}

import com.securemessaging.SecureMessenger;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.Message;
import com.securemessaging.sm.search.SearchMessagesResults;
import com.securemessaging.sm.search.SearchMessagesFilter;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class GetMessageTests extends BaseTestCase {

    @Test
    public void testGetMessageFirst25() throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();

        SearchMessagesResults results = messenger.searchMessages(filter);
        Iterator<Message> iterator = results.iterator();

        for(int i = 0; i < 25; i++){
            Message message = iterator.next();

            Message gotAgain = messenger.getMessage(message.getMessageGuid());

            if(!message.getMessageGuid().equals(message.getMessageGuid())){
                Assert.fail();
            }
        }
    }

}

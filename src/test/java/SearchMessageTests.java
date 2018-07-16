import com.securemessaging.SecureMessenger;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.sm.Message;
import com.securemessaging.sm.search.SearchMessagesResults;
import com.securemessaging.sm.search.SearchMessagesFilter;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class SearchMessageTests extends BaseTestCase {




    @Test
    public void testSearchEverything() throws SecureMessengerException, SecureMessengerClientException {
        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();

        SearchMessagesResults results = messenger.searchMessages(filter);

    }

    @Test
    public void testSearchEverythingIterator() throws SecureMessengerException, SecureMessengerClientException{
        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();

        SearchMessagesResults results = messenger.searchMessages(filter);
        Iterator<Message> iterator = results.iterator();
    }

    @Test
    public void testSearchEverythingIteratorFirst25() throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();

        SearchMessagesResults results = messenger.searchMessages(filter);
        Iterator<Message> iterator = results.iterator();

        for(int i = 0; i < 25; i++){
            Message message = iterator.next();
        }
    }

    @Test
    public void testSearchEverythingIteratorFirst50() throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();

        SearchMessagesResults results = messenger.searchMessages(filter);
        Iterator<Message> iterator = results.iterator();

        for(int i = 0; i < 50; i++){
            Message message = iterator.next();
        }
    }

    @Test
    public void testSearchEverythingPageFetching() throws SecureMessengerException, SecureMessengerClientException{

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();
        filter.setPageSize(6);

        SearchMessagesResults results = messenger.searchMessages(filter);
        Iterator<Message> iterator = results.iterator();

        for(int i = 0; i < 25; i++){
            Message message = iterator.next();
        }

    }

    @Test
    public void testSearchEverythingCollection() throws SecureMessengerException, SecureMessengerClientException{
        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        messenger.login(credentials);

        SearchMessagesFilter filter = new SearchMessagesFilter();
        filter.setPageSize(100);

        SearchMessagesResults results = messenger.searchMessages(filter);

        results.fetchAllSearchResultsLocally();
        results.toArray();
        results.toArray(new Message[results.size()]);
        results.clear();
        results.isEmpty();
    }


}

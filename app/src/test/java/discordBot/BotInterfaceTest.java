package discordBot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BotInterfaceTest {
    private Map<String, User> users = new HashMap<>();
    private final String userName1 = "testUser1";

    // TODO: 3/16/2022 could create a simple test bot that sends a message for further testing of onMessageReceived

    @Before
    public void populateMapUsers() {
        assertTrue("Map \"users\" must be empty before populating", users.isEmpty());
        User testUser1 = new User(userName1);
        users.put(userName1, testUser1);
        assertTrue("Map \"users\" must contain testUser1 before any tests", users.containsKey(userName1));
    }

    @After
    public void clearMapUsers() {
        users.clear();
    }

    @Test
    public void mustBeAbleToCreateANewUser() {

        String userName2 = "testUser2";
        User testUser2 = users.get(userName2);
        assertNull("testUser2 must be null before it is initiated", testUser2);
        //if statement is unnecessary as testUser2 will always be null at this point
        testUser2 = new User(userName2);
        assertNotNull("testUser2 must not be null after initiation", testUser2);
        users.put(userName2, testUser2);
        assertTrue("testUser2 must be in Map \"users\"", users.containsKey(userName2));
    }

    @Test
    public void mustNotCreateOrAlterAUserIfAlreadyExists() {

        String toCheckAgainst = "impostor";
        User testUser1 = users.get(userName1);
        if (testUser1 == null) {
            testUser1 = new User(toCheckAgainst);
            users.put(toCheckAgainst, testUser1);
        }
        assertFalse("Map \"users\" must not contain key impostor because testUser1 already existed", users.containsKey(toCheckAgainst));
        assertTrue("testUser1's data must remain unchanged in Map \"users\"", users.containsKey(userName1));
    }
}
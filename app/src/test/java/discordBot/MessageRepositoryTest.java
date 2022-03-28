package discordBot;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRepositoryTest {

    MessageRepository messageRepository = new MessageRepository();

    @BeforeClass
    public static void createDatabase() {
        MessageRepository.createTable();
    }

    @Test
    public void shouldCreateDatabaseInstance() {
        String discordID = "Markku Lavi";
        User testUser = messageRepository.create(new User(discordID));
        assertEquals(discordID, testUser.getDiscordId());
        assertNotEquals("Id must not be zero", 0, testUser.getDatabaseId());
        assertEquals("messagecount must be 1 after creation as user will be put into database at first message", 1, testUser.getMsgCount());
    }

    @Test
    public void shouldUpdateDatabaseInstance() {
        User testUser = new User("MARKO NÄRHI");
        testUser.setDiscordId("100");
        User testUserUpdate = messageRepository.create(testUser);
        int idOriginal = testUserUpdate.getDatabaseId();
        String nameOriginal = testUserUpdate.getUserName();
        int mesCountOriginal = testUserUpdate.getMsgCount();
        messageRepository.update(testUserUpdate);
        assertTrue(testUserUpdate.getMsgCount() > mesCountOriginal);
        assertEquals(testUserUpdate.getDatabaseId(), idOriginal);
        assertEquals(testUserUpdate.getUserName(), nameOriginal);
    }
}

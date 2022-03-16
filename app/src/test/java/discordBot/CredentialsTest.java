package discordBot;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CredentialsTest {

    @Test
    public void getBotToken() {
        assertTrue(Credentials.getBotToken() instanceof String);
        assertTrue(Credentials.getBotToken() != null);
    }

    @Test
    public void getWeatherAPIKey() {
        assertTrue(Credentials.getWeatherAPIKey() instanceof String);
        assertTrue(Credentials.getWeatherAPIKey() != null);
    }
}
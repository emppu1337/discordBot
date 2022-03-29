package discordBot;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeatherAPITest {

    @Test
    // Must be able to perform the API request successfully when request is built correctly.
    public void whenRequestIsCorrect_thenResponseOk() throws IOException {
        WeatherAPI test = new WeatherAPI();
        String testLocation = "London";

        test.weatherForecastToday(testLocation);

        assertEquals("request must succeed with code 200 when request is sent correctly", 200, test.getStatus());
    }

    @Test
    // Must be able to format location names that contain spaces to correct form for url.
    public void mustFormatLocationToCorrectForm() throws IOException {
        WeatherAPI test = new WeatherAPI();
        String testLocation = "Los ANGELES";

        test.weatherForecastToday(testLocation);

        assertEquals("request must succeed with code 200 when request is sent correctly", 200, test.getStatus());
    }

    @Test
    public void mustParseDataCorrectly() throws IOException {
        WeatherAPI test = new WeatherAPI();
        String testLocation = "Los Angeles";
        String expectedName = "Los angeles";
        String expectedRegion = "California";

        test.weatherForecastToday(testLocation);

        assertTrue("jotain jotain", test.getName().equalsIgnoreCase(expectedName));
        assertTrue("jotain jotain jotain", test.getRegion().equalsIgnoreCase(expectedRegion));
        System.out.println(test.getRegion());
        System.out.println(test.getTemp_c());
    }
}
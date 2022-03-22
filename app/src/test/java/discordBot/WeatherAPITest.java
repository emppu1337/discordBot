package discordBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeatherAPITest {

    @Test
    // Must be able to perform the API request successfully when request is built correctly.
    public void whenRequestIsCorrect_thenResponseOk() throws IOException {
        WeatherAPI test = new WeatherAPI();
        test.setLOCATION("london");

        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", test.getLOCATION());

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();

        Response response = client.newCall(request).execute();
        assertEquals("request must succeed with code 200 when request is sent correctly", 200, response.code());
        System.out.println(response);
    }

    @Test
    // Must be able to format location names that contain spaces to correct form for url.
    public void mustFormatLocationToCorrectForm() throws IOException {
        WeatherAPI test = new WeatherAPI();
        test.setLOCATION("los angeles");

        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", test.getLOCATION());

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();

        Response response = client.newCall(request).execute();
        assertEquals("request must succeed with code 200 when request is sent correctly", 200, response.code());
    }

    @Test
    public void mustParseDataCorrectly() throws IOException {
        WeatherAPI test = new WeatherAPI();
        test.setLOCATION("los angeles");
        String expectedName = "Los angeles";
        String expectedRegion = "California";

        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", test.getLOCATION());

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();

        Response response = client.newCall(request).execute();
        WeatherData testData;
        ObjectMapper testMapper = new ObjectMapper();

        testData = testMapper.readValue(Objects.requireNonNull(response.body()).string(), WeatherData.class);
        assertTrue("jotain jotain", testData.getName().equalsIgnoreCase(expectedName));
        assertTrue("jotain jotain jotain", testData.getRegion().equalsIgnoreCase(expectedRegion));
        System.out.println(testData.getRegion());
        System.out.println(testData.getTemp_c());
    }
}
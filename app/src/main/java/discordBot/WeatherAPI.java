package discordBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class WeatherAPI {

    private String LOCATION;
    private String name;
    private String region;
    private Double temp_c;
    private String text;

    public void weatherForecastToday() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", LOCATION);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();
        Response response = client.newCall(request).execute();

        WeatherData weatherData;
        ObjectMapper weatherDataMapper = new ObjectMapper();
        weatherData = weatherDataMapper.readValue(Objects.requireNonNull(response.body()).string(), WeatherData.class);

        name = weatherData.getName();
        region = weatherData.getRegion();
        temp_c = weatherData.getTemp_c();
        text = weatherData.getText();
    }

    public String getLOCATION() {
        return LOCATION.toLowerCase().replace(" ", "%20");
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public Double getTemp_c() {
        return temp_c;
    }

    public String getText() {
        return text;
    }
}
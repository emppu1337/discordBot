package discordBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class WeatherAPI {

    private int status;

    private String name;
    private String region;
    private Double temp_c;
    private String text;

    public void weatherForecastToday(String location) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", location);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();
        Response response = client.newCall(request).execute();

        status = response.code();

        WeatherData weatherData;
        ObjectMapper weatherDataMapper = new ObjectMapper();
        weatherData = weatherDataMapper.readValue(Objects.requireNonNull(response.body()).string(), WeatherData.class);

        this.name = weatherData.getName();
        this.region = weatherData.getRegion();
        this.temp_c = weatherData.getTemp_c();
        this.text = weatherData.getText();
    }

    public int getStatus() {
        return status;
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
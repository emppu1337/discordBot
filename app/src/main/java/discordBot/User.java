package discordBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class User {
    private String name;

    private boolean wantsSpam = false;
    private boolean wantsWeather = false;

    public User(String name) {
        this.name = name;
    }

    public void processMessage(String message) {
        System.out.println(name + ": " + message);
        if (message == null) return;
        if (wantsSpam == false) {
            wantsWeather = isCommandOf("weather", message);
            wantsSpam = isCommandOf("start", message);  // command to activate the bot by setting status "wantsSpam" to true
        }
        if (wantsSpam == true) {
            wantsSpam = !isCommandOf("stop", message);  // command to stop the bot
        }
    }

    // Method that takes two strings as parameters; one defined command and message from the method "add".
    // Returns true or false depending on the parameters.
    private boolean isCommandOf(String command, String message) {
        // Commands need to be defined in lowercase only.
        command = command.toLowerCase();
        message = message.toLowerCase();

        if (command.equalsIgnoreCase(message)) {
            System.out.println("Setting response to true");
            return true;
        }
        if (message.startsWith(command + " ")) {
            return true;
        }
        if (message.contains(" " + command + " ")) {
            return true;
        }
        return message.endsWith(" " + command); //IntelliJ simplified if-else
    }

    public boolean isWantsSpam() {
        return wantsSpam;
    }

    public void setWantsSpam(boolean wantsSpam) {
        this.wantsSpam = wantsSpam;
    }

    public boolean isWantsWeather() {
        return wantsWeather;
    }

    public void setWantsWeather(boolean wantsWeather) {
        this.wantsWeather = wantsWeather;
    }

    public CharSequence getHelloMessage() {
        return "Hello " + name;
    }

    public CharSequence getWeatherMessage() throws IOException {
        WeatherAPI weather = new WeatherAPI();
        weather.setLOCATION("los angeles");

        OkHttpClient client = new OkHttpClient();
        String API_URL = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", weather.getLOCATION());

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
                .addHeader("x-rapidapi-key", Credentials.getWeatherAPIKey())
                .build();

        Response response = client.newCall(request).execute();
        WeatherData weatherData;
        ObjectMapper testMapper = new ObjectMapper();

        weatherData = testMapper.readValue(Objects.requireNonNull(response.body()).string(), WeatherData.class);

        return name + " wants to know weather! Weather in " + weatherData.getName() + " is " + weatherData.getText();
    }
}

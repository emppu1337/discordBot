package discordBot;

import java.io.IOException;

public class User {

    private String userName;
    private String discordId;
    private int databaseId;
    private int msgCount = 1;
    private String location;

    private boolean wantsSpam = false;
    private boolean wantsWeather = false;
    private boolean givingLocation = false;

    public User(String discordId) {
        this.discordId = discordId;
    }

    public void processMessage(String message) {
        String ANSI_GREEN = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";
        String printOut = String.format(userName + " wrote:%n" + message);
        System.out.println(ANSI_GREEN + printOut + ANSI_RESET);

        if (message == null) return;
        if (wantsSpam == false) {
            wantsWeather = isCommandOf("weather", message);
            wantsSpam = isCommandOf("start", message);  // command to activate the bot by setting status "wantsSpam" to true
        }
        if (wantsSpam == true) {
            wantsSpam = !isCommandOf("stop", message);  // command to stop the bot
        }
        if (givingLocation == true && location == null) {
            wantsWeather = false;
            location = message.toLowerCase().replace(" ", "%20");
        }
    }

    // Method that takes two strings as parameters; one defined command and message from the method "add".
    // Returns true or false depending on the parameters.
    private boolean isCommandOf(String command, String message) {
        // Commands need to be defined in lowercase only.
        command = command.toLowerCase();
        message = message.toLowerCase();

        if (command.equalsIgnoreCase(message)) {
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

    public CharSequence getHelloMessage() {
        return "Hello " + userName;
    }

    public CharSequence getWeatherLocationRequest() {
        this.wantsWeather = false;
        this.givingLocation = true;
        return "Hello <@" + discordId + ">! Please write down location for the weather report.";
    }

    public CharSequence getWeatherMessage() throws IOException {
        WeatherAPI weather = new WeatherAPI();
        weather.weatherForecastToday(location);
        if (weather.getName() != null) {
            this.givingLocation = false;
            this.location = null;
            return "Weather report for " + userName + ": " + "Weather in " + weather.getName() + ", " + weather.getRegion() + " is " + weather.getText() + " with temperature at " + weather.getTemp_c() + " degrees Celsius.";
        } else {
            this.location = null;
            this.givingLocation = true;
            return "I'm sorry <@" + discordId + ">, I did not understand. Please try again.";
        }
    }

    public CharSequence getNotUnderstoodMessage() {
        return "I'm sorry <@" + discordId + ">, I did not understand. Please try again.";
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

    public boolean isGivingLocation() {
        return givingLocation;
    }

    public void setGivingLocation(boolean givingLocation) {
        this.givingLocation = givingLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

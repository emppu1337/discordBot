package discordBot;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotInterface extends ListenerAdapter {
    private MessageChannel messageChannel = null;
    private Map<String, User> users = new HashMap<>();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public BotInterface() {
        executor.scheduleAtFixedRate(responseRunnable, 0, 1, TimeUnit.SECONDS);
    }

    Runnable responseRunnable = new Runnable() {
        @Override
        public void run() {
            if (messageChannel == null) return;
            for (User user : users.values()) {

                if (user.isWantsSpam() == true) {
                    messageChannel.sendMessage(user.getHelloMessage()).queue();
                }

                if (user.isWantsWeather() == true) {

                    user.setWantsWeather(false);
                    try {
                        messageChannel.sendMessage(user.getWeatherMessage()).queue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        messageChannel = event.getChannel();            // save the channel
        if (event.getAuthor().isBot()) return;          // ignore bots
        String userName = event.getAuthor().getName();  // save username

        User user = users.get(userName);
        if (user == null) {                             // check that user does not exist in map
            user = new User(userName);                  // create new user and give it name from userName
            users.put(userName, user);                  // put user into Map "users" with key userName
        }
        user.processMessage(event.getMessage().getContentRaw());
    } // TODO: 3/17/2022 could userId be used instead of name?
}
/*
Bot runs on a scheduled executor, that executes a runnable method at fixed interval.
Runnable method "interacts" with users based on the status of the user.
Status of the user is defined by users input through onMessageReceived method.

For example:
- user sends message "weather". This sets users status "wantsWeather" from "false" to "true".
- if user.wantsWeather = true method "run" will ask user for input for city, set "wantsWeather" to false and "isGivingALocation" to true.
- if user.isGivingALocation = true bot will read users message, format it to comply with url input for weather API, send user weather information and set statuses back to default.

This makes it possible to add features to bot in a modular fashion and make bot appear as if it is interacting with the user in a chatbot-kind of way.

To keep track of users and their statuses, all users are saved in a database (a hashmap in this case). This is done when user first writes into the chat.
*/
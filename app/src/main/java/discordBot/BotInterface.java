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
    MessageRepository messageRepository = new MessageRepository();
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
        messageChannel = event.getChannel();
        // ignore bots
        if (event.getAuthor().isBot()) return;
        // create database table if this is the first user to send message
        if (users.isEmpty()) {
            MessageRepository.createTable();
        }

        String discordId = event.getAuthor().getId();
        String userName = event.getAuthor().getName();

        // get user from map "users"
        User user = users.get(discordId);

        // check if user does not exist in map "users" - create new user with discord id if not and give it username from above
        if (user == null) {
            user = new User(discordId);
            user.setUserName(userName);
            messageRepository.create(user);
            MessageRepository.setMessageRepositorySize(users.size());

            // put user into map "users" with key discordId
            users.put(discordId, user);
            System.out.println("Size of map users is: " + users.size());
        } else {
            messageRepository.update(user);
            messageRepository.printById(discordId);
        }
        user.processMessage(event.getMessage().getContentRaw());
    }
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
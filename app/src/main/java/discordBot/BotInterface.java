package discordBot;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class BotInterface extends ListenerAdapter {
    private MessageChannel messageChannel = null;
    private Map<String, User> users = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        messageChannel = event.getChannel();            // save the channel
        if (event.getAuthor().isBot()) return;          // ignore bots
        String userId = event.getAuthor().getId();      // save user ID
        String userName = event.getAuthor().getName();  // save username

        User user = users.get(userName);
        if (user == null) {                             // check that user does not exist in map
            user = new User(userName);                  // create new user and give it name from userName
            users.put(userName, user);                  // put user into Map "users" with key userName
            System.out.println(users.get(userName));
        }
        System.out.println(messageChannel);
        System.out.println(userId);
        System.out.println(userName);
    }
}

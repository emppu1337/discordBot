package discordBot;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotInterface extends ListenerAdapter {
    private MessageChannel messageChannel = null;

    @Override
    public void onMessageReceived (MessageReceivedEvent event) {
        messageChannel = event.getChannel();            // save the channel
        if (event.getAuthor().isBot()) return;          // ignore bots
        String userID = event.getAuthor().getId();      // save user ID
        String userName = event.getAuthor().getName();  // save username

    }

}

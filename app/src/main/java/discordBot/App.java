/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package discordBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class App {

    public static void main(String[] args) {

        try {
            JDABuilder discordBot = JDABuilder.createDefault(Credentials.getBotToken());
            discordBot.addEventListeners(new BotInterface() {
                @Override
                public void onReady(@NotNull ReadyEvent event) {
                    System.out.println("botInterface ready");
                }
            });
            discordBot.setStatus(OnlineStatus.ONLINE);
            discordBot.setActivity(Activity.watching("over you"));
            discordBot.build();
            System.out.println("login successful");
        }
        catch (LoginException loginException) {
            System.out.println("login failed");
        }
    }
}
package discordBot;

public class User {
    private String name;

    private boolean wantsSpam = true;

    public User(String name) {
        this.name = name;
    }

    public void add(String message) {
        System.out.println(name + ": " + message);
        if (message == null) return;
        wantsSpam = isCommandOf("start", message);  // command to activate the bot by setting status "wantsSpam" to true
        wantsSpam = !isCommandOf("stop", message);  // command to stop the bot
    }

    // Method that takes two strings as parameters; one defined command and message from the method "add".
    // Returns true or false depending on the parametres.
    private boolean isCommandOf(String command, String message) {
        // To get rid of case sensitivity. Note that commands need to be defined in lowercase only.
        command = command.toLowerCase();
        message = message.toLowerCase();

        if (command.equalsIgnoreCase(message)) {
            return true;
        }
        return false;
    }


    public boolean wantsSpam() {
        return wantsSpam;
    }

    public void setWantsSpam(boolean wantsSpam) {
        this.wantsSpam = wantsSpam;
    }

}

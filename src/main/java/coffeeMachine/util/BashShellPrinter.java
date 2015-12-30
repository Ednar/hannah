package coffeeMachine.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Skriver ut text till konsolen med extra formattering genom bash-commandon.
 */
public class BashShellPrinter {

    /**
     * Tömmer konsolen. Kan användas inför varje nytt spel.
     */
    public static void clear() {
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("clear");

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        try {
            commandExecutor.executeCommand();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        System.out.println(stdout);
    }
}

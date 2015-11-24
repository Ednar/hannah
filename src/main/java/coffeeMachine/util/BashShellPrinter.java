package coffeeMachine.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Skriver ut text till konsolen med extra formattering genom bash-commandon.
 */
public class BashShellPrinter {

    /**
     * Skriver ut text till konsolen i en box.
     * Text består av nyckel och värde från en Map.
     *
     * @param map Map med information att skriva ut
     * @param <K> Nyckel
     * @param <V> Värde
     */
    public static <K,V> void printMapInBox(Map<K, V> map) {
        String entries = "\\\\n";
        for (Map.Entry entry : map.entrySet()) {
            entries += entry.getKey() + " " + entry.getValue() + "\\\\n";
        }
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("echo " + entries + " | boxes -d netdata");

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

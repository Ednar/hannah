package hannah;

import jade.MicroBoot;
import jade.core.MicroRuntime;

public class App extends MicroBoot {

    public static void main(String[] args) {
        MicroBoot.main(args);

        try {
            MicroRuntime.startAgent("hannah", "hannah.ConcurrentMoodHannah", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

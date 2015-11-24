package coffeeMachine.story;

import java.util.concurrent.TimeUnit;

public class StoryTellingUtils {

    public static void slowWrite(String text) {
        writeAtSpeed(100, text);
    }

    public static void fastWrite(String text) {
        writeAtSpeed(10, text);
    }

    private static void writeAtSpeed(int speed, String text) {
        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            try {
                TimeUnit.MILLISECONDS.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void dramaticPause() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

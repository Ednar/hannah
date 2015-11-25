package coffeeMachine.story;

import java.util.concurrent.TimeUnit;

public class StoryTellingUtils {

    public static void slowWrite(String text) {
        writeAtSpeed(40, text);
    }

    public static void turboWrite(String text) {
        writeAtSpeed(10, text);
    }

    public static void fastWrite(String text) {
        writeAtSpeed(20, text);
    }

    public static void epicSlowWrite(String text) {
        writeAtSpeed(200, text);
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
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

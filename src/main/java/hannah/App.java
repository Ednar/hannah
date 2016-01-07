package hannah;

import hannah.utils.AudioPlayer;

import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws InterruptedException {


        AudioPlayer player = new AudioPlayer();

        player.play("cry.wav");
        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");

        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");
        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");
        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");
        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");
        TimeUnit.SECONDS.sleep(1);
        player.play("giggle.wav");
    }

}

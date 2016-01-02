package hannah;

import hannah.utils.AudioPlayer;
import jade.MicroBoot;

public class App extends MicroBoot {

    public static void main(String[] args) {


        AudioPlayer player = new AudioPlayer();

        player.play("cry.wav");
        player = new AudioPlayer();
        player.play("giggle.wav");

    }

}

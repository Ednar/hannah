package coffeeMachine.util;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class Mp3Player extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
    }

    public void dropBeat() {
        Application.launch();
        String bip = "rain.mp3";
        Media hit = new Media(Paths.get(bip).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);

        mediaPlayer.play();
    }
}

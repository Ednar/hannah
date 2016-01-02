package hannah.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AudioPlayer {

    private boolean playCompleted;
    private Clip clip;

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    public void play(String audioFilePath) {
        if (!playCompleted && clip != null) {
            clip.stop();
            executorService.shutdownNow();
        }
        executorService = Executors.newFixedThreadPool(1);

        File audioFile = new File(audioFilePath);

        executorService.submit((Runnable) () -> {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);

                clip.open(audioStream);

                clip.start();

                while (!playCompleted) {
                    // vänta på att klip ska spela klart
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                clip.close();

            } catch (UnsupportedAudioFileException ex) {
                System.out.println("The specified audio file is not supported.");
                ex.printStackTrace();
            } catch (LineUnavailableException ex) {
                System.out.println("Audio line for playing back is unavailable.");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Error playing the audio file.");
                ex.printStackTrace();
            }
        });


    }



}
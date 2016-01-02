package hannah.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class AudioPlayer {

    private Clip clip;

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private String audioFilePath;
    private Future<?> playingClip;

    public void play(String audioFilePath) {
        if (clip != null && clip.isRunning() && !Objects.equals(this.audioFilePath, audioFilePath)) {
            clip.stop();
            clip.close();
            playingClip.cancel(true);
        }

        if ( (clip != null &&  !clip.isRunning() )  || !Objects.equals(this.audioFilePath, audioFilePath) ) {
            this.audioFilePath = audioFilePath;

            File audioFile = new File(audioFilePath);

            playingClip = executorService.submit((Runnable) () -> {
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();

                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(audioStream);
                    clip.start();

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

}
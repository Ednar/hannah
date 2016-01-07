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

    public void play(String audioFilePath) {
        if (clip != null && clip.isOpen()) {
            if (this.audioFilePath != null && !this.audioFilePath.equals(audioFilePath)) {
                stop();
            }
        }

        this.audioFilePath = audioFilePath;
        File audioFile = new File(audioFilePath);

        if (clip != null && !clip.isRunning() || clip == null) {
            executorService.submit((Runnable) () -> {
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


    private void stop() {
        clip.stop();
        clip.close();
    }

}

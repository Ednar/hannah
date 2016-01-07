package hannah.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AudioPlayer {

    private Clip clip;

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private String audioFilePath;

    public void play(String audioFilePath) {
        if (isNewFilePath(audioFilePath)) {
            clip.stop();
            clip.close();
            clip = null;
        }

        this.audioFilePath = audioFilePath;

        if (clip != null && clip.isOpen()) return;

        executorService.submit((Runnable) () -> {
            try {
                File audioFile = new File(audioFilePath);
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
                clip.close();
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Error playing the audio file.");
                ex.printStackTrace();
            }
        });
    }

    private boolean isNewFilePath(final String audioFilePath) {return this.audioFilePath != null && !this.audioFilePath.equals(audioFilePath);}
}

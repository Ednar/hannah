package hannah.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlayerTwo implements Runnable {

    private String fileLocation;

    public void play(String fileName) {
        Thread t = new Thread(this);
        fileLocation = fileName;
        t.start();
    }

    public void run() {
        playSound(fileLocation);
    }

    private void playSound(String fileName) {
        File soundFile = new File(fileName);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[128000];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                line.write(abData, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
    }
}
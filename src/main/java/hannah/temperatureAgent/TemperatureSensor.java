package hannah.temperatureAgent;

import java.io.*;

public class TemperatureSensor {
    static String w1DirPath = "/sys/bus/w1/devices/28-021562c60fff/w1_slave";
    static String filePath = w1DirPath;
    static File file = new File(filePath);

    public static void main(String[] args) {
        measureTemperature();
    }

    public static void measureTemperature() {

        BufferedReader br;
        String output;

        while (true) {
            try {
                br = new BufferedReader(new FileReader(file));
                br.readLine();
                output = br.readLine();
                System.out.println("output: " + output);
                if (output != null) {
                    int idx = output.indexOf("t=");
                    if (idx > -1) {
                        // Temp data (multiplied by 1000) in 5 chars after t=
                        float tempC = Float.parseFloat(
                                output.substring(output.indexOf("t=") + 2));
                        // Divide by 1000 to get degrees Celsius
                        tempC /= 1000;
                        System.out.print(String.format("%.3f ", tempC));

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


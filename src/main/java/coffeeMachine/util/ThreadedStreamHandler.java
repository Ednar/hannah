package coffeeMachine.util;

import java.io.*;

/**
 * This class is intended to be used with the SystemCommandExecutor
 * class to let users execute system commands from Java applications.
 *
 * This class is based on work that was shared in a JavaWorld article
 * named "When System.exec() won't". That article is available at this
 * url:
 *
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
 *
 * Documentation for this class is available at this URL:
 *
 * http://devdaily.com/java/java-processbuilder-process-system-exec
 *
 *
 * Copyright 2010 alvin j. alexander, devdaily.com.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.

 * You should have received a copy of the GNU Lesser Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Please ee the following page for the LGPL license:
 * http://www.gnu.org/licenses/lgpl.txt
 *
 */
public class ThreadedStreamHandler extends Thread {

    InputStream inputStream;
    String adminPassword;
    OutputStream outputStream;
    PrintWriter printWriter;
    StringBuilder outputBuffer = new StringBuilder();
    private boolean sudoIsRequested = false;

    public ThreadedStreamHandler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ThreadedStreamHandler(InputStream inputStream, OutputStream outputStream, String adminPassword) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.printWriter = new PrintWriter(outputStream);
        this.adminPassword = adminPassword;
        this.sudoIsRequested = true;
    }

    public void run() {
        if (sudoIsRequested) {
            printWriter.println(adminPassword);
            printWriter.flush();
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                outputBuffer.append(line).append("\n");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public StringBuilder getOutputBuffer() {
        return outputBuffer;
    }

}











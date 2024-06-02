import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        try {
            try (Socket socket = new Socket("localhost", 5000)) {
                System.out.println("Connected to server.");

                // Audio capture setup
                AudioFormat format = new AudioFormat(16000, 16, 2, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                // Audio streaming logic
                OutputStream outputStream = socket.getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while (true) {
                    bytesRead = line.read(buffer, 0, buffer.length); // Capture audio from microphone
                    outputStream.write(buffer, 0, bytesRead); // Send audio to the server
                }
            }
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

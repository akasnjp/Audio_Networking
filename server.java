import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is waiting for client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Audio playback setup
            AudioFormat format = new AudioFormat(16000, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Audio streaming logic
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead); // Play the received audio
            }

            // Close resources
            line.drain();
            line.stop();
            line.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

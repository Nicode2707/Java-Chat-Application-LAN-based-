import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = input.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            // Read input from user and send to server
            String userMsg;
            while ((userMsg = consoleInput.readLine()) != null) {
                output.println(userMsg);
            }

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}

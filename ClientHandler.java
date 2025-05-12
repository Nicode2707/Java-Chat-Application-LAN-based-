import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Set<ClientHandler> clientHandlers;
    private String clientName;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Enter your name: ");
            clientName = input.readLine();
            broadcast(clientName + " has joined the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = input.readLine()) != null) {
                broadcast(clientName + ": " + message);
            }
        } catch (IOException e) {
            System.out.println(clientName + " disconnected.");
        } finally {
            try {
                clientHandlers.remove(this);
                broadcast(clientName + " has left the chat.");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcast(String message) {
        for (ClientHandler ch : clientHandlers) {
            ch.output.println(message);
        }
    }
}

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // Shared list of all active client output streams
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        int port = 5000;
        System.out.println("Chat Server started on port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Thread class to handle individual clients
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String clientAddress;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            // Grab the IP and Port so we know who is talking
            this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Safely add this new client's writer to our shared list
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Announce that someone joined!
                broadcast("--- [" + clientAddress + "] has joined the chat ---");

                String message;
                // Keep reading messages from this client
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    // Broadcast the message to EVERYONE
                    broadcast("[" + clientAddress + "]: " + message);
                }
            } catch (IOException e) {
                System.out.println(clientAddress + " disconnected abruptly.");
            } finally {
                // When they leave or type 'exit', remove them from the list
                if (out != null) {
                    synchronized (clientWriters) {
                        clientWriters.remove(out);
                    }
                }
                broadcast("--- [" + clientAddress + "] has left the chat ---");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Helper method to loop through all clients and send the message
    private static void broadcast(String message) {
        System.out.println("Broadcasting: " + message); // Server logs it
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}
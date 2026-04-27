import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        // EXAM NOTE: Change this to the Server's IP for Host-to-Guest testing
        String serverAddress = "127.0.0.1";
        int port = 5000;

        try {
            Socket socket = new Socket(serverAddress, port);
            System.out.println("Connected to Chat Server! Start typing...");

            // THREAD 1: Start a background thread to constantly listen to the server
            new Thread(new ServerListener(socket)).start();

            // THREAD 2: The Main thread handles typing and sending
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String msg = scanner.nextLine(); // Wait for user to type
                out.println(msg);                // Send it to server

                if ("exit".equalsIgnoreCase(msg)) {
                    System.out.println("Exiting chat...");
                    socket.close();
                    System.exit(0); // Force close the background thread too
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not connect to server.");
        }
    }

    // Inner class that acts as our listening thread
    private static class ServerListener implements Runnable {
        private Socket socket;

        public ServerListener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                // As long as the server keeps sending stuff, print it out
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                // This will trigger when we close the socket by typing 'exit'
                System.out.println("Disconnected from server.");
            }
        }
    }
}
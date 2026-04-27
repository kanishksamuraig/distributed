
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static Set<PrintWriter> clients;

    public static void main(String args[]) {
        System.out.println("Chat Server has been started!!");
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket clientsocket = serverSocket.accept();
            new ClientHandler(clientsocket).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Server Disconnected");
    }

    private static class ClientHandler extends Thread {

        private Socket socket;
        private PrintWriter out;
        private String clientAddress;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.clientAddress = socket.getInetAddress().getHostAddress();
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                broadcast(clientAddress + ":" + socket.getPort() + "has been connected");

                synchronized (clients) {
                    clients.add(out);
                }
                String message;
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    broadcast(message);
                }
            } catch (Exception e) {
                System.out.println(clientAddress + "Disconnected abruptly");
            } finally {
                broadcast(clientAddress + " has been disconnected");
                if (out != null) {
                    synchronized (clients) {
                        clients.remove(out);
                    }
                }
            }
        }

        public void broadcast(String message) {
            System.out.println(clientAddress + ":" + message);
            synchronized (clients) {
                for (PrintWriter writer : clients) {
                    writer.println(message);
                }
            }
        }

    }

}

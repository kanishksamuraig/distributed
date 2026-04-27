
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {

    public static void main(String[] args) {
        int port = 5000;
        String address = "127.0.0.1";
        try (Socket socket = new Socket(address, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            new Thread(new ServerListener(socket));
            while (true) {
                String msg = sc.nextLine();
                out.println(msg);

                if ("exit".equalsIgnoreCase(msg)) {
                    System.out.println("Exiting chat...");
                    socket.close();
                    System.exit(0);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static class ServerListener implements Runnable {

        private Socket socket;

        public ServerListener(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

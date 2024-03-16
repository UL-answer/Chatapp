import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static Map<String, PrintWriter> users = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running on port 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastMessage(String message, String sender) {
        for (Map.Entry<String, PrintWriter> entry : users.entrySet()) {
            if (!entry.getKey().equals(sender)) {
                entry.getValue().println(sender + ": " + message);
            }
        }
    }

    public static void sendPrivateMessage(String receiver, String message, String sender) {
        PrintWriter receiverWriter = users.get(receiver);
        if (receiverWriter != null) {
            receiverWriter.println(sender + " (private): " + message);
        }
    }

    public static void registerUser(String username, PrintWriter writer) {
        users.put(username, writer);
    }

    public static Set<String> getAllUsers() {
        return users.keySet();
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            writer.println("Enter your username:");
            username = reader.readLine();
            Server.registerUser(username, writer);

            writer.println("Welcome to the chat, " + username + "!");
            writer.println("To send a private message, use @username message format");

            String message;
            while ((message = reader.readLine()) != null) {
                if (message.startsWith("@")) {
                    int spaceIndex = message.indexOf(" ");
                    if (spaceIndex != -1 && spaceIndex < message.length() - 1) {
                        String receiver = message.substring(1, spaceIndex);
                        String privateMessage = message.substring(spaceIndex + 1);
                        Server.sendPrivateMessage(receiver, privateMessage, username);
                    } else {
                        writer.println("Invalid private message format. Use @username message");
                    }
                } else {
                    Server.broadcastMessage(message, username);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (username != null) {
                    Server.getAllUsers().remove(username);
                }
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
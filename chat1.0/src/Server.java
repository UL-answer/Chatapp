// 服务器端代码
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is running and waiting for connections...");

            Socket socketA = serverSocket.accept();
            System.out.println("Client A connected.");
            DataInputStream disA = new DataInputStream(socketA.getInputStream());
            DataOutputStream dosA = new DataOutputStream(socketA.getOutputStream());

            Socket socketB = serverSocket.accept();
            System.out.println("Client B connected.");
            DataInputStream disB = new DataInputStream(socketB.getInputStream());
            DataOutputStream dosB = new DataOutputStream(socketB.getOutputStream());

            while (true) {
                if (disA.available() > 0) {
                    String message = disA.readUTF();
                    dosB.writeUTF(message);
                    dosB.flush();
                }
                if (disB.available() > 0) {
                    String message = disB.readUTF();
                    dosA.writeUTF(message);
                    dosA.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
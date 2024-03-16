// 客户端代码
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientB {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter server port:");
            int port = scanner.nextInt();

            Socket socket = new Socket("localhost", port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("Enter your username:");
            String username = scanner.next();

            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        if (dis.available() > 0) {
                            String message = dis.readUTF();
                            System.out.println(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            while (true) {
                String message = scanner.nextLine();
                dos.writeUTF(username + ": " + message);
                dos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
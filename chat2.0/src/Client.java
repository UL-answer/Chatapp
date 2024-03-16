import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println(reader.readLine());
            String username = scanner.nextLine();
            writer.println(username);

            System.out.println(reader.readLine());

            Thread receiveThread = new Thread(() -> {
                String message;
                try {
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String input;
            while (true) {
                input = scanner.nextLine();
                writer.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
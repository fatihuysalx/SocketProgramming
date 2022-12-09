package net.codejava.networking.chat.ChatServer;

import net.codejava.networking.chat.server.ChatServer;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Thread:Birden fazla işi aynı anda yapmamızı sağlayan "İş Parçacığı".
 * bu classın asıl amacı birden fazla clienti aynı anda çalıştırmamıza olanak sağlaması.
 *
 */
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "Yeni kullanıcı bağlandı: " + userName;
            server.broadcast(serverMessage, Br.this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
              //WriteThread classındaki "bye" kodunu burdan çalıştırıyoruz.
            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " Çıkıldı.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("UserThreadda hata: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Yeni giriş yapan kullanıcalara o anda kimlerin aktif olduğunu gösteren ve listelern yapı.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Kullanıcı Bağlandı: " + server.getUserNames());
        } else {
            writer.println("Şu an da başka bağlı kullanıcı yoktur.");
        }
    }

    /**
     * İstemciye mesaj gönderme.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
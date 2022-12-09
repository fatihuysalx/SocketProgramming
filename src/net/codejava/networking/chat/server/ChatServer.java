package net.codejava.networking.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Server.
 * HashSet kullanım amacımız:Burayı anlayınca yaz.
 *Bu javayı bırakmandaki en büyük sebeptir.
 */
public class ChatServer {
    private final int port;
    private final Set<String> userNames = new HashSet<>();
    private final Set<UserThread> userThreads = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server " + port + "'unu dinliyor.");

            do {
                Socket socket = serverSocket.accept();
                System.out.println("Yeni kullanıcı bağlandı.");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            } while (true);

        } catch (IOException ex) {
            System.out.println("Server hatası: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Bir clientten diğer cliente mesaj iletir.
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Yeni kullnıcılarımızn adlarını saklıyoruz.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * Kullanıcımıızn bağlantısı kesildiği zaman adını ve UserThreadlarını kestimiz yapı.
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("Kullanıcı " + userName + " ayrıldı");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Bağlı kullanıcılarımız olduğunda true döndürmemizi burada sağlıyoruz.
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
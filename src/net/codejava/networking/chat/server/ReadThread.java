package net.codejava.networking.chat.client;

import java.io.*;
import java.net.*;

/**
 * Sunucumuzun konsolla olan işlerini yaptığımız yerdir.
 * Konsoldaki okuma ve yazma için yaptık burayı. Gomutanım bunlar bizim.
 * Sonsuz döndürüyoruz.Client ayrılana kadar.
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client) {
        this.socket = this.socket;
        this.client = client;

        try {
            InputStream input = this.socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                System.out.println("\n" + response);

                //Sunucu mesajından sonra kullanıcı adını yazdırıyoruz.
                if (client.getUserName() != null) {
                    System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}
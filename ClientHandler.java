package chatserver;

import commonchat.EncryptionUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    private static final String SECRET_KEY_STR = "sBv8k2qfE9uV7hF4+O0jPw==";
    private static final SecretKey SECRET_KEY = EncryptionUtil.getKeyFromString(SECRET_KEY_STR);

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            log("Error initializing client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            out.println("Enter your nickname: ");
            username = in.readLine();
            ChatServer.clients.put(username, this);

            log(username + " connected.");
            broadcastMessage(" " + username + " joined the chat!");

            String message;
            while ((message = in.readLine()) != null) {
                handleMessage(message);
            }
        } catch (IOException e) {
            log(username + " disconnected unexpectedly.");
        } finally {
            try {
                ChatServer.clients.remove(username);
                broadcastMessage(" " + username + " left the chat!");
                log(username + " connection closed.");
                socket.close();
            } catch (IOException e) {
                log("Error closing connection for " + username + ": " + e.getMessage());
            }
        }
    }

    private void handleMessage(String message) {
        try {
            if (message.startsWith("@")) {
                int spaceIndex = message.indexOf(" ");
                if (spaceIndex != -1) {
                    String targetUser = message.substring(1, spaceIndex);
                    String privateMsg = message.substring(spaceIndex + 1);
                    ClientHandler targetClient = ChatServer.clients.get(targetUser);
                    if (targetClient != null) {
                        targetClient.sendMessage("(Private) " + username + ": " + privateMsg);
                        this.sendMessage("(Private) To " + targetUser + ": " + privateMsg);
                        log("Private message from " + username + " to " + targetUser + ": " + privateMsg);
                    } else {
                        this.sendMessage("⚠ User " + targetUser + " not found.");
                    }
                }
            } else {
                broadcastMessage(username + ": " + message);
                log("Broadcast from " + username + ": " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            String decrypted = EncryptionUtil.decrypt(EncryptionUtil.encrypt(message, SECRET_KEY), SECRET_KEY);
            out.println(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler client : ChatServer.clients.values()) {
            if (client != this) client.sendMessage(message);
        }
        this.sendMessage(message);
    }

    private void log(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + time + "] " + message);
    }
}


package chatclient;

import commonchat.EncryptionUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;

public class ChatClientGUI extends Application {

    private TextArea chatArea;
    private TextField inputField;
    private PrintWriter out;
    private BufferedReader in;

    private static final String SECRET_KEY_STR = "sBv8k2qfE9uV7hF4+O0jPw==";
    private static SecretKey SECRET_KEY;

    @Override
    public void start(Stage primaryStage) {
        try {
            SECRET_KEY = EncryptionUtil.getKeyFromString(SECRET_KEY_STR);

            // GUI Setup
            chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.setWrapText(true);

            inputField = new TextField();
            inputField.setPromptText("Type message here...");

            Button sendButton = new Button("Send");
            sendButton.setDefaultButton(true);

            VBox root = new VBox(10, chatArea, inputField, sendButton);
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, 400, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("JavaFX Chat Client");
            primaryStage.show();

            // Connect to server
            Socket socket = new Socket("localhost", 5679);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Read server nickname prompt (optional)
            String prompt = in.readLine();

            // Prompt user for nickname
            TextInputDialog dialog = new TextInputDialog("Mona");
            dialog.setTitle("Enter Nickname");
            dialog.setHeaderText("Choose your chat nickname");
            dialog.setContentText("Nickname:");

            String nickname = dialog.showAndWait().orElse("Mona");
            out.println(nickname); // Send chosen nickname to server

            // Thread to listen for incoming messages
            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        // Decrypt the message
                        String decrypted = EncryptionUtil.decrypt(EncryptionUtil.encrypt(msg, SECRET_KEY), SECRET_KEY);
                        String finalMsg = decrypted;
                        Platform.runLater(() -> chatArea.appendText(finalMsg + "\n"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Send message actions
            sendButton.setOnAction(e -> sendMessage());
            inputField.setOnAction(e -> sendMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                String encrypted = EncryptionUtil.encrypt(message, SECRET_KEY);
                out.println(message); // Send plain text to server
                inputField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
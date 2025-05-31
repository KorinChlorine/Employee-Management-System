package com.example.villagerems;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatbotWindow {

    private DeepseekChatbot bot = new DeepseekChatbot();

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Tiny guy in the PC");

        if (!bot.ensureReady()) {
            return;
        }

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        TextField inputField = new TextField();
        inputField.setPromptText("Type a message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String input = inputField.getText();
            if (!input.isEmpty()) {
                chatArea.appendText("You: " + input + "\n");
                String response = bot.sendMessage(input);
                chatArea.appendText("Miniature guy I shrank and put in my PC: " + response + "\n\n");
                inputField.clear();
            }
        });

        VBox root = new VBox(10, chatArea, inputField, sendButton);
        root.setPadding(new Insets(10));
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }
}

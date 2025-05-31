// RegisterController.java
package com.example.villagerems;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    @FXML
    private void onRegister() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Fields cannot be empty.");
            return;
        }
        if (!pass.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        if (UserStore.register(user, pass)) {
            onShowLogin();
        } else {
            errorLabel.setText("User already exists.");
        }
    }

    @FXML
    private void onShowLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            errorLabel.setText("Failed to load login page.");
        }
    }
}
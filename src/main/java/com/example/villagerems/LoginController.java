// LoginController.java
package com.example.villagerems;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        if (UserStore.validate(user, pass)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/villager_ems.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
            } catch (IOException e) {
                errorLabel.setText("Failed to load main app.");
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void onShowRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            errorLabel.setText("Failed to load register page.");
        }
    }
}
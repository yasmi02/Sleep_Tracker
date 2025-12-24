package org.sleeptracker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class WelcomeController {

    @FXML private TextField nameField;
    @FXML private Button continueButton;

    @FXML
    public void initialize() {
        // Enter tuşu ile devam et
        nameField.setOnAction(e -> handleContinue());
    }

    @FXML
    private void handleContinue() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            nameField.setPromptText("Please enter your name 🌙");
            nameField.setStyle("-fx-prompt-text-fill: #e895d8;");
            return;
        }

        // İsmi kaydet
        saveUserName(name);

        // Ana uygulamaya geç
        openMainApp();
    }

    private void saveUserName(String name) {
        try {
            FileWriter writer = new FileWriter("user_preferences.txt");
            writer.write("username=" + name);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving username");
        }
    }

    private void openMainApp() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sleeptracker/fxml/main.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root, 600, 800);
            scene.getStylesheets().add(
                    getClass().getResource("/org/sleeptracker/css/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.setTitle("🌙 Sleepmoon");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

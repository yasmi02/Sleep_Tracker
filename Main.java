package org.sleeptracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // İlk açılış kontrolü
            boolean isFirstTime = !new File("user_preferences.txt").exists();

            String fxmlPath;
            String title;

            if (isFirstTime) {
                // İlk açılış
                fxmlPath = "/org/sleeptracker/fxml/welcome.fxml";
                title = "🌙 Welcome to Sleeply";
                System.out.println("👋 First time launch - showing welcome screen");
            } else {
                // Normal açılış - Ana ekran göster
                fxmlPath = "/org/sleeptracker/fxml/main.fxml";
                title = "🌙 Sleep Tracker";
                System.out.println("✅ Returning user - loading main screen");
            }


            // Load FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create scene (telefon boyutu)
            Scene scene = new Scene(root, 600, 800);

            // Load CSS
            String css = getClass().getResource("/org/sleeptracker/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            // Setup stage
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);  // Sabit boyut (telefon gibi)

            // Show
            primaryStage.show();

            System.out.println("✅ Application started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error starting application:");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.out.println("Application closing...");
    }

    public static void main(String[] args) {
        System.out.println("🌙 Starting Sleep Tracker...");
        launch(args);
    }
}


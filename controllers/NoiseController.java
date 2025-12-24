package org.sleeptracker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.sleeptracker.services.AudioPlayer;

import java.util.HashMap;
import java.util.Map;

public class NoiseController {

    @FXML private Button rainButton;
    @FXML private Button oceanButton;
    @FXML private Button forestButton;
    @FXML private Button fireplaceButton;
    @FXML private Button whiteNoiseButton;
    @FXML private Button brownNoiseButton;
    @FXML private Button cityButton;
    @FXML private Button cafeButton;
    @FXML private Button trainButton;
    @FXML private Button pianoButton;
    @FXML private Button guitarButton;
    @FXML private Button thunderButton;

    @FXML private Button playPauseButton;
    @FXML private Button stopButton;
    @FXML private Slider volumeSlider;
    @FXML private ComboBox<String> timerComboBox;
    @FXML private Label statusLabel;

    private AudioPlayer audioPlayer;
    private Map<String, String> soundPaths;
    private String currentSound = "";

    @FXML
    public void initialize() {
        audioPlayer = AudioPlayer.getInstance();
        soundPaths = new HashMap<>();

        loadSoundPaths();
        setupKeyboardShortcuts();

        timerComboBox.getItems().addAll("No Timer", "15 min", "30 min", "1 hour", "2 hours");
        timerComboBox.setValue("No Timer");

        volumeSlider.setValue(50);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioPlayer.setVolume(newVal.intValue());
        });
    }

    private void setupKeyboardShortcuts() {
        statusLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        handleClose();
                    }
                });
            }
        });
    }

    private void loadSoundPaths() {
        // Dosya isimleri TAM OLARAK dosyadaki gibi!
        soundPaths.put("Rain", "rain");
        soundPaths.put("Ocean", "ocean");
        soundPaths.put("Forest", "forest");
        soundPaths.put("Fireplace", "fire");
        soundPaths.put("White Noise", "white_noise");
        soundPaths.put("Brown Noise", "brown_noise");  // DÜZELTME: brown_nosie → brown_noise
        soundPaths.put("City", "city");
        soundPaths.put("Cafe", "Cafe");  // Büyük C!
        soundPaths.put("Train", "train");
        soundPaths.put("Piano", "piano");
        soundPaths.put("Guitar", "guitar");
        soundPaths.put("Thunderstorm", "thunderstorm");
    }

    @FXML
    private void handleRain() {
        playSound("Rain");
    }

    @FXML
    private void handleOcean() {
        playSound("Ocean");
    }

    @FXML
    private void handleForest() {
        playSound("Forest");
    }

    @FXML
    private void handleFireplace() {
        playSound("Fireplace");
    }

    @FXML
    private void handleWhiteNoise() {
        playSound("White Noise");
    }

    @FXML
    private void handleBrownNoise() {
        playSound("Brown Noise");
    }

    @FXML
    private void handleCity() {
        playSound("City");
    }

    @FXML
    private void handleCafe() {
        playSound("Cafe");
    }

    @FXML
    private void handleTrain() {
        playSound("Train");
    }

    @FXML
    private void handlePiano() {
        playSound("Piano");
    }

    @FXML
    private void handleGuitar() {
        playSound("Guitar");
    }

    @FXML
    private void handleThunderstorm() {
        playSound("Thunderstorm");
    }

    private void playSound(String soundName) {
        String soundFileName = soundPaths.get(soundName);

        if (soundFileName != null) {
            // DOĞRU KLASÖR: /org/sleeptracker/media/
            String fullPath = "/org/sleeptracker/media/" + soundFileName + ".mp3";

            System.out.println("🔊 Playing: " + fullPath);

            audioPlayer.stop();
            audioPlayer.play(fullPath);
            currentSound = fullPath;
            playPauseButton.setText("⏸️ Pause");
            statusLabel.setText("▶️ Playing: " + soundName);
            updateUI();
        } else {
            statusLabel.setText("❌ Sound not found: " + soundName);
            System.err.println("❌ Sound name not in map: " + soundName);
        }
    }

    @FXML
    private void handlePlayPause() {
        if (currentSound == null || currentSound.isEmpty()) {
            statusLabel.setText("Please select a sound first");
            return;
        }

        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            playPauseButton.setText("▶️ Play");
            statusLabel.setText("⏸️ Paused");
        } else {
            audioPlayer.play(currentSound);
            playPauseButton.setText("⏸️ Pause");
            statusLabel.setText("▶️ Playing");
        }
    }

    @FXML
    private void handleStop() {
        audioPlayer.stop();
        playPauseButton.setText("▶️ Play");
        statusLabel.setText("⏹️ Stopped");
        currentSound = "";
        updateUI();
    }

    @FXML
    private void handleTimerChange() {
        String timerValue = timerComboBox.getValue();
        if (!timerValue.equals("No Timer")) {
            statusLabel.setText("⏰ Timer: " + timerValue);
        }
    }

    private void updateUI() {
        boolean playing = audioPlayer.isPlaying();
        playPauseButton.setDisable(currentSound.isEmpty());
        stopButton.setDisable(!playing);
    }

    @FXML
    private void handleClose() {
        audioPlayer.stop();
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
}
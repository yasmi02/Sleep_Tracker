package org.sleeptracker.controllers;

import org.sleeptracker.models.MoodType;
import org.sleeptracker.models.SleepEntry;
import org.sleeptracker.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainController {

    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> bedHourSpinner;
    @FXML private Spinner<Integer> bedMinuteSpinner;
    @FXML private Spinner<Integer> wakeHourSpinner;
    @FXML private Spinner<Integer> wakeMinuteSpinner;
    @FXML private Slider qualitySlider;
    @FXML private Label qualityLabel;
    @FXML private ComboBox<MoodType> moodComboBox;
    @FXML private TextArea notesArea;
    @FXML private ListView<SleepEntry> recentEntriesListView;
    // @FXML private Label statusLabel;  // Yorum yaptık

    private DatabaseManager dbManager;
    private ObservableList<SleepEntry> recentEntries;

    @FXML
    public void initialize() {
        dbManager = new DatabaseManager();

        // Setup date picker
        datePicker.setValue(LocalDate.now());

        // Setup time spinners
        setupTimeSpinners();

        // Setup quality slider
        qualitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateQualityLabel(newVal.intValue());
        });
        updateQualityLabel(3);

        // Setup mood combo box
        moodComboBox.setItems(FXCollections.observableArrayList(MoodType.values()));
        moodComboBox.setValue(MoodType.NEUTRAL);

        // Load recent entries
        loadRecentEntries();

        // Setup cell factory for list view - RENK AYARI
        recentEntriesListView.setCellFactory(listView -> new ListCell<SleepEntry>() {
            @Override
            protected void updateItem(SleepEntry entry, boolean empty) {
                super.updateItem(entry, empty);

                if (empty || entry == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    setText(entry.toString());
                    setStyle(
                            "-fx-background-color: transparent;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 12;" +
                                    "-fx-border-color: rgba(255, 255, 255, 0.15);" +
                                    "-fx-border-radius: 12;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-padding: 16;" +
                                    "-fx-background-color:#1e2847;" +
                                    "-fx-text-fill: #FFFFFF;" +
                                    "-fx-background-radius: 15;" +
                                    "-fx-border-color: transparent" +
                                    "-fx-border-radius: 15;" +
                                    "-fx-border-width: 1.5;" +
                                    "-fx-padding: 18;" +
                                    "-fx-font-size: 15px;" +
                                    "-fx-font-weight: 500;"
                    );
                }

            }
        });
    }

    private void setupTimeSpinners() {
        // Bed time - default 23:00
        bedHourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 23)
        );
        bedMinuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0)
        );

        // Wake time - default 07:00
        wakeHourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 7)
        );
        wakeMinuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0)
        );
    }

    private void updateQualityLabel(int quality) {
        String stars = "⭐".repeat(quality);
        qualityLabel.setText(stars + " (" + quality + "/5)");
    }

    @FXML
    private void handleSaveEntry() {  // handleSave → handleSaveEntry
        try {
            // Create sleep entry
            SleepEntry entry = new SleepEntry();
            entry.setDate(datePicker.getValue());

            LocalTime bedTime = LocalTime.of(
                    bedHourSpinner.getValue(),
                    bedMinuteSpinner.getValue()
            );
            entry.setBedTime(bedTime);

            LocalTime wakeTime = LocalTime.of(
                    wakeHourSpinner.getValue(),
                    wakeMinuteSpinner.getValue()
            );
            entry.setWakeTime(wakeTime);

            entry.setQuality((int) qualitySlider.getValue());
            entry.setMood(moodComboBox.getValue());
            entry.setNotes(notesArea.getText());

            // Save to database
            dbManager.saveSleepEntry(entry);

            System.out.println("✅ Sleep entry saved! (" +
                    String.format("%.1f", entry.getHoursSlept()) + " hours)");

            loadRecentEntries();
            resetForm();

        } catch (Exception e) {
            System.err.println("❌ Error saving entry: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        resetForm();
    }

    private void resetForm() {
        datePicker.setValue(LocalDate.now());
        qualitySlider.setValue(3);
        moodComboBox.setValue(MoodType.NEUTRAL);
        notesArea.clear();
    }

    private void loadRecentEntries() {
        recentEntries = FXCollections.observableArrayList(
                dbManager.getRecentEntries(7)
        );
        recentEntriesListView.setItems(recentEntries);
    }
    @FXML
    private void handleOpenAlarms() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sleeptracker/fxml/alarm.fxml")
            );
            Parent root = loader.load();

            Stage alarmStage = new Stage();
            Scene scene = new Scene(root, 600, 800);
            scene.getStylesheets().add(
                    getClass().getResource("/org/sleeptracker/css/style.css").toExternalForm()
            );

            alarmStage.setTitle("⏰ Alarms");
            alarmStage.setResizable(false);
            alarmStage.setScene(scene);
            alarmStage.show();

        } catch (Exception e) {
            System.err.println("❌ Error opening alarms");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenStats() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sleeptracker/fxml/stats.fxml")
            );
            Parent root = loader.load();

            Stage statsStage = new Stage();
            Scene scene = new Scene(root, 600, 800);
            scene.getStylesheets().add(
                    getClass().getResource("/org/sleeptracker/css/style.css").toExternalForm()
            );

            statsStage.setTitle("📊 Sleep Statistics");
            statsStage.setScene(scene);
            statsStage.show();

        } catch (Exception e) {
            System.err.println("❌ Error opening stats window");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenNoise() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/sleeptracker/fxml/noise.fxml")
            );
            Parent root = loader.load();

            Stage noiseStage = new Stage();
            Scene scene = new Scene(root, 600, 850);
            scene.getStylesheets().add(
                    getClass().getResource("/org/sleeptracker/css/style.css").toExternalForm()
            );

            noiseStage.setTitle("🎵 White Noise Player");
            noiseStage.setScene(scene);
            noiseStage.show();

        } catch (Exception e) {
            System.err.println("❌ Error opening noise player");
            e.printStackTrace();
        }
    }
}

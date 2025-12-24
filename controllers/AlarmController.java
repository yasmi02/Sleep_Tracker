package org.sleeptracker.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.sleeptracker.models.Alarm;
import org.sleeptracker.services.AlarmService;
import org.sleeptracker.services.AudioPlayer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmController {
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private TextField alarmNameField;
    @FXML private ComboBox<String> soundComboBox;
    @FXML private VBox alarmListContainer;
    @FXML private CheckBox monCheck;
    @FXML private CheckBox tueCheck;
    @FXML private CheckBox wedCheck;
    @FXML private CheckBox thuCheck;
    @FXML private CheckBox friCheck;
    @FXML private CheckBox satCheck;
    @FXML private CheckBox sunCheck;
    private AlarmService alarmService;
    private List<Alarm> alarms;

    @FXML
    public void initialize() {
        alarmService = AlarmService.getInstance();
        alarms = new ArrayList<>();
        System.out.println("⏰ AlarmService initialized!");
        setupTimeSpinners();
        soundComboBox.getItems().addAll("Classic Alarm","Gentle Wake","Rooster","Digital Beep","Siren");
        soundComboBox.setValue("Classic Alarm");
        soundComboBox.setOnAction(e -> {
            String s = soundComboBox.getValue();
            if(s != null) previewAlarmSound(s);
        });
        loadAlarms();
    }

    private void setupTimeSpinners() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,23,7));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59,0));
    }

    private void previewAlarmSound(String soundName) {
        try {
            AudioPlayer ap = AudioPlayer.getInstance();
            ap.stop();
            String path = "/org/sleeptracker/media/" + getSoundFileName(soundName) + ".mp3";
            System.out.println("🔊 Preview: " + soundName);
            ap.play(path);
            new Thread(() -> {
                try { Thread.sleep(3000); ap.stop(); } catch(Exception ex){}
            }).start();
        } catch(Exception e) { e.printStackTrace(); }
    }

    private String getSoundFileName(String soundName) {
        return switch(soundName) {
            case "Classic Alarm" -> "classic_alarm";
            case "Gentle Wake" -> "gentle_wake";
            case "Rooster" -> "Rooster sound";
            case "Digital Beep" -> "digital_beep";
            case "Siren" -> "siren";
            default -> "classic_alarm";
        };
    }

    @FXML
    private void handleSaveAlarm() {
        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        String name = alarmNameField.getText().trim();
        if(name.isEmpty()) name = String.format("%02d:%02d Alarm",hour,minute);
        String sound = soundComboBox.getValue();
        List<String> days = getSelectedDays();
        Alarm alarm = new Alarm(LocalTime.of(hour,minute),name,sound,days,true);
        alarmService.addAlarm(alarm);
        alarms.add(alarm);
        loadAlarms();
        alarmNameField.clear();
        clearDayToggles();
        System.out.println("✅ Alarm saved: " + alarm);
    }

    private List<String> getSelectedDays() {
        List<String> days = new ArrayList<>();
        if(monCheck.isSelected()) days.add("Mon");
        if(tueCheck.isSelected()) days.add("Tue");
        if(wedCheck.isSelected()) days.add("Wed");
        if(thuCheck.isSelected()) days.add("Thu");
        if(friCheck.isSelected()) days.add("Fri");
        if(satCheck.isSelected()) days.add("Sat");
        if(sunCheck.isSelected()) days.add("Sun");
        return days;
    }

    private void clearDayToggles() {
        monCheck.setSelected(false);
        tueCheck.setSelected(false);
        wedCheck.setSelected(false);
        thuCheck.setSelected(false);
        friCheck.setSelected(false);
        satCheck.setSelected(false);
        sunCheck.setSelected(false);
    }

    private void loadAlarms() {
        alarmListContainer.getChildren().clear();
        for(Alarm alarm : alarmService.getAllAlarms()) {
            alarmListContainer.getChildren().add(createAlarmCard(alarm));
        }
    }

    private VBox createAlarmCard(Alarm alarm) {
        VBox card = new VBox(10);
        card.getStyleClass().add("alarm-card");
        Label timeLabel = new Label(alarm.getTimeFormatted());
        timeLabel.setStyle("-fx-font-size:32px;-fx-font-weight:700;-fx-text-fill:#e895d8;");
        Label nameLabel = new Label(alarm.getName());
        nameLabel.setStyle("-fx-font-size:16px;-fx-text-fill:white;");
        Label daysLabel = new Label(String.join(", ",alarm.getDays()));
        daysLabel.setStyle("-fx-font-size:14px;-fx-text-fill:rgba(255,255,255,0.7);");
        HBox bottomBox = new HBox(15);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        ToggleButton activeToggle = new ToggleButton(alarm.isActive()?"ON":"OFF");
        activeToggle.setSelected(alarm.isActive());
        activeToggle.setStyle("-fx-background-color:"+(alarm.isActive()?"linear-gradient(from 0% 0% to 100% 0%,#e895d8,#9c7bea);":"rgba(59,74,125,0.4);")+"-fx-text-fill:white;-fx-background-radius:20;-fx-font-weight:600;-fx-padding:8 20 8 20;");
        activeToggle.setOnAction(e->{
            alarm.setActive(activeToggle.isSelected());
            activeToggle.setText(alarm.isActive()?"ON":"OFF");
            activeToggle.setStyle("-fx-background-color:"+(alarm.isActive()?"linear-gradient(from 0% 0% to 100% 0%,#e895d8,#9c7bea);":"rgba(59,74,125,0.4);")+"-fx-text-fill:white;-fx-background-radius:20;-fx-font-weight:600;-fx-padding:8 20 8 20;");
            alarmService.updateAlarm(alarm);
        });
        Region spacer = new Region();
        HBox.setHgrow(spacer,Priority.ALWAYS);
        Button deleteButton = new Button("🗑️ Delete");
        deleteButton.setStyle("-fx-background-color:rgba(255,82,82,0.7);-fx-text-fill:white;-fx-background-radius:20;-fx-font-weight:600;-fx-padding:8 20 8 20;-fx-cursor:hand;");
        deleteButton.setOnAction(e->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Alarm");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Delete alarm: "+alarm.getName()+"?");
            alert.showAndWait().ifPresent(response->{
                if(response==ButtonType.OK){
                    alarmService.removeAlarm(alarm);
                    loadAlarms();
                }
            });
        });
        bottomBox.getChildren().addAll(activeToggle,spacer,deleteButton);
        card.getChildren().addAll(timeLabel,nameLabel,daysLabel,bottomBox);
        return card;
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) hourSpinner.getScene().getWindow();
        stage.close();
    }
}
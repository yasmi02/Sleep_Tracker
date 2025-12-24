package org.sleeptracker.services;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.sleeptracker.models.Alarm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmService {
    private static AlarmService instance;
    private List<Alarm> alarms;
    private Timer timer;
    private AudioPlayer audioPlayer;

    private AlarmService() {
        alarms = new ArrayList<>();
        audioPlayer = AudioPlayer.getInstance();
        startAlarmChecker();
    }

    public static AlarmService getInstance() {
        if (instance == null) {
            instance = new AlarmService();
        }
        return instance;
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        System.out.println("✅ Alarm added: " + alarm);
    }

    public void updateAlarm(Alarm alarm) {
        System.out.println("🔄 Alarm updated: " + alarm);
    }

    public void removeAlarm(Alarm alarm) {
        alarms.remove(alarm);
        System.out.println("❌ Alarm removed: " + alarm);
    }

    public List<Alarm> getAllAlarms() {
        return new ArrayList<>(alarms);
    }

    private void startAlarmChecker() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAlarms();
            }
        }, 0, 30000);
    }

    private void checkAlarms() {
        LocalTime now = LocalTime.now();
        LocalTime currentTime = LocalTime.of(now.getHour(), now.getMinute());

        for (Alarm alarm : alarms) {
            if (alarm.isActive() && alarm.getTime().equals(currentTime)) {
                triggerAlarm(alarm);
            }
        }
    }

    private void triggerAlarm(Alarm alarm) {
        Platform.runLater(() -> {
            System.out.println("⏰ ALARM TRIGGERED! " + alarm.getName());
            playAlarmSound(alarm.getSound());
            showAlarmNotification(alarm);
        });
    }

    private void playAlarmSound(String soundName) {
        try {
            String soundPath = "/org/sleeptracker/media/" + getSoundFileName(soundName) + ".mp3";
            System.out.println("🔊 Trying to play: " + soundPath);

            audioPlayer.stop();
            audioPlayer.play(soundPath);

            System.out.println("✅ Alarm sound playing!");

        } catch (Exception e) {
            System.err.println("❌ Error playing alarm sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSoundFileName(String soundName) {
        return switch (soundName) {
            case "Classic Alarm" -> "classic_alarm";
            case "Gentle Wake" -> "gentle_wake";
            case "Rooster" -> "Rooster sound";
            case "Digital Beep" -> "digital_beep";
            case "Chimes" -> "chimes";
            case "Morning Birds" -> "morning_birds";
            case "Soft Piano" -> "soft_piano";
            default -> "classic_alarm";
        };
    }

    private void showAlarmNotification(Alarm alarm) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("⏰ Alarm!");
        alert.setHeaderText(alarm.getName());
        alert.setContentText("Time: " + alarm.getTimeFormatted());

        ButtonType stopButton = new ButtonType("Stop");
        ButtonType snoozeButton = new ButtonType("Snooze 5 min");

        alert.getButtonTypes().setAll(stopButton, snoozeButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == stopButton) {
                stopAlarm();
            } else if (response == snoozeButton) {
                snoozeAlarm(alarm);
            }
        });
    }

    private void stopAlarm() {
        audioPlayer.stop();
        System.out.println("⏹️ Alarm stopped");
    }

    private void snoozeAlarm(Alarm alarm) {
        audioPlayer.stop();

        LocalTime snoozeTime = alarm.getTime().plusMinutes(5);
        Alarm snoozeAlarm = new Alarm(
                snoozeTime,
                alarm.getName() + " (Snoozed)",
                alarm.getSound(),
                alarm.getDays(),
                true
        );

        addAlarm(snoozeAlarm);
        System.out.println("😴 Alarm snoozed for 5 minutes");
    }

    public void stopAllAlarms() {
        audioPlayer.stop();
    }
}
package org.sleeptracker.controllers;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.sleeptracker.database.DatabaseManager;
import org.sleeptracker.models.MoodType;
import org.sleeptracker.models.SleepEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsController {

    @FXML private LineChart<String, Number> sleepHoursChart;
    @FXML private BarChart<String, Number> qualityChart;
    @FXML private ComboBox<String> periodComboBox;

    @FXML private Label avgSleepLabel;
    @FXML private Label bestNightLabel;
    @FXML private Label worstNightLabel;
    @FXML private Label totalEntriesLabel;
    @FXML private Label avgQualityLabel;
    @FXML private Label moodAnalysisLabel;

    private DatabaseManager dbManager;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");

    @FXML
    public void initialize() {
        dbManager = new DatabaseManager();

        periodComboBox.getItems().addAll("Last 7 Days", "Last 14 Days", "Last 30 Days", "All Time");
        periodComboBox.setValue("Last 7 Days");
        periodComboBox.setOnAction(e -> loadStats());

        loadStats();

        // ESC tuşu ile kapat
        setupKeyboardShortcuts();
    }

    private void setupKeyboardShortcuts() {
        avgSleepLabel.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                        handleClose();
                    }
                });
            }
        });
    }

    private void loadStats() {
        String period = periodComboBox.getValue();
        int days = switch (period) {
            case "Last 7 Days" -> 7;
            case "Last 14 Days" -> 14;
            case "Last 30 Days" -> 30;
            default -> 365;
        };

        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDate endDate = LocalDate.now();

        List<SleepEntry> entries = dbManager.getEntriesInRange(startDate, endDate);

        if (entries.isEmpty()) {
            showNoDataMessage();
            return;
        }

        updateSleepHoursChart(entries);
        updateQualityChart(entries);
        updateStatistics(entries);
        updateMoodAnalysis(entries);

        // Chart renklerini pembe yap
        applyChartColors();
    }

    private void updateSleepHoursChart(List<SleepEntry> entries) {
        sleepHoursChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sleep Hours");

        for (SleepEntry entry : entries) {
            String date = entry.getDate().format(dateFormatter);
            series.getData().add(new XYChart.Data<>(date, entry.getHoursSlept()));
        }

        sleepHoursChart.getData().add(series);
    }

    private void updateQualityChart(List<SleepEntry> entries) {
        qualityChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sleep Quality");

        for (SleepEntry entry : entries) {
            String date = entry.getDate().format(dateFormatter);
            series.getData().add(new XYChart.Data<>(date, entry.getQuality()));
        }

        qualityChart.getData().add(series);
    }

    private void updateStatistics(List<SleepEntry> entries) {
        double avgSleep = entries.stream()
                .mapToDouble(SleepEntry::getHoursSlept)
                .average()
                .orElse(0.0);
        avgSleepLabel.setText(String.format("%.1f hours", avgSleep));

        SleepEntry bestNight = entries.stream()
                .max((e1, e2) -> Double.compare(e1.getHoursSlept(), e2.getHoursSlept()))
                .orElse(null);
        if (bestNight != null) {
            bestNightLabel.setText(String.format("%.1f hours (%s)",
                    bestNight.getHoursSlept(),
                    bestNight.getDate().format(dateFormatter)));
        }

        SleepEntry worstNight = entries.stream()
                .min((e1, e2) -> Double.compare(e1.getHoursSlept(), e2.getHoursSlept()))
                .orElse(null);
        if (worstNight != null) {
            worstNightLabel.setText(String.format("%.1f hours (%s)",
                    worstNight.getHoursSlept(),
                    worstNight.getDate().format(dateFormatter)));
        }

        totalEntriesLabel.setText(String.valueOf(entries.size()));

        double avgQuality = entries.stream()
                .mapToInt(SleepEntry::getQuality)
                .average()
                .orElse(0.0);
        avgQualityLabel.setText(String.format("%.1f / 5 ⭐", avgQuality));
    }

    private void updateMoodAnalysis(List<SleepEntry> entries) {
        Map<MoodType, Double> moodToAvgSleep = new HashMap<>();
        Map<MoodType, Integer> moodCount = new HashMap<>();

        for (SleepEntry entry : entries) {
            MoodType mood = entry.getMood();
            moodToAvgSleep.merge(mood, entry.getHoursSlept(), Double::sum);
            moodCount.merge(mood, 1, Integer::sum);
        }

        for (MoodType mood : moodToAvgSleep.keySet()) {
            double total = moodToAvgSleep.get(mood);
            int count = moodCount.get(mood);
            moodToAvgSleep.put(mood, total / count);
        }

        MoodType bestMood = moodToAvgSleep.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        MoodType worstMood = moodToAvgSleep.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (bestMood != null && worstMood != null) {
            double bestAvg = moodToAvgSleep.get(bestMood);
            double worstAvg = moodToAvgSleep.get(worstMood);

            moodAnalysisLabel.setText(String.format(
                    "You sleep best when %s (%.1f hrs avg)\nYou sleep less when %s (%.1f hrs avg)",
                    bestMood.toString().toLowerCase(), bestAvg,
                    worstMood.toString().toLowerCase(), worstAvg
            ));
        } else {
            moodAnalysisLabel.setText("Add more entries to see mood analysis");
        }
    }

    private void showNoDataMessage() {
        avgSleepLabel.setText("No data");
        bestNightLabel.setText("No data");
        worstNightLabel.setText("No data");
        totalEntriesLabel.setText("0");
        avgQualityLabel.setText("No data");
        moodAnalysisLabel.setText("Start tracking your sleep to see statistics!");
    }

    private void applyChartColors() {
        Platform.runLater(() -> {
            // Line Chart - Pembe çizgi ve noktalar
            sleepHoursChart.lookupAll(".chart-series-line").forEach(node ->
                    node.setStyle("-fx-stroke: #e895d8; -fx-stroke-width: 4px;")
            );

            sleepHoursChart.lookupAll(".chart-line-symbol").forEach(node ->
                    node.setStyle("-fx-background-color: #e895d8, white; -fx-background-radius: 8px;")
            );

            // Bar Chart - Pembe barlar
            qualityChart.lookupAll(".chart-bar").forEach(node ->
                    node.setStyle("-fx-bar-fill: #e895d8;")
            );
        });
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) avgSleepLabel.getScene().getWindow();
        stage.close();
    }
}
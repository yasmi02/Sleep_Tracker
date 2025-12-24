package org.sleeptracker.models;

public enum MoodType {
    VERY_SAD("😢", "Very Sad"),
    SAD("😕", "Sad"),
    NEUTRAL("😐", "Neutral"),
    HAPPY("🙂", "Happy"),
    VERY_HAPPY("😊", "Very Happy"),
    EXCITED("😄", "Excited");

    private final String emoji;
    private final String description;

    MoodType(String emoji, String description) {
        this.emoji = emoji;
        this.description = description;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return emoji + " " + description;
    }
}
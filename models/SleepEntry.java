package org.sleeptracker.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

public class SleepEntry {
    private int id;
    private LocalDate date;
    private LocalTime bedTime;
    private LocalTime wakeTime;
    private int quality;
    private MoodType mood;
    private String notes;

    public SleepEntry() {
        this.date = LocalDate.now();
        this.quality = 3;
        this.mood = MoodType.NEUTRAL;
    }

    public SleepEntry(LocalDate date, LocalTime bedTime, LocalTime wakeTime, int quality, MoodType mood) {
        this.date = date;
        this.bedTime = bedTime;
        this.wakeTime = wakeTime;
        this.quality = quality;
        this.mood = mood;
    }

    public double getHoursSlept() {
        if (bedTime == null || wakeTime == null) return 0;

        Duration duration;
        if (wakeTime.isBefore(bedTime)) {
            duration = Duration.between(bedTime, LocalTime.MAX)
                    .plus(Duration.between(LocalTime.MIN, wakeTime))
                    .plusMinutes(1);
        } else {
            duration = Duration.between(bedTime, wakeTime);
        }

        return duration.toHours() + (duration.toMinutesPart() / 60.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getBedTime() {
        return bedTime;
    }

    public void setBedTime(LocalTime bedTime) {
        this.bedTime = bedTime;
    }

    public LocalTime getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(LocalTime wakeTime) {
        this.wakeTime = wakeTime;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = Math.max(1, Math.min(5, quality));
    }

    public MoodType getMood() {
        return mood;
    }

    public void setMood(MoodType mood) {
        this.mood = mood;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f hours (Quality: %d/5, Mood: %s)",
                date, getHoursSlept(), quality, mood);
    }
}
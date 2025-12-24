package org.sleeptracker.models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Alarm {
    private LocalTime time;
    private String name;
    private String sound;
    private List<String> days;
    private boolean active;

    public Alarm(LocalTime time, String name, String sound, List<String> days, boolean active) {
        this.time = time;
        this.name = name;
        this.sound = sound;
        this.days = days;
        this.active = active;
    }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSound() { return sound; }
    public void setSound(String sound) { this.sound = sound; }

    public List<String> getDays() { return days; }
    public void setDays(List<String> days) { this.days = days; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getTimeFormatted() {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", getTimeFormatted(), name,
                active ? "Active" : "Inactive");
    }
}

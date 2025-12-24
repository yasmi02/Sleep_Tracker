package org.sleeptracker.database;

import org.sleeptracker.models.MoodType;
import org.sleeptracker.models.SleepEntry;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:sleep_tracker.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS sleep_entries (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                bed_time TEXT NOT NULL,
                wake_time TEXT NOT NULL,
                quality INTEGER NOT NULL,
                mood TEXT NOT NULL,
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✅ Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveSleepEntry(SleepEntry entry) {
        String insertSQL = """
            INSERT INTO sleep_entries (date, bed_time, wake_time, quality, mood, notes)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, entry.getDate().toString());
            pstmt.setString(2, entry.getBedTime().toString());
            pstmt.setString(3, entry.getWakeTime().toString());
            pstmt.setInt(4, entry.getQuality());
            pstmt.setString(5, entry.getMood().name());
            pstmt.setString(6, entry.getNotes());

            pstmt.executeUpdate();
            System.out.println("✅ Sleep entry saved!");
        } catch (SQLException e) {
            System.err.println("❌ Error saving entry: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<SleepEntry> getRecentEntries(int limit) {
        List<SleepEntry> entries = new ArrayList<>();
        String selectSQL = """
            SELECT * FROM sleep_entries 
            ORDER BY date DESC, created_at DESC 
            LIMIT ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SleepEntry entry = new SleepEntry();
                entry.setId(rs.getInt("id"));
                entry.setDate(LocalDate.parse(rs.getString("date")));
                entry.setBedTime(LocalTime.parse(rs.getString("bed_time")));
                entry.setWakeTime(LocalTime.parse(rs.getString("wake_time")));
                entry.setQuality(rs.getInt("quality"));
                entry.setMood(MoodType.valueOf(rs.getString("mood")));
                entry.setNotes(rs.getString("notes"));

                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error loading entries: " + e.getMessage());
            e.printStackTrace();
        }

        return entries;
    }

    public List<SleepEntry> getEntriesInRange(LocalDate startDate, LocalDate endDate) {
        List<SleepEntry> entries = new ArrayList<>();
        String selectSQL = """
            SELECT * FROM sleep_entries 
            WHERE date BETWEEN ? AND ?
            ORDER BY date ASC
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SleepEntry entry = new SleepEntry();
                entry.setId(rs.getInt("id"));
                entry.setDate(LocalDate.parse(rs.getString("date")));
                entry.setBedTime(LocalTime.parse(rs.getString("bed_time")));
                entry.setWakeTime(LocalTime.parse(rs.getString("wake_time")));
                entry.setQuality(rs.getInt("quality"));
                entry.setMood(MoodType.valueOf(rs.getString("mood")));
                entry.setNotes(rs.getString("notes"));

                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public double getAverageSleepHours(int days) {
        String sql = """
            SELECT AVG(
                (julianday(wake_time) - julianday(bed_time)) * 24
            ) as avg_hours
            FROM sleep_entries
            WHERE date >= date('now', '-' || ? || ' days')
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, days);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_hours");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void deleteEntry(int id) {
        String deleteSQL = "DELETE FROM sleep_entries WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
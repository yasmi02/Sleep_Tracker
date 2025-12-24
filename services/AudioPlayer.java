package org.sleeptracker.services;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class AudioPlayer {
    private static AudioPlayer instance;
    private Player player;
    private Thread playerThread;
    private boolean isPlaying = false;

    private AudioPlayer() {}

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    public void play(String soundPath) {
        stop();

        try {
            InputStream audioSrc = getClass().getResourceAsStream(soundPath);
            if (audioSrc == null) {
                System.err.println("❌ Sound file not found: " + soundPath);
                return;
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            player = new Player(bufferedIn);
            isPlaying = true;

            playerThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    System.err.println("❌ Error playing sound: " + e.getMessage());
                }
            });
            playerThread.start();

            System.out.println("▶️ Playing: " + soundPath);

        } catch (Exception e) {
            System.err.println("❌ Error loading sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        if (player != null) {
            player.close();
            isPlaying = false;
            System.out.println("⏹️ Sound stopped");
        }
    }

    public void setVolume(int volume) {
        System.out.println("🔊 Volume: " + volume);
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
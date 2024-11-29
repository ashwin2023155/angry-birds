package com.angrybirds.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
    private Music backgroundMusic;
    private Sound effectSound;
    private float musicVolume = 0.5f;
    private float soundVolume = 0.7f;
    private boolean isMusicEnabled = true;
    private boolean areSoundEffectsEnabled = true;
    private boolean isPaused = false;

    public MusicManager() {
        // Load background music
        try {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));
            backgroundMusic.setLooping(true);
        } catch (Exception e) {
            Gdx.app.error("MusicManager", "Failed to load background music", e);
        }

        // Load a sample sound effect
        try {
            effectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bird_launch.wav"));
        } catch (Exception e) {
            Gdx.app.error("MusicManager", "Failed to load sound effect", e);
        }
    }

    public void playBackgroundMusic() {
        if (isMusicEnabled && backgroundMusic != null) {
            backgroundMusic.play();
            backgroundMusic.setVolume(musicVolume);
            isPaused = false; // Update state
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            isPaused = false; // Reset state as music is fully stopped
        }
    }

    public void pauseBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
            isPaused = true; // Update state
        }
    }

    public void playSoundEffect() {
        if (areSoundEffectsEnabled && effectSound != null) {
            effectSound.play(soundVolume);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0f, Math.min(1f, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume);
        }
    }

    public void setSoundEffectVolume(float volume) {
        this.soundVolume = Math.max(0f, Math.min(1f, volume));
    }

    public void toggleMusic() {
        isMusicEnabled = !isMusicEnabled;
        if (isMusicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    public void toggleSoundEffects() {
        areSoundEffectsEnabled = !areSoundEffectsEnabled;
    }

    // New method to check if music is currently playing
    public boolean isPlaying() {
        return backgroundMusic != null && backgroundMusic.isPlaying();
    }

    // New method to check if music is paused
    public boolean isPaused() {
        return isPaused;
    }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
        if (effectSound != null) {
            effectSound.dispose();
        }
    }
}

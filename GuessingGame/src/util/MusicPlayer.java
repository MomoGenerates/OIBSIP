package util;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer extends Thread {
    private final String filePath;
    private Clip clip;
    private boolean isPaused = false;
    private final boolean shouldLoop;
    private static MusicPlayer currentBackgroundMusic = null;

    public MusicPlayer(String filePath, boolean shouldLoop) {
        this.filePath = filePath;
        this.shouldLoop = shouldLoop;
    }

    public MusicPlayer(String filePath) {
        this(filePath, true); 
    }

    @Override
    public void run() {
        try {
            File musicFile = new File(filePath);
            if (!musicFile.exists()) {
                System.out.println("Music file not found");
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            if (shouldLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                currentBackgroundMusic = this;
            } else if (currentBackgroundMusic != null) {
                currentBackgroundMusic.pauseMusic();
            }
            
            clip.start();

            // Add LineListener to handle one-time playback completion
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP && !shouldLoop) {
                    clip.close();
                    if (currentBackgroundMusic != null) {
                        currentBackgroundMusic.resumeMusic();
                    }
                }
            });

            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (isPaused) {
                        wait();
                    }
                }
                TimeUnit.MILLISECONDS.sleep(100);
                
                // Exit thread if one-time playback is complete
                if (!shouldLoop && !clip.isRunning()) {
                    break;
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }

    public synchronized void pauseMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPaused = true;
        }
    }

    public synchronized void resumeMusic() {
        if (clip != null && isPaused) {
            if (shouldLoop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);  
            }
            clip.start();
            isPaused = false;
            notify(); 
        }
    }

    public void stopMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
            interrupt(); 
        }
    }
}
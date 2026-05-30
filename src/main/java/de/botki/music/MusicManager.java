package de.botki.music;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicManager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    private static MusicManager instance;

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void playTrack(String songName) {
        logger.info("Playing track: {}", songName);
        // Lavaplayer implementation
    }

    public void stopTrack() {
        logger.info("Stopping current track");
    }

    public void pauseTrack() {
        logger.info("Pausing current track");
    }

    public void resumeTrack() {
        logger.info("Resuming current track");
    }
}
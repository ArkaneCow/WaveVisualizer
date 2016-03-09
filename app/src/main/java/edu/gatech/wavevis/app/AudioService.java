package edu.gatech.wavevis.app;

public class AudioService {
    private static AudioService ourInstance;

    public static AudioService getInstance() {
        if (ourInstance == null) {
            ourInstance = new AudioService();
        }
        return ourInstance;
    }

    private AudioService() {
    }
}

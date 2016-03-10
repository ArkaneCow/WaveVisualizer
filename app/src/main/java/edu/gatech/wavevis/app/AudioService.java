package edu.gatech.wavevis.app;

import be.tarsos.dsp.AudioDispatcher;

public class AudioService {
    private static AudioService ourInstance;

    private final int SAMPLE_RATE = 8000;
    private final int BUFFER_SIZE = 1024;

    private AudioDispatcher dispatcher;

    private Thread dispatcherThread;

    public static AudioService getInstance() {
        if (ourInstance == null) {
            ourInstance = new AudioService();
        }
        return ourInstance;
    }



    private AudioService() {

    }
}

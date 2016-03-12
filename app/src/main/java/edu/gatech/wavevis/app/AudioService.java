package edu.gatech.wavevis.app;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;

public class AudioService {
    private static AudioService ourInstance;

    private final int SAMPLE_RATE = 8000;
    private final int BUFFER_SIZE = 1024;

    private AudioDispatcher dispatcher;

    private Thread dispatcherThread;

    public int getSampleRate() {
        return SAMPLE_RATE;
    }

    public int getBufferSize() {
        return BUFFER_SIZE;
    }

    public static AudioService getInstance() {
        if (ourInstance == null) {
            ourInstance = new AudioService();
        }
        return ourInstance;
    }

    private void startThread() {
        if (!dispatcherThread.isAlive()) {
            dispatcherThread.start();
        }
    }

    private void stopThread() {
        if (dispatcherThread.isAlive()) {
            dispatcherThread.stop();
        }
    }

    public AudioDispatcher getAudioDispatcher() {
        return dispatcher;
    }

    private AudioService() {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, 0);
        dispatcherThread = new Thread(dispatcher);
        startThread();
    }
}

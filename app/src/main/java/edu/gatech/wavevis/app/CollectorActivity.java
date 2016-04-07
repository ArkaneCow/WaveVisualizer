package edu.gatech.wavevis.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.writer.WaveHeader;
import be.tarsos.dsp.writer.WriterProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CollectorActivity extends Activity {

    enum RecordingState {
        STOP,
        START,
        PAUSE,;
        @Override
        public String toString() {
            switch (this) {
                case START:
                    return "Start";
                case STOP:
                    return "Stop";
                case PAUSE:
                    return "Pause";
                default:
                    throw new IllegalArgumentException("Invalid RecordingState");
            }
        }
    }

    private Button startButton;
    private TextView promptText;

    private RecordingState recordingState;

    private static final int PAUSE_TIME = 3000;

    private LabelTest[] testLabels = { new LabelTest(LabelState.BREATHING, 2000), new LabelTest(LabelState.LEFT_BLOW, 2000), new LabelTest(LabelState.RIGHT_BLOW, 2000), new LabelTest(LabelState.TALKING, 2000) };
    private LabelData[] testResults;

    private LabelData currentLabelData;

    private AudioDispatcher dispatcher;

    private int labelIndex = 0;

    private Handler stateHandler = new Handler();

    private Runnable statePauser = new Runnable() {
        @Override
        public void run() {
            if (labelIndex < testLabels.length) {
                currentLabelData = null;
                LabelTest currentTest = testLabels[labelIndex];
                setPromptText("Up Next: " + currentTest.getLabelState().toString());
                stateHandler.postDelayed(stateChanger, PAUSE_TIME);
            } else {
                stopSession();
            }
        }
    };

    private Runnable stateChanger = new Runnable() {
        @Override
        public void run() {
            LabelTest currentTest = testLabels[labelIndex];
            setPromptText(currentTest.getLabelState().toString());
            LabelData lData = new LabelData(getApplicationContext(), currentTest.getLabelState());
            testResults[labelIndex] = lData;
            currentLabelData = lData;
            labelIndex++;
            stateHandler.postDelayed(statePauser, currentTest.getTestLength());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        startButton = (Button) findViewById(R.id.startButton);
        promptText = (TextView) findViewById(R.id.promptText);
        recordingState = RecordingState.STOP;
        testResults = new LabelData[testLabels.length];
        dispatcher = AudioService.getInstance().getAudioDispatcher();
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                final float[] audioFloatBuffer = audioEvent.getFloatBuffer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentLabelData != null) {
                            NVector frameVector = new NVector(audioFloatBuffer);
                            currentLabelData.getLabelData().add(frameVector);
                        }
                    }
                });
                return true;
            }

            @Override
            public void processingFinished() {

            }
        });

        dispatcher.addAudioProcessor(new AudioProcessor() {
            private TarsosDSPAudioFormat audioFormat = dispatcher.getFormat();

            @Override
            public boolean process(AudioEvent audioEvent) {
                try {
                    if (currentLabelData != null) {
                        Log.v("cldbp", "" + currentLabelData.getLabelWav().getFilePointer());
                        currentLabelData.setWavDataLength(currentLabelData.getWavDataLength() + audioEvent.getByteBuffer().length);
                        currentLabelData.getLabelWav().write(audioEvent.getByteBuffer());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void processingFinished() {

            }
        });
    }

    private void setPromptText(String text) {
        promptText.setText(text);
    }

    private void setStartButtonText(String text) {
        startButton.setText(text);
    }

    private void startSession() {
        recordingState = RecordingState.START;
        labelIndex = 0;
        setStartButtonText("Stop");
        stateHandler = new Handler();
        stateHandler.postDelayed(stateChanger, 0);
    }

    private class SaveLabelTask extends AsyncTask<Void, Integer,
            Object> {
        @Override
        protected void onPreExecute() {
            setPromptText("Saving...");
        }

        @Override
        protected Object doInBackground(Void... params) {
            TarsosDSPAudioFormat audioFormat = dispatcher.getFormat();
            for (LabelData ld : testResults) {
                WaveHeader waveHeader = new WaveHeader(WaveHeader.FORMAT_PCM, (short) audioFormat.getChannels(), (int) audioFormat.getSampleRate(),(short) 16, ld.getWavDataLength());
                ByteArrayOutputStream header = new ByteArrayOutputStream();
                try {
                    waveHeader.write(header);
                    ld.getLabelWav().seek(0);
                    ld.getLabelWav().write(header.toByteArray());
                    ld.getLabelWav().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (LabelData result : testResults) {
                if (result != null) {
                    result.writeToFile();
                    Log.v("result", "result written to file");
                } else {
                    Log.v("result", "result is null");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            setPromptText("None");
        }
    }

    private void stopSession() {
        recordingState = RecordingState.STOP;
        currentLabelData = null;
        SaveLabelTask saveTask = new SaveLabelTask();
        saveTask.execute();
        setStartButtonText("Start");
    }

    public void recordStart(View v) {
        switch (recordingState) {
            case START:
                stopSession();
                break;
            case STOP:
                startSession();
                break;
            default:
                break;
        }
        Log.v("RecordingState", recordingState.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

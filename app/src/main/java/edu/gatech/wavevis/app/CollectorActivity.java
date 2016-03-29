package edu.gatech.wavevis.app;

import android.app.Activity;
import android.graphics.Bitmap;
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
import be.tarsos.dsp.util.fft.FFT;

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

    private LabelTest[] testLabels = { new LabelTest(LabelState.BREATHING, 1000), new LabelTest(LabelState.LEFT_BLOW, 1000), new LabelTest(LabelState.RIGHT_BLOW, 1000), new LabelTest(LabelState.TALKING, 1000) };
    private LabelData[] testResults;

    private LabelData currentLabelData;

    private AudioDispatcher dispatcher;

    private int labelIndex = 0;

    private Handler stateHandler = new Handler();

    private Runnable stateChanger = new Runnable() {
        @Override
        public void run() {
            if (labelIndex < testLabels.length) {
                LabelTest currentTest = testLabels[labelIndex];
                promptText.setText(currentTest.getLabelState().toString());
                LabelData lData = new LabelData(currentTest.getLabelState());
                testResults[labelIndex] = lData;
                currentLabelData = lData;
                labelIndex++;
                stateHandler.postDelayed(this, currentTest.getTestLength());
            } else {
                stopSession();
            }
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
        final int audioBufferSize = AudioService.getInstance().getBufferSize();
        dispatcher.addAudioProcessor(new AudioProcessor() {
            FFT fft = new FFT(audioBufferSize);
            float[] amplitudes = new float[audioBufferSize / 2];
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] audioFloatBuffer = audioEvent.getFloatBuffer();
                float[] transformBuffer = new float[audioBufferSize * 2];
                System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);
                fft.forwardTransform(transformBuffer);
                fft.modulus(transformBuffer, amplitudes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentLabelData != null) {
                            NVector frameVector = new NVector(amplitudes);
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

    }

    private void setPromptText(String text) {
        promptText.setText(text);
    }

    private void setStartButtonText(String text) {
        startButton.setText(text);
    }

    private void startSession() {
        recordingState = RecordingState.START;
        setStartButtonText("Stop");
        stateHandler = new Handler();
        stateHandler.postDelayed(stateChanger, 0);
    }

    private void stopSession() {
        recordingState = RecordingState.STOP;
        for (LabelData result : testResults) {
            if (result != null) {
                result.writeToFile(this);
            } else {
                Log.v("result", "result is null");
            }
        }
        setStartButtonText("Start");
        setPromptText("None");
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

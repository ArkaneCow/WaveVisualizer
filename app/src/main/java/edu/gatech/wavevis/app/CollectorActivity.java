package edu.gatech.wavevis.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.writer.WaveHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private TextView countText;
	private int audioLen=0;

    private RecordingState recordingState;

	private ArrayList<GestureRecording> gestureRecordingSet;
	private List<Integer> trainingIndices = new ArrayList<Integer>();
	public 	List<String> trainingSet = new ArrayList<String>();
	private Gesture[] gestureCollection;

    private Gesture currentGesture;

    private AudioDispatcher dispatcher;

    private int labelIndex = 0;

    private Handler stateHandler = new Handler();

	public static long session_timestamp;

    private Runnable statePauser = new Runnable() {
        @Override
        public void run() {
            if (labelIndex < gestureRecordingSet.size()) {
                currentGesture = null;
                GestureRecording currentTest = gestureRecordingSet.get(labelIndex);
                setPromptText(currentTest.getGestureRecordIcon());
				setPromptColor(Color.WHITE);
                stateHandler.postDelayed(stateChanger, Config.PAUSE_TIME);
            } else {
                stopSession();
            }
        }
    };

    private Runnable stateChanger = new Runnable() {
        @Override
        public void run() {
            GestureRecording currentTest = gestureRecordingSet.get(labelIndex);
            setPromptText(currentTest.getGestureRecordIcon());
			setPromptColor(Color.GREEN);
			setCountText("# " + Integer.toString(currentTest.getGestureSetIndex()));
            Gesture lData = new Gesture(getApplicationContext(), currentTest.getGestureRecordName(), currentTest.getGestureRecordCount(), currentTest.getGestureSetIndex());
            gestureCollection[labelIndex] = lData;
            currentGesture = lData;
            labelIndex++;
            stateHandler.postDelayed(statePauser, currentTest.getGestureRecordLength());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);
        startButton = (Button) findViewById(R.id.startButton);
        promptText = (TextView) findViewById(R.id.promptText);
		countText = (TextView) findViewById(R.id.countText);
        recordingState = RecordingState.STOP;
		session_timestamp = System.currentTimeMillis();

		beginSessionPrep();
        dispatcher = AudioService.getInstance().getAudioDispatcher();
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                final float[] audioFloatBuffer = audioEvent.getFloatBuffer();
				if (currentGesture != null) {
					NVector frameVector = new NVector(audioFloatBuffer);
					currentGesture.getGestureData().add(frameVector);
				}
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
                    if (currentGesture != null) {
                        //Log.v("cldbp", "" + currentGesture.getGestureWav().getFilePointer());
                        currentGesture.setGestureWavLength(currentGesture.getGestureWavLength() + audioEvent.getByteBuffer().length);
                        currentGesture.getGestureWav().write(audioEvent.getByteBuffer());
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

	private void beginSessionPrep() {
		generateLabels();
		gestureCollection = new Gesture[gestureRecordingSet.size()];
		GestureRecording currentTest = gestureRecordingSet.get(labelIndex);
		setPromptText(currentTest.getGestureRecordIcon());
		setPromptColor(Color.WHITE);
		setCountText("");
	}

	private void generateLabels() {
		gestureRecordingSet = new ArrayList<GestureRecording>();
		for(int i=0; i< Config.NUM_GESTURES; i++)
		{
			for(int j=0; j<Config.GESTURE_MAP.size(); j++)
			{
				trainingIndices.add(j);
			}
		}
		if(Config.RANDOMIZE_SET) Collections.shuffle(trainingIndices);

		List keys = new ArrayList(Config.GESTURE_MAP.keySet());
		List values = new ArrayList(Config.GESTURE_MAP.values());

		Log.d(Config.TAG, keys.toString());
		Log.d(Config.TAG, values.toString());
		Log.d(Config.TAG, trainingIndices.toString());

		for(int i=0; i<trainingIndices.size(); i++)
		{
			gestureRecordingSet.add(new GestureRecording(keys.get(trainingIndices.get(i)).toString(), values.get(trainingIndices.get(i)).toString(), Config.RECORD_TIME, getCurrentGestureCount(i), i+1));
		}
	}

	public Integer getCurrentGestureCount(int setIndex) {
		int currentGestureCount = 0;

		for(int i=0; i<setIndex; i++)
		{
			if(trainingIndices.get(i) == trainingIndices.get(setIndex))
			{
				currentGestureCount++;
			}
		}
		return currentGestureCount+1;
	}

	private void setPromptText(String text) {
        promptText.setText(text);
    }

	private void setPromptColor(int color) { promptText.setTextColor(color); }

	private void setCountText(String text) { countText.setText(text); }

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

	private void saveDataToFile() {
		for(Gesture ld : gestureCollection) {
			SaveLabelTask saveTask = new SaveLabelTask();
			saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ld);
		}
	}

    private class SaveLabelTask extends AsyncTask<Object, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Object... params) {
			TarsosDSPAudioFormat audioFormat = dispatcher.getFormat();
			Gesture audioGesture = (Gesture) params[0];
//			Log.d(Config.TAG, audioGesture.getGestureName().toString());
			Log.d(Config.TAG, audioGesture.baseFileName());
//			Log.d(Config.TAG, Integer.toString(audioGesture.getGestureData().size()));
			WaveHeader waveHeader = new WaveHeader(WaveHeader.FORMAT_PCM, (short) audioFormat.getChannels(), (int) audioFormat.getSampleRate(),(short) 16, audioGesture.getGestureWavLength());
			ByteArrayOutputStream header = new ByteArrayOutputStream();
			try {
				waveHeader.write(header);
				if (audioGesture != null) {
					audioGesture.getGestureWav().seek(0);
					audioGesture.getGestureWav().write(header.toByteArray());
					audioGesture.getGestureWav().close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return "Done";
		}

        @Override
        protected void onPostExecute(String result) {
			setPromptColor(Color.WHITE);
			setPromptText(result);
        }
    }

    private void stopSession() {
        recordingState = RecordingState.STOP;
        currentGesture = null;
		saveDataToFile();
		beginSessionPrep();
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

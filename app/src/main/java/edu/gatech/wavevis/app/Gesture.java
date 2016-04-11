package edu.gatech.wavevis.app;

import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gesture {
	private String gestureName;
	private List<NVector> gestureData;
	private RandomAccessFile gestureWav;
	private int gestureWavLength = 0;
	private int gestureCount = 0;
	private int gestureSetCount = 0;

	private File mRootDir;
	private String mStorageState = Environment.getExternalStorageState();
	public static final String LOG_FOLDER_PATH = "whoosh";
	private String sFilePath;

	private Context context;

	private static final int HEADER_LENGTH = 44; // length of a WAV header

	public Gesture(Context context, String gestureName, int gestureCount, int gestureSetCount) {
		this(context, gestureName, new ArrayList<NVector>(), gestureCount, gestureSetCount);
	}

	public String baseFileName() {

		if (Environment.MEDIA_MOUNTED.equals(mStorageState)) {
			try {
				mRootDir = Environment.getExternalStorageDirectory();
				sFilePath = mRootDir + "/" + LOG_FOLDER_PATH + "/" + millis2Date(CollectorActivity.session_timestamp);

				File folder = new File(sFilePath);
				boolean success = false;

				if (!folder.exists()) {
					success = folder.mkdirs();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String baseFileName = sFilePath + "/" + Config.DEVICE + "_" + "audio" + "_" + gestureName + "_" + gestureCount + "_" + String.format("%03d", gestureSetCount);

		//Log.d("output", baseFileName);
		return baseFileName;
	}

	public int getGestureWavLength() {
		return gestureWavLength;
	}

	public void setGestureWavLength(int gestureWavLength) {
		this.gestureWavLength = gestureWavLength;
	}

	public Gesture(Context context, String gestureName, List<NVector> gestureData, int gestureCount, int gestureSetCount) {
		this.gestureName = gestureName;
		this.gestureData = gestureData;
		this.gestureCount = gestureCount;
		this.gestureSetCount = gestureSetCount;
		this.context = context;
		try {
			gestureWav = new RandomAccessFile(new File(baseFileName() + ".wav"), "rw");
			gestureWav.write(new byte[HEADER_LENGTH]);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RandomAccessFile getGestureWav() {
		Log.d(Config.TAG, baseFileName());
		if (gestureWav == null) {
			try {
				gestureWav = new RandomAccessFile(new File(baseFileName() + ".wav"), "rw");
				gestureWav.write(new byte[HEADER_LENGTH]);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gestureWav;
	}

	public List<NVector> getGestureData() {
		return gestureData;
	}

	public String getGestureName() {
		return gestureName;
	}

	public static String millis2Date(long timestamp) {
		Date date = new Date(timestamp);
		String strdate = DateFormat.format("yyyy-MM-dd/HH-mm-ss", date).toString();
		return strdate;
	}
}

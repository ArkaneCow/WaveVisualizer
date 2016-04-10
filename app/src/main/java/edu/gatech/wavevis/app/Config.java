package edu.gatech.wavevis.app;

import java.util.LinkedHashMap;

/**
 * Created by gareyes on 4/9/16.
 */
public class Config {
	public static final String TAG = "whoosh";
	public static final String DEVICE = "watch";
	public static final int SAMPLERATE_AUDIO = 48000;

	public static final LinkedHashMap<String, String> GESTURE_MAP;
	static {
		GESTURE_MAP = new LinkedHashMap<String, String>();
		GESTURE_MAP.put("leftswipe", "⬅");
		GESTURE_MAP.put("rightswipe", "➡");
		GESTURE_MAP.put("downswipe", "⬇");
		GESTURE_MAP.put("upswipe", "⬆");
		GESTURE_MAP.put("clockwise", "↻");
		GESTURE_MAP.put("counterclockwise", "↺");
	}

	public static final int NUM_GESTURES = 5;
	public static final int RECORD_TIME = 1500; // milliseconds
	public static final int PAUSE_TIME = 1000; // milliseconds
	public static final boolean RANDOMIZE_SET = true;
}

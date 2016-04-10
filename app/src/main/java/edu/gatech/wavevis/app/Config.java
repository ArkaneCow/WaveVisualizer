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
		GESTURE_MAP.put("blowleft", "left");
		GESTURE_MAP.put("blowright", "right");
		GESTURE_MAP.put("blowbottom", "bottom");
		GESTURE_MAP.put("blowtop", "top");
		GESTURE_MAP.put("clockwise", "↻");
		GESTURE_MAP.put("counterclockwise", "↺");
		GESTURE_MAP.put("doubleblow", "double");
		GESTURE_MAP.put("shortblow", "short");
		GESTURE_MAP.put("shoosh", "shoosh");
		GESTURE_MAP.put("open", "open");
		GESTURE_MAP.put("sip", "sip");
		GESTURE_MAP.put("puff", "puff");
		GESTURE_MAP.put("longblow", "longblow");
	}

	public static final int NUM_GESTURES = 5;
	public static final int RECORD_TIME = 2000; // milliseconds
	public static final int PAUSE_TIME = 1000; // milliseconds
	public static final boolean RANDOMIZE_SET = true;
}

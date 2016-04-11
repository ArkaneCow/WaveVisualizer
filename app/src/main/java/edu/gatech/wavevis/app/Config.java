package edu.gatech.wavevis.app;

import java.util.LinkedHashMap;

/**
 * Created by gareyes on 4/9/16.
 */
public class Config {
	public static final String TAG = "whoosh";
	public static final String DEVICE = "watch";
	public static final int SAMPLERATE_AUDIO = 48000;
	public static final boolean WATCH_CASE = true;

	public static final LinkedHashMap<String, String> GESTURE_MAP;
		static {
		GESTURE_MAP = new LinkedHashMap<String, String>();
		if(!WATCH_CASE)
		{
			GESTURE_MAP.put("swipeleft", "⬅⬅");
			GESTURE_MAP.put("swiperight", "➡➡");
			GESTURE_MAP.put("swipedown", "⬇⬇");
			GESTURE_MAP.put("swipeup", "⬆⬆");

			GESTURE_MAP.put("left", "⬅");
			GESTURE_MAP.put("right", "➡");
			GESTURE_MAP.put("top", "⬆");

			GESTURE_MAP.put("clockwise", "↻");
			GESTURE_MAP.put("counterclockwise", "↺");

			GESTURE_MAP.put("doubleblow", "double");
			GESTURE_MAP.put("shortblow", "short");
			GESTURE_MAP.put("shoosh", "shoosh");
			GESTURE_MAP.put("open", "open");
			GESTURE_MAP.put("sip", "sip");
			GESTURE_MAP.put("puff", "puff");
			GESTURE_MAP.put("longblow", "long");
		} else {
			GESTURE_MAP.put("swipeleft", "⬅⬅");
			GESTURE_MAP.put("swiperight", "➡➡");
			GESTURE_MAP.put("swipedown", "⬇⬇");
			GESTURE_MAP.put("swipeup", "⬆⬆");

			GESTURE_MAP.put("clockwise", "↻");
			GESTURE_MAP.put("counterclockwise", "↺");

			GESTURE_MAP.put("topleft", "↖");
			GESTURE_MAP.put("topcenter", "↑");
			GESTURE_MAP.put("topright", "↗");
			GESTURE_MAP.put("right", "➡");
			GESTURE_MAP.put("left", "⬅");
			GESTURE_MAP.put("bottomleft", "↙");
			GESTURE_MAP.put("bottomcenter", "↓");
			GESTURE_MAP.put("bottomright", "↘");
		}
	}

	public static final int NUM_GESTURES = 5;
	public static final int RECORD_TIME = 2000; // milliseconds
	public static final int PAUSE_TIME = 1000; // milliseconds
	public static final boolean RANDOMIZE_SET = true;
}

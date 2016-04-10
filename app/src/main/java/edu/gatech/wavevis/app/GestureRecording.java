package edu.gatech.wavevis.app;

import android.util.Log;

public class GestureRecording {

    private String gestureRecordName;
	private String gestureRecordIcon;
    private int gestureRecordLength;
	private int gestureRecordCount;
	private int gestureSetIndex;

    public GestureRecording(String gestureRecordName, String gestureRecordIcon, int gestureRecordLength, int gestureRecordCount, int gestureSetIndex) {
        this.gestureRecordName = gestureRecordName;
		this.gestureRecordIcon = gestureRecordIcon;
        this.gestureRecordLength = gestureRecordLength;
		this.gestureRecordCount = gestureRecordCount;
		this.gestureSetIndex = gestureSetIndex;

		Log.d(Config.TAG, gestureRecordName + "," + gestureRecordIcon + "," + Integer.toString(gestureRecordLength) + "," + Integer.toString(gestureRecordCount) + "," + Integer.toString(gestureSetIndex));
    }

    public String getGestureRecordName() { return gestureRecordName; }

	public String getGestureRecordIcon() { return gestureRecordIcon; }

    public int getGestureRecordLength() { return gestureRecordLength; }

	public int getGestureRecordCount() { return gestureRecordCount; }

	public int getGestureSetIndex() { return gestureSetIndex; }
}
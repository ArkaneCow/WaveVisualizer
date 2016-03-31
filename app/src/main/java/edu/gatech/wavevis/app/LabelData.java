package edu.gatech.wavevis.app;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LabelData {
    private LabelState labelState;
    private List<NVector> labelData;
    private RandomAccessFile labelWav;
    private int wavDataLength = 0;

    private Context context;

    private static final int HEADER_LENGTH = 44; // length of a WAV header

    public LabelData(Context context, LabelState labelState) {
        this(context, labelState, new ArrayList<NVector>());
    }

    private String baseFileName() {
        return context.getFilesDir() + "/" + labelState.toString() + ("" + System.currentTimeMillis());
    }

    public int getWavDataLength() {
        return wavDataLength;
    }

    public void setWavDataLength(int wavDataLength) {
        this.wavDataLength = wavDataLength;
    }

    public LabelData(Context context, LabelState labelState, List<NVector> labelData) {
        this.labelState = labelState;
        this.labelData = labelData;
        this.context = context;
        try {
            labelWav = new RandomAccessFile(new File(baseFileName() + ".wav"), "rw");
            labelWav.write(new byte[HEADER_LENGTH]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RandomAccessFile getLabelWav() {
        return labelWav;
    }

    public List<NVector> getLabelData() {
        return labelData;
    }

    public LabelState getLabelState() {
        return labelState;
    }

    public void writeToFile() {
        String filePath = baseFileName() + ".txt";
        Log.v("labeldata filepath", filePath);
        try {
            FileOutputStream fop = new FileOutputStream(new File(filePath));
            for (NVector ld : getLabelData()) {
                fop.write((ld.toString() + "\n").getBytes(Charset.forName("UTF-8")));
            }
            fop.flush();
            fop.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

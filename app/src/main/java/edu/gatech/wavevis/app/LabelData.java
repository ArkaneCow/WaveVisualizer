package edu.gatech.wavevis.app;

import android.app.Activity;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LabelData {
    private LabelState labelState;
    private List<NVector> labelData;

    public LabelData(LabelState labelState) {
        this.labelState = labelState;
        this.labelData = new ArrayList<NVector>();
    }

    public LabelData(LabelState labelState, List<NVector> labelData) {
        this.labelState = labelState;
        this.labelData = labelData;
    }

    public List<NVector> getLabelData() {
        return labelData;
    }

    public LabelState getLabelState() {
        return labelState;
    }

    public void writeToFile(Activity activity) {
        String fileName = labelState.toString() + ("" + System.currentTimeMillis()) + ".txt";
        try {
            FileOutputStream fop = new FileOutputStream(new File(activity.getFilesDir() + fileName));
            for (NVector ld : getLabelData()) {
                fop.write(ld.toString().getBytes(Charset.forName("UTF-8")));
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

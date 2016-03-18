package edu.gatech.wavevis.app;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

    public void writeToFile() {
        String fileName = labelState.toString() + ("" + System.currentTimeMillis()) + ".txt";
        try {
            PrintWriter fileWriter = new PrintWriter(fileName, "UTF-8");
            for (NVector vector : labelData) {
                fileWriter.println(vector.toString());
            }
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

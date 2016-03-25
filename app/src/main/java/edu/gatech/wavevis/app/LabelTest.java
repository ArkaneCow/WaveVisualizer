package edu.gatech.wavevis.app;

public class LabelTest {

    private LabelState labelState;
    private int testLength;

    public LabelTest(LabelState labelState, int testLength) {
        this.labelState = labelState;
        this.testLength = testLength;
    }

    public LabelState getLabelState() {
        return labelState;
    }

    public int getTestLength() {
        return testLength;
    }
}

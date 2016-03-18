package edu.gatech.wavevis.app;

public class NVector {
    private float[] vectorData;

    private Float minValue = null;

    private Float maxValue = null;

    public NVector(float[] vectorData) {
        this.vectorData = vectorData;
    }

    public void setVectorData(float[] vectorData) {
        this.vectorData = vectorData;
    }

    public float[] getVectorData() {
        return vectorData;
    }

    private void updateRange() {
        float minVal = vectorData[0];
        float maxVal = vectorData[0];
        for (int i = 0; i < vectorData.length; i++) {
            float currentVal = vectorData[i];
            if (currentVal < minVal) {
                minVal = currentVal;
            }
            if (currentVal > maxVal) {
                maxVal = currentVal;
            }
        }
        minValue = new Float(minVal);
        maxValue = new Float(maxVal);
    }

    public float min() {
        if (minValue == null) {
            updateRange();
        }
        return minValue.floatValue();
    }

    public float max() {
        if (maxValue == null) {
            updateRange();
        }
        return maxValue.floatValue();
    }

    public NVector log(float base) {
        float[] lArray = logArray(vectorData);
        float lBase = (float) Math.log(base);
        for (int i = 0; i < lArray.length; i++) {
            lArray[i] = lArray[i] / lBase;
        }
        return new NVector(lArray);
    }

    private float[] logArray(float[] array) {
        float[] lArray = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            lArray[i] = (float) Math.log(array[i]);
        }
        return lArray;
    }

    public NVector log() {
        return new NVector(logArray(vectorData));
    }

    public NVector log10() {
        return log(10.F);
    }

    public float magnitude() {
        float mag = 0.F;
        for (int i = 0; i < vectorData.length; i++) {
            mag += vectorData[i] * vectorData[i];
        }
        return (float) Math.sqrt((double) mag);
    }

    public int getDimensions() {
        return vectorData.length;
    }

    public float getValue(int index) {
        return vectorData[index];
    }

    public void setValue(int index, float value) {
        vectorData[index] = value;
    }

    public float distanceFrom(NVector v) {
        if (getDimensions() != v.getDimensions()) {
            throw new IllegalArgumentException("incompatible dimensions: " + getDimensions() + " vs " + v.getDimensions());
        }
        float mag = 0;
        for (int i = 0; i < vectorData.length; i++) {
            float diff = v.getValue(i) - vectorData[i];
            mag += diff * diff;
        }
        return (float) Math.sqrt((double) mag);
    }

    @Override
    public String toString() {
        String vectorString = "";
        for (int i = 0; i < vectorData.length; i++) {
            vectorString += vectorData[i] + ",";
        }
        return vectorString;
    }
}

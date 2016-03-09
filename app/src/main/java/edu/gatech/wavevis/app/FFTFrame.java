package edu.gatech.wavevis.app;

public class FFTFrame implements Drawable{

    private NVector frameBins;
    private NVector frameData;

    public FFTFrame(NVector frameBins) {
        this.frameBins = frameBins;
    }

    public FFTFrame(NVector frameBins, NVector frameData) {
        this.frameBins = frameBins;
        this.frameData = frameData;
    }

    public int getBinCount() {
        return frameData.getDimensions();
    }

    public NVector getFrameBins() {
        return frameBins;
    }

    public void setFrameData(NVector frameData) {
        this.frameData = frameData;
    }

    public NVector getFrameData() {
        return frameData;
    }

    @Override
    public int[] draw(int width, int height) {
        NVector logFrame = frameData.log10();
        float minLog = -5.F;
        float maxLog = 3.F;
        float logRange = maxLog - minLog;
        int[] imageBuffer = new int[width * height];
        for (int i = 0; i < width; i++) {
            int binIndex = (int) ((float) i * (float) getBinCount() / (float) width);
            int normPower = (int) (((logFrame.getValue(binIndex) - minLog) / logRange) * (float) height);
            for (int j = 0; j < height; j++) {
                int mapIndex = (height - 1 - j) * width + i;
                if (j <= normPower) {
                    imageBuffer[mapIndex] = 0xffff0000;
                } else {
                    imageBuffer[mapIndex] = 0xff000000;
                }
            }
        }
        return imageBuffer;
    }
}

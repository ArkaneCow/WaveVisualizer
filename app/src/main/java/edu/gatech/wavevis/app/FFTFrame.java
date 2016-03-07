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
    public byte[] draw(int width, int height) {
        NVector logFrame = frameData.log10();
        float minLog = logFrame.min();
        float maxLog = logFrame.max();
        float logRange = maxLog - minLog;
        byte[] imageBuffer = new byte[width * height * 4];
        for (int i = 0; i < width; i++) {
            int binIndex = (int) ((float) i * (float) getBinCount() / (float) width);
            int normPower = (int) (((logFrame.getValue(binIndex) - minLog) / logRange) * (float) height);
            for (int j = 0; j < height; j++) {
                int mapIndex = (j * width + i) * 4; // index of imageBuffer[x, y, 0]
                if (j <= normPower) {
                    imageBuffer[mapIndex] = (byte) 255;
                    imageBuffer[mapIndex + 1] = (byte) 255;
                    imageBuffer[mapIndex + 2] = (byte) 0;
                    imageBuffer[mapIndex + 3] = (byte) 0;
                } else {
                    imageBuffer[mapIndex] = (byte) 255;
                    imageBuffer[mapIndex + 1] = (byte) 0;
                    imageBuffer[mapIndex + 2] = (byte) 0;
                    imageBuffer[mapIndex + 3] = (byte) 0;
                }
            }
        }
        return imageBuffer;
    }
}

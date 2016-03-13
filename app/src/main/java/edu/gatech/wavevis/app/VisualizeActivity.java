package edu.gatech.wavevis.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.util.fft.FFT;

public class VisualizeActivity extends Activity {

    private ImageView visualGraph;
    private TextView textView;

    private AudioDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        textView = (TextView) findViewById(R.id.magnitudeValue);
        visualGraph = (ImageView) findViewById(R.id.visualGraph);
        dispatcher = AudioService.getInstance().getAudioDispatcher();
        final int audioBufferSize = AudioService.getInstance().getBufferSize();
        dispatcher.addAudioProcessor(new AudioProcessor() {
            FFT fft = new FFT(audioBufferSize);
            float[] amplitudes = new float[audioBufferSize / 2];
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] audioFloatBuffer = audioEvent.getFloatBuffer();
                float[] transformBuffer = new float[audioBufferSize * 2];
                System.arraycopy(audioFloatBuffer, 0, transformBuffer, 0, audioFloatBuffer.length);
                fft.forwardTransform(transformBuffer);
                fft.modulus(transformBuffer, amplitudes);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int drawWidth = 240;
                        int drawHeight = 160;
                        NVector frameVector = new NVector(amplitudes);
                        FFTFrame frameFFT = new FFTFrame(frameVector, frameVector);
                        int[] frameVis = frameFFT.draw(drawWidth, drawHeight);
                        Bitmap bmp = Bitmap.createBitmap(drawWidth, drawHeight, Bitmap.Config.ARGB_8888);
                        bmp.setPixels(frameVis, 0, drawWidth, 0, 0, drawWidth, drawHeight);
                        visualGraph.setImageBitmap(bmp);
                        textView.setText("" + frameVector.magnitude());
                    }
                });
                return true;
            }

            @Override
            public void processingFinished() {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

package edu.gatech.wavevis.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.SpectralPeakProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.mfcc.MFCC;

public class VisualizeActivity extends Activity {

    private int sampleRate = 8000;
    private int bufferSize = 1024;
    private AudioDispatcher dispatcher;
    private ImageView visualGraph;
    private TextView textView;
    private SpectralPeakProcessor spProcessor;
    private MFCC mfccProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        textView = (TextView) findViewById(R.id.testText);
        visualGraph = (ImageView) findViewById(R.id.visualGraph);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0);
        spProcessor = new SpectralPeakProcessor(bufferSize, bufferSize / 2, sampleRate);
        mfccProcessor = new MFCC(bufferSize, sampleRate);
        dispatcher.addAudioProcessor(spProcessor);
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                final float[] magSpectrum = mfccProcessor.getMFCC();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int drawWidth = 120;
                        int drawHeight = 80;
                        NVector frameVector = new NVector(magSpectrum);
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

        new Thread(dispatcher).start();
    }
}

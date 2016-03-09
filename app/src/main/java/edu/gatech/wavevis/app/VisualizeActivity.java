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
import be.tarsos.dsp.mfcc.MFCC;

public class VisualizeActivity extends Activity {

    private int sampleRate = 8000;
    private int bufferSize = 1024;
    private AudioDispatcher dispatcher;
    private ImageView visualGraph;
    private TextView textView;
    private MFCC mfccProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
        textView = (TextView) findViewById(R.id.testText);
        visualGraph = (ImageView) findViewById(R.id.visualGraph);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(sampleRate, bufferSize, 0);
        mfccProcessor = new MFCC(bufferSize, sampleRate);
        dispatcher.addAudioProcessor(mfccProcessor);
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                final float[] magSpectrum = mfccProcessor.getMFCC();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NVector frameVector = new NVector(magSpectrum);
                        FFTFrame frameFFT = new FFTFrame(frameVector, frameVector);
                        int[] frameVis = frameFFT.draw(600, 400);
                        Bitmap bmp = Bitmap.createBitmap(600, 400, Bitmap.Config.ARGB_8888);
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

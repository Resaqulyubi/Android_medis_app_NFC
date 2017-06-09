package thpplnetwork.android_medis_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import thpplnetwork.android_medis_app.R;

/**
 * Created by ResaQulyubi on 4/8/2016.
 */
public class splashscreen extends AppCompatActivity {
    private static int splashInterval = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);

        //thread for splash screen running
        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    Log.d("Exception", "Exception" + e);
                } finally {
                    startActivity(new Intent(splashscreen.this, MainActivity.class));
                }
                finish();
            }
        };
        logoTimer.start();

    }}
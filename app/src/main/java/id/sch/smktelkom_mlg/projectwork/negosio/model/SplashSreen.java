package id.sch.smktelkom_mlg.projectwork.negosio.model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * Created by Dwi Enggar on 30/01/2017.
 */

public class SplashSreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashSreen.this, MainActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}

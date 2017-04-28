package id.sch.smktelkom_mlg.projectwork.negosio;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import id.sch.smktelkom_mlg.projectwork.negosio.manager.ScreenSlider;

/**
 * Created by Dwi Enggar on 07/04/2017.
 */

public class IntroActivity extends AppIntro {

    public static final int CALL_CODE = 24;
    public static final int CAMERA_CODE = 23;
    public static final int READ_STORAGE_CODE = 25;
    public static final int WRITE_STORAGE_CODE = 26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(ScreenSlider.newInstance(R.layout.intro1));
        addSlide(ScreenSlider.newInstance(R.layout.intro2));
        addSlide(ScreenSlider.newInstance(R.layout.intro3));

        showStatusBar(true);
        setDepthAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        loadMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        loadMainActivity();
    }

    private void loadMainActivity() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_CODE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_CODE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_CODE);
        }

        finish();
    }
}

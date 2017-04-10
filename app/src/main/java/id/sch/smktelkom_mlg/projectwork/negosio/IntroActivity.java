package id.sch.smktelkom_mlg.projectwork.negosio;

import android.content.Intent;
import android.os.Bundle;
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
        startActivity(new Intent(this, MainActivity.class));
    }
}

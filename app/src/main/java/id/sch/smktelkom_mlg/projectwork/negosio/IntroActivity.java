package id.sch.smktelkom_mlg.projectwork.negosio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Dwi Enggar on 07/04/2017.
 */

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance
                ("", "Hello, Negosion! Welcome to Renting Appliction Negosio!", R.drawable.icon_negosio, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance
                ("", "Rent equipment for any occasion and project at every budget. Find what you need here!", R.drawable.slide2, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance
                ("", "Want to help other negosion to get the items? Just start register and renting now to make more money", R.drawable.slide3, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }
}

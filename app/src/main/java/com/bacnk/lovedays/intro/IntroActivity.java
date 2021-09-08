package com.bacnk.lovedays.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bacnk.lovedays.R;
import com.bacnk.lovedays.common.SaveSharedPreferences;
import com.bacnk.lovedays.main.MainInfoActivity;
import com.bacnk.lovedays.main.database.DatabaseService;

import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_1;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_10;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_2;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_3;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_4;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_5;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_6;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_7;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_8;
import static com.bacnk.lovedays.common.LoveCommon.KEY_THEME_COLOR_9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class IntroActivity extends AppCompatActivity {
    private DatabaseService databaseService ;
    Window window;

    private ConstraintLayout bgIntro;
    public SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        bgIntro = findViewById(R.id.bgIntro);
        setColotTheme(saveSharedPreferences.getAppTheme());
        // Animation logo
        setUpFadeAnimationLogo((TextView) findViewById(R.id.txtLogo));

    }

    /**
     * Set color bar
     * @param colorCd
     */
    private void setColorBar(int colorCd) {
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(colorCd);
        }

        bgIntro.setBackgroundColor(colorCd);
    }

    /**
     * Click color select
     * @param colorCd
     */
    private void setColotTheme(int colorCd) {

        switch (colorCd) {
            case KEY_THEME_COLOR_1:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.Pi300));
                break;
            case KEY_THEME_COLOR_2:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_2));
                break;
            case KEY_THEME_COLOR_3:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_3));
                break;
            case KEY_THEME_COLOR_4:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_4));
                break;
            case KEY_THEME_COLOR_5:
                setColorBar(this.getResources().getColor(R.color.color_bar_5));
                break;
            case KEY_THEME_COLOR_6:
                setColorBar(this.getResources().getColor(R.color.color_bar_6));
                break;
            case KEY_THEME_COLOR_7:
                setColorBar(this.getResources().getColor(R.color.color_bar_7));
                break;
            case KEY_THEME_COLOR_8:
                setColorBar(this.getResources().getColor(R.color.color_bar_8));
                break;
            case KEY_THEME_COLOR_9:
                setColorBar(this.getResources().getColor(R.color.color_bar_9));
                break;
            case KEY_THEME_COLOR_10:
                setColorBar(this.getResources().getColor(R.color.color_bar_10));
                break;
            default:
                setColorBar(this.getResources().getColor(R.color.Pi300));
                break;
        }
    }
    /**
     * Animation text logo
     * @param textView
     */
    private void setUpFadeAnimationLogo(final TextView textView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(2000);

        fadeIn.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                findViewById(R.id.txtDecription).setVisibility(View.VISIBLE);
                setUpFadeAnimationDecript((TextView) findViewById(R.id.txtDecription));
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        textView.startAnimation(fadeIn);
    }

    /**
     * Animation text description
     * @param textView
     */
    private void setUpFadeAnimationDecript(final TextView textView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(1000);

        fadeIn.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(IntroActivity.this, MainInfoActivity.class);
                        startActivity(intent);
                        IntroActivity.this.finish();
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        textView.startAnimation(fadeIn);
    }
}

package com.example.lovedays.intro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.lovedays.R;
import com.example.lovedays.main.MainInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {

    Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        // Set color win bar
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.Pi300));
        }
        // Animation logo
        setUpFadeAnimationLogo((TextView) findViewById(R.id.txtLogo));

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

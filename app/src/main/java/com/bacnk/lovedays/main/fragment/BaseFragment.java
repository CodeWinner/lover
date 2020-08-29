package com.bacnk.lovedays.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bacnk.lovedays.common.SaveSharedPreferences;
import com.bacnk.lovedays.common.service.SoundService;
import com.bacnk.lovedays.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;

import java.io.IOException;

import static com.bacnk.lovedays.main.fragment.FragmentMain.pathMusicMess;
import static com.bacnk.lovedays.main.fragment.FragmentMain.sentences;

/**
 * Created by pc1 on 9/27/2017.
 */

public class BaseFragment extends Fragment {
    MediaPlayer mediaPlayer;
    SaveSharedPreferences saveSharedPreferences ;
    public static boolean SINGLE_CLICK_CAR_ANIM = false;
    int index;
    public RunText runText;
    private FirebaseAnalytics mFirebaseAnalytics;
    private InterstitialAd mInterstitialAd;

    class ClickListener implements View.OnClickListener {
        HTextView hTextView;
        TextView mLabel1, mLabel2, mTextCoutDays;
        public ClickListener(HTextView hTextView,
                             TextView mLabel1,
                             TextView mLabel2,
                             TextView mTextCoutDays) {
            this.hTextView = hTextView;
            this.mLabel1 = mLabel1;
            this.mLabel2 = mLabel2;
            this.mTextCoutDays = mTextCoutDays;
            saveSharedPreferences = new SaveSharedPreferences(getContext());

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
            firebaseAnalysClick("click_image_car_love_message", "click_image_car_love_message");
        }
        @Override
        public void onClick(View v) {

            // Ads
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId(getString(R.string.id_admod_full));
            // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
            if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }


            if (v instanceof ImageView && !SINGLE_CLICK_CAR_ANIM) {
                SINGLE_CLICK_CAR_ANIM = true;
                setUpFadeAnimationStart(mLabel1, mLabel2, mTextCoutDays, hTextView);

                // Stop music
                if (saveSharedPreferences.isMusicBackgroundMessOnOff()) {
                    getActivity().stopService(new Intent(getActivity(), SoundService.class));
                }
            }
        }
    }

    /**
     * Show ads full
     */
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void firebaseAnalysClick(String event, String type) {

        // [START image_view_event]
        Bundle bundle = new Bundle();
        bundle.putString("value", event);
        bundle.putString("type", type);
        mFirebaseAnalytics.logEvent(event, bundle);
        bundle.clear();
    }

    class SimpleAnimationListener extends Animation implements AnimationListener {

        HTextView hTextView;
        TextView mLabel1, mLabel2, mTextCoutDays;
        private Context context;

        public SimpleAnimationListener(Context context, HTextView hTextView,
                                       TextView mLabel1,
                                       TextView mLabel2,
                                       TextView mTextCoutDays) {
            this.context = context;
            this.hTextView = hTextView;
            this.mLabel1 = mLabel1;
            this.mLabel2 = mLabel2;
            this.mTextCoutDays = mTextCoutDays;
        }
        @Override
        public void onAnimationEnd(HTextView hTextView) {
            setUpFadeAnimationEnd(mLabel1, mLabel2, mTextCoutDays, hTextView);
        }
    }

    /**
     * Animation text logo
     */
    private void setUpFadeAnimationStart(final TextView mLabel1,
                                    final TextView mLabel2,
                                    final TextView mTextCoutDays,
                                    final HTextView hTextView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(2000);

        final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(2000);

        fadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                mLabel1.setVisibility(View.GONE);
                mLabel2.setVisibility(View.GONE);
                mTextCoutDays.setVisibility(View.GONE);

                //
                hTextView.setVisibility(View.VISIBLE);
                hTextView.startAnimation(fadeIn);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
                if (saveSharedPreferences.isMusicBackgroundMessOnOff()) {
                    playMusic();
                }
            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runText = new RunText(hTextView, mLabel1, mLabel2, mTextCoutDays);
                runText.execute();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLabel1.startAnimation(fadeOut);
        mLabel2.startAnimation(fadeOut);
        mTextCoutDays.startAnimation(fadeOut);
    }

    /**
     * Animation text logo
     */
    private void setUpFadeAnimationEnd(final TextView mLabel1,
                                         final View mLabel2,
                                         final View mTextCoutDays,
                                         final HTextView hTextView) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(2000);

        final Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(2000);

        fadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeOut when fadeIn ends (continue)
                hTextView.setVisibility(View.GONE);
                //
                mLabel1.setVisibility(View.VISIBLE);
                mLabel2.setVisibility(View.VISIBLE);
                mTextCoutDays.setVisibility(View.VISIBLE);
                mLabel1.startAnimation(fadeIn);
                mLabel2.startAnimation(fadeIn);
                mTextCoutDays.startAnimation(fadeIn);

                if (saveSharedPreferences.isMusicBackgroundMessOnOff()) {
                    stopMusic();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInterstitial();
                    }
                }, 4500);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {

            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (saveSharedPreferences.isMusicBackgroundOnOff()) {
                    getActivity().startService(new Intent(getActivity(), SoundService.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hTextView.startAnimation(fadeOut);
    }

    /**
     * Play music
     */
    public void playMusic() {
        if (pathMusicMess != null && !pathMusicMess.isEmpty()) {
            Uri uri = Uri.parse(pathMusicMess);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(getContext(), uri);
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true); // Set looping
                mediaPlayer.setVolume(50, 50);
                mediaPlayer.start();
            } catch (IOException e) {
                SINGLE_CLICK_CAR_ANIM = false;
                e.printStackTrace();
            }
        }
    }

    /**
     * Stop meusic
     */
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    class RunText extends AsyncTask<Void, String, Void> {
        HTextView v;
        TextView v1;
        View v2;
        View v3;

        RunText(HTextView v, TextView v1, TextView v2, TextView v3) {
            this.v = v;
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpFadeAnimationEnd(v1, v2, v3, v);
            SINGLE_CLICK_CAR_ANIM = false;

            ((HTextView) v).animateText("   ");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < sentences.length; i++) {
                try {
                    if(!isCancelled()) {
                        publishProgress(sentences[i]);
                        Thread.sleep(2500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SINGLE_CLICK_CAR_ANIM = false;
                    runText.cancel(true);
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            ((HTextView) v).animateText(values[0].toString());
            Log.i("MESSAGE", values[0].toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopMusic();
        if (runText != null) {
            runText.cancel(true);
        }
    }
}

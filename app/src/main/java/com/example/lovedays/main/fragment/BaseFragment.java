package com.example.lovedays.main.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;

import static com.example.lovedays.common.LoveCommon.SINGLE_CLICK_CAR_ANIM;

/**
 * Created by pc1 on 9/27/2017.
 */

public class BaseFragment extends Fragment {
    public static String[] sentences ;
    int index;

    class ClickListener implements View.OnClickListener {
        HTextView hTextView;
        TextView mLabel1, mLabel2, mTextCoutDays;
        public ClickListener(HTextView hTextView,
                             TextView mLabel1,
                             TextView mLabel2,
                             TextView mTextCoutDays,
                             String[] sentences) {
            this.hTextView = hTextView;
            this.mLabel1 = mLabel1;
            this.mLabel2 = mLabel2;
            this.mTextCoutDays = mTextCoutDays;
            sentences = sentences;
        }
        @Override
        public void onClick(View v) {
            if (v instanceof ImageView && !SINGLE_CLICK_CAR_ANIM) {
                SINGLE_CLICK_CAR_ANIM = true;
                setUpFadeAnimationStart(mLabel1, mLabel2, mTextCoutDays, hTextView);
            }
        }
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
            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RunText runText = new RunText(hTextView, mLabel1, mLabel2, mTextCoutDays);
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hTextView.startAnimation(fadeOut);
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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if(MainActivity.model_json_static.getMess().equals("")==false)
//            {
//                sentences = customMess(MainActivity.model_json_static.getMess());
//                Log.i("SHOW","toArrayMess : "+ toArrayMess(customMess(MainActivity.model_json_static.getMess())).length);
//                Log.i("SHOW","sentences : "+ sentences.size());
//            }
//            else {
               /// sentences = new ArrayList<>();
     //       }
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < sentences.length; i++) {
                publishProgress(sentences[i]);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            ((HTextView) v).animateText(values[0].toString());
        }
    }
}

package com.bacnk.lovedays.main.fragment;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.bacnk.lovedays.common.DateUnitCommon;
import com.bacnk.lovedays.common.DateUtils;
import com.bacnk.lovedays.common.SaveSharedPreferences;
import com.bacnk.lovedays.common.SingleClickListener;
import com.bacnk.lovedays.common.exception.LoveDaysCountDayException;
import com.bacnk.lovedays.main.database.DatabaseInfoAppListener;
import com.bacnk.lovedays.main.units.InfoApp;
import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.database.DatabaseService;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.material.button.MaterialButton;
import com.hanks.htextview.base.HTextView;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.bacnk.lovedays.main.MainInfoActivity.konfettiView;

public class FragmentMain extends BaseFragment implements DatabaseInfoAppListener {
    public static String PATERN_SPLIT_MESS = "\n";
    public static float MAX_ROAD = 800f;
    public static float MAX_TIME = 86400000;
    public static float SPEED = MAX_ROAD/MAX_TIME;
    // Store instance variables
    private String title;
    private int page;

    public static TextView mTextCountDay;
    private TextView mTextViewStartDayMain;
    private HTextView hTextView;
    public static Typeface custom_font;
    public static ImageView mImageCar;
    private FrameLayout frameLayout2;
    // Progress
    private ProgressDialog progressDialog;
    // Dialo update infor lover
    private AlertDialog dialogUpdateLocer;

    private DatabaseService databaseService ;
    private Handler handler = new Handler();

    private SaveSharedPreferences saveSharedPreferences;
    private int countStartUpdate = 0;

    public TextView mLabel1, mLabel2;

    public static String pathMusicMess;
    public static String[] sentences ;
    public static InfoApp infoApp;
    public String mess;
    public String path;

    // newInstance constructor for creating fragment with arguments
    public static FragmentMain newInstance(int mess, String musicPath) {
        FragmentMain fragmentMain = new FragmentMain();
        return fragmentMain;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTextCountDay = view.findViewById(R.id.mTextCountDay);
        mLabel1 = view.findViewById(R.id.mLabel1);
        mLabel2 = view.findViewById(R.id.mLabel2);
        hTextView =view.findViewById(R.id.txtEvaporateText);
        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/scripti.ttf");
        hTextView.setTypeface(custom_font);

        mTextViewStartDayMain = view.findViewById(R.id.textViewStartDayMain);
        mImageCar = view.findViewById(R.id.loverCar);
        frameLayout2 = view.findViewById(R.id.frameLayout2);


        // Set ting slide mess
        if (infoApp != null) {
            mess = infoApp.getLoveText();
            if (mess != null && !mess.trim().isEmpty()) {
                sentences = mess.split(PATERN_SPLIT_MESS);
                mImageCar.setOnClickListener(new ClickListener(hTextView, mLabel1, mLabel2, mTextCountDay));
            }

            path = infoApp.getMusicPathMess();
            if (path != null && !path.trim().isEmpty()) {
                pathMusicMess = path;
            }
        }

        saveSharedPreferences = new SaveSharedPreferences(getContext());
        countStartUpdate = saveSharedPreferences.getUpdateStartDayCount();
        // Click text
        mTextCountDay.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                displayDialogSettingProfile();
            }
        });

        // Select data start day
        String startDay = databaseService.getStartDay();
        if (startDay != null) {
            setCountDays(startDay);
        }

        mLabel1.setText(saveSharedPreferences.getTitleDisplay().getTitleTop());
        mLabel2.setText(saveSharedPreferences.getTitleDisplay().getTitleBottom());

        //setAnimCar(mImageCar);

        // On show case count day
        if (!saveSharedPreferences.getShowCaseStartDay()) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    new ShowcaseView.Builder(getActivity())
                            .withMaterialShowcase()
                            .setStyle(R.style.CustomShowcaseTheme2)
                            .setTarget(new ViewTarget(mTextCountDay))
                            .setContentTitle("Events")
                            .setContentText(getString(R.string.show_case_start_date))
                            .setShowcaseEventListener(new OnShowcaseEventListener() {
                                @Override
                                public void onShowcaseViewHide(ShowcaseView showcaseView) {
                                    if (!saveSharedPreferences.getShowCaseLoveMess()) {
                                        new ShowcaseView.Builder(getActivity())
                                                .withMaterialShowcase()
                                                .setStyle(R.style.CustomShowcaseTheme3)
                                                .setTarget(new ViewTarget(mImageCar))
                                                .setContentTitle("Events")
                                                .setContentText(getString(R.string.show_case_love_mess))
                                                .setShowcaseEventListener(this)
                                                .build();

                                        saveSharedPreferences.saveShowCaseLoveMess(true);
                                    }
                                }

                                @Override
                                public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                                }

                                @Override
                                public void onShowcaseViewShow(ShowcaseView showcaseView) {

                                }

                                @Override
                                public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                                }
                            })
                            .build();

                    saveSharedPreferences.saveShowCaseStartDay(true);
                }
            }, 2000);
        }

        //mImageCar.setVisibility(View.INVISIBLE);
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open db
        databaseService = new DatabaseService(getContext(), this);
        databaseService.open();

    }

    /**
     * Seting name, birth day, image profile
     */
    public void displayDialogSettingProfile() {
        // Show wait progress
        showProcess();

        AlertDialog.Builder dialogBuilder =	new AlertDialog.Builder(getContext());
        LayoutInflater inflater	= this.getLayoutInflater();
        View dialogView	=	inflater.inflate(R.layout.custom_dialog_input_startday, null);

        // Init view
        final EditText mTextInputStartDay = dialogView.findViewById(R.id.mTextInputStartDay);
        MaterialButton mButtonUpdateStartDay =  dialogView.findViewById(R.id.mButtonUpdateStartDay);

        // get day
        String startDay = databaseService.getStartDay();
        if (startDay != null) {
            mTextInputStartDay.setText(startDay);
        }

        final int[] oldTextLength = {0};
        final boolean[] isRemove = {false};
        mTextInputStartDay.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (oldTextLength[0] > mTextInputStartDay.getText().length()) {
                    isRemove[0] = true;
                } else {
                    isRemove[0] = false;

                }

                if (!isRemove[0]) {
                    if (mTextInputStartDay.getText().length() == 2 || mTextInputStartDay.getText().length() == 5) {
                        mTextInputStartDay.setText(mTextInputStartDay.getText() + "/");
                        mTextInputStartDay.setSelection(mTextInputStartDay.getText().length());
                        isRemove[0] = false;
                    }
                }

                oldTextLength[0] = mTextInputStartDay.getText().length();
            }
        });

        // Click button update
        mButtonUpdateStartDay.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                // Show wait progress
                showProcess();

                if (countStartUpdate == 0) {
                    countStartUpdate ++;
                    saveSharedPreferences.saveUpdateStartDayCount(countStartUpdate);
                }

                String startDay = mTextInputStartDay.getText().toString();
                // Case date format dd/MM/yyyy
                if(startDay.length() == 10 && new DateUtils().isValidDate(startDay)) {
                    databaseService.updateStartDay(startDay);

                } else {
                    cancelProcess();
                    Toasty.error(getContext(), getString(R.string.vailidate_startday_error), Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(dialogView);
        alert.setCancelable(true);
        dialogUpdateLocer = alert.create();
        // Wait
        waitProgress(dialogUpdateLocer, true);
        //  dialog.show();
    }

    private void sendDataToFragmentCount(String startDay) {
        Bundle bundle = new Bundle();
        bundle.putString("START_DAY", startDay);

        // Your fragment
        FragmentCount.newInstance(startDay);
    }

    /**
     *
     * @param startDay
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCountDays(String startDay) {
        try {

            long numDay = DateUtils.getNumDaysFromCurrentTime(startDay);
            mTextCountDay.setText(numDay + "");
            DateUnitCommon dateUnitCommon = DateUtils.getTimeBetween(startDay);
            int hours = dateUnitCommon.getHours();

            // morning
            mTextViewStartDayMain.setText("-- " + startDay + " --");

            sendDataToFragmentCount(startDay);
        } catch (LoveDaysCountDayException e) {
            Toasty.error(getContext(), getString(R.string.error), Toast.LENGTH_SHORT, true).show();
        }
    }

    /**
     * Show process
     */
    private void showProcess() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    /**
     * Wait process dialog
     * @param dialog
     */
    private void waitProgress(final AlertDialog dialog, final boolean isShow) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {

                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (isShow) {
                            dialog.show();
                        } else {
                            dialog.cancel();
                            Toasty.success(getContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Cancel process wait
     */
    private void cancelProcess() {
        progressDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void updPersonSuccess(String startDay) {
        if (startDay != null) {
            setCountDays(startDay);

            konfettiView.build()
                    .addColors(this.getResources().getColor(R.color.PB1), this.getResources().getColor(R.color.PB2), this.getResources().getColor(R.color.PB3))
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .streamFor(300, 5000L);
        }

        waitProgress(dialogUpdateLocer, false);
    }

    @Override
    public void updPersionError(String error) {
        Toasty.error(getContext(), getString(R.string.update_error), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void getPersonError(String error) {

    }

    @Override
    public void updateInforError(String error) {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

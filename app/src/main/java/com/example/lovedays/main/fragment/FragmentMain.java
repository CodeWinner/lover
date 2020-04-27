package com.example.lovedays.main.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.fragment.app.Fragment;

import com.example.lovedays.R;
import com.example.lovedays.common.DateUnitCommon;
import com.example.lovedays.common.DateUtils;
import com.example.lovedays.common.SaveSharedPreferences;
import com.example.lovedays.common.SingleClickListener;
import com.example.lovedays.common.exception.LoveDaysCountDayException;
import com.example.lovedays.common.service.CountDaysService;
import com.example.lovedays.main.database.DatabaseInfoAppListener;
import com.example.lovedays.main.database.DatabaseService;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lovedays.common.DateUtils.getNumDaysFromCurrentTime;
import static com.example.lovedays.common.DateUtils.getTimeBetween;

public class FragmentMain extends Fragment implements DatabaseInfoAppListener {
    public static float MAX_ROAD = 800f;
    public static float MAX_TIME = 86400000;
    public static float SPEED = MAX_ROAD/MAX_TIME;
    // Store instance variables
    private String title;
    private int page;

    private TextView mTextCountDay, mTextViewStartDayMain;
    private ImageView mImageCar;
    private FrameLayout frameLayout2;
    // Progress
    private SweetAlertDialog progressDialog;
    // Dialo update infor lover
    private AlertDialog dialogUpdateLocer;

    private DatabaseService databaseService ;
    private Handler handler = new Handler();

    private SaveSharedPreferences saveSharedPreferences;
    private int countStartUpdate = 0;

    // newInstance constructor for creating fragment with arguments
    public static FragmentMain newInstance(int page, String title) {
        FragmentMain fragmentMain = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentMain.setArguments(args);
        return fragmentMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mTextCountDay = view.findViewById(R.id.mTextCountDay);

        mTextViewStartDayMain = view.findViewById(R.id.textViewStartDayMain);
        mImageCar = view.findViewById(R.id.loverCar);
        frameLayout2 = view.findViewById(R.id.frameLayout2);

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
        setAnimCar(mImageCar);
        //mImageCar.setVisibility(View.INVISIBLE);
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        page = getArguments().getInt("someInt");
//        title = getArguments().getString("someTitle");
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

    private void setAnimCar(final ImageView view) {

       // float startPx = saveSharedPreferences.getPositionCar();

        Date currentTime = Calendar.getInstance().getTime();
        long secondNowH = currentTime.getHours() * 60 * 60;
        long secondNowM = currentTime.getMinutes() * 60;
        long secondNowS = currentTime.getSeconds();

        long secondRuned = (secondNowH + secondNowM + secondNowS) * 1000;

        long pX = (long) (secondRuned * SPEED);
        long roandConLai = (long) (MAX_ROAD - pX);

        // Start car
        view.setX(pX);

        long timeConLai = (long) (roandConLai/SPEED);

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", roandConLai);
        animation.setDuration(timeConLai);
//        animation.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                //animation.setDuration((long) MAX_TIME);
//                //animation.start();
//            }
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                view.setScaleX(0);
//            }
//        });
        animation.start();


        //setAnimCar(view);
    }
    /**
     *
     * @param startDay
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCountDays(String startDay) {
        try {

            long numDay = getNumDaysFromCurrentTime(startDay);
            mTextCountDay.setText(numDay + "");
            DateUnitCommon dateUnitCommon = getTimeBetween(startDay);
            int hours = dateUnitCommon.getHours();

            // morning
            if (hours > 4 && hours < 13) {
                mTextViewStartDayMain.setText("-- Good morning --");
            } else if (hours > 12 && hours < 18) {
                mTextViewStartDayMain.setText("-- Good afternoon --");
            } else if (hours > 17 && hours < 23) {
                mTextViewStartDayMain.setText("-- Good everning --");
            } else {
                mTextViewStartDayMain.setText("-- Good night --");
            }

            sendDataToFragmentCount(startDay);
        } catch (LoveDaysCountDayException e) {
            Toasty.error(getContext(), getString(R.string.error), Toast.LENGTH_SHORT, true).show();
        }
    }

    /**
     * Show process
     */
    private void showProcess() {
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
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
                        progressDialog.dismiss();
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

    @Override
    public void updPersonSuccess(String startDay) {
        if (startDay != null) {
            setCountDays(startDay);
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
    public void onStop() {
        super.onStop();
        Log.i("MAIN_FRG","onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MAIN_FRG","onDestroy()");
        //saveSharedPreferences.savePositionCar(mImageCar.getX());
    }
}

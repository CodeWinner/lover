package com.example.lovedays.main.time_line;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.lovedays.BuildConfig;
import com.example.lovedays.R;
import com.example.lovedays.common.DateUtils;
import com.example.lovedays.common.SingleClickListener;
import com.example.lovedays.common.exception.LoveDaysCountDayException;
import com.example.lovedays.intro.IntroActivity;
import com.example.lovedays.main.MainInfoActivity;
import com.example.lovedays.main.database.DatabaseService;
import com.example.lovedays.main.database.DatabaseTimelineOnListener;
import com.example.lovedays.main.helper.RecyclerItemClickListener;
import com.example.lovedays.main.helper.TimeLineEditAdapter;
import com.example.lovedays.main.units.TimeLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.example.lovedays.common.LoveCommon.BLANK;
import static com.example.lovedays.common.LoveCommon.POS_BUNDLE_STRING;

public class TimeLineActivity extends AppCompatActivity implements DatabaseTimelineOnListener {
    private static final int IMAGE_1 = 1;
    private static final int IMAGE_2 = 2;
    private static final int IMAGE_3 = 3;
    public static final int PICK_IMAGE = 1 ;
    public static final int MODE_COUNT_DAY = 1 ;
    public static final int MODE_SPECIAL_DAY = 2 ;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Window window;
    // Progress
    private SweetAlertDialog progressDialog;
    private Handler handler = new Handler();
    // Dialog update infor lover
    private AlertDialog dialogEditTimeLine;
    private AlertDialog dialogMenuTimeLine;

    // 1 : then count day, 2: then special day
    private static int typeDayFlg = MODE_COUNT_DAY;

    private int requestMode = BuildConfig.RequestMode;

    private CircleImageView mImageHistory1;
    private CircleImageView mImageHistory2;
    private CircleImageView mImageHistory3;

    private String imageUri1 = null;
    private String imageUri2 = null;
    private String imageUri3 = null;

    private DatabaseService databaseService ;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date currentTime = Calendar.getInstance().getTime();

    public static int positionTimelineScroll = 0;
    public static String idTimelineScroll = null;

    public List<TimeLine> timeLines = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseService = new DatabaseService(this, this);
        databaseService.open();

        // Set color win bar
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.BgTimeLine));
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialogSettingProfile(null);
            }
        });

        Bundle bundle = getIntent().getExtras();
        String positionFromFragment = null;
        if (bundle != null) {
            positionFromFragment = bundle.getString(POS_BUNDLE_STRING);

        }

        recyclerView = findViewById(R.id.TimeLineRecycleEdit);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        // Select data timeline in SQL
        timeLines = databaseService.getAllTimeline();

        if (timeLines != null  && timeLines.size() > 0) {
            Collections.sort(timeLines);

            mAdapter = new TimeLineEditAdapter(setStatusTimeline(timeLines), this);
            recyclerView.setAdapter(mAdapter);

            if (positionFromFragment != null && !positionFromFragment.trim().isEmpty()) {
                positionTimelineScroll = Integer.parseInt(positionFromFragment);
                positionTimelineScroll = (positionTimelineScroll + 1) <= timeLines.size()
                        ? (positionTimelineScroll + 1) : positionTimelineScroll;
            }

            recyclerView.smoothScrollToPosition(positionTimelineScroll);
        }

        // Click recycler view
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        displayDialogMenu(position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));

    }

    /**
     * Set status
     * @param timeLines
     * @return
     */
    private List<TimeLine> setStatusTimeline(List<TimeLine> timeLines) {
        int i = 0;
        do {
            try {

                if (timeLines.get(i).getDate() != null
                        && sdf.parse(timeLines.get(i).getDate()).before(currentTime)) {

                        timeLines.get(i).setStatus("inactive");
                } else if (timeLines.get(i).getDate() != null
                        && sdf.parse(timeLines.get(i).getDate()).after(currentTime)
                        && i != 0) {

                    positionTimelineScroll = i ;
                    timeLines.get(i-1).setStatus("active");
                    return timeLines;
                }

                i++;
            } catch (ParseException e) {
                e.printStackTrace();
                i++;
            }
        } while (i < timeLines.size());

        return timeLines;
    }

    public int getPositionById(String id, List<TimeLine> timeLines) {
        for (int i = 0; i < timeLines.size(); i++) {
            if (id.equals(timeLines.get(i).getId().trim())) {
                return (i+1) <= timeLines.size() ? (i+1) : i;
            }
        }

        return 0;
    }

    /**
     * Display menu
     * @param position
     */
    public void displayDialogMenu(final int position) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LayoutInflater inflater	= this.getLayoutInflater();
        View dialogView	=	inflater.inflate(R.layout.dialog_menu_timeline, null);

        final Button mButtonEditTimeline = dialogView.findViewById(R.id.buttonEditTimeline);
        final Button mButtonDeleteTimeline = dialogView.findViewById(R.id.buttonDeleteTimeline);

        // Move to edit timeline dialog
        mButtonEditTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayDialogSettingProfile(timeLines.get(position).getId());
                dialogMenuTimeLine.cancel();
            }
        });

        mButtonDeleteTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeLines.size() > 0) {
                    showDialodConfirmDelete(timeLines.get(position).getId(), position);
                }
            }
        });

        alert.setView(dialogView);
        alert.setCancelable(true);
        dialogMenuTimeLine = alert.create();
        dialogMenuTimeLine.show();
    }

    public void showDialodConfirmDelete(final String idTimeline, final int position) {
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.confirm_delete_1))
                .setContentText(getString(R.string.confirm_delete_2))
                .setConfirmText(getString(R.string.confirm_delete_button));
        progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance
                        dialogMenuTimeLine.cancel();
                        progressDialog = sDialog;
                        databaseService.deleteTimeline(idTimeline, position);
                    }
                });
        progressDialog.show();
    }
    /**
     * Display dialog add/edit timeline
     * @param idTimeLine
     */
    public void displayDialogSettingProfile(final String idTimeLine) {
        // Show wait progress
        showProcess();

        AlertDialog.Builder dialogBuilder =	new AlertDialog.Builder(this);
        LayoutInflater inflater	= this.getLayoutInflater();
        View dialogView	=	inflater.inflate(R.layout.dialog_edit_timeline, null);

        final RadioButton mRadioCount = dialogView.findViewById(R.id.radioCountDay);
        final RadioButton mRadioSpecial = dialogView.findViewById(R.id.radioSpecialDay);
        final EditText mInputCountDay = dialogView.findViewById(R.id.inputCountDay);
        final EditText mInputSpecialDay = dialogView.findViewById(R.id.inputSpecialDay);
        final EditText mInputDescription = dialogView.findViewById(R.id.edtDescription);
        final Button mButtonUpdateDiary = dialogView.findViewById(R.id.mButtonUpdateDiary);
        final TextInputLayout inputCountDayLayout = dialogView.findViewById(R.id.inputCountDayLayout);
        final TextInputLayout inputSpecialDayLayout = dialogView.findViewById(R.id.inputSpecialDayLayout);
        final EditText mInputSubject = dialogView.findViewById(R.id.txtInputSubject);
        mImageHistory1 = dialogView.findViewById(R.id.mImageHistory1);
        mImageHistory2 = dialogView.findViewById(R.id.mImageHistory2);
        mImageHistory3 = dialogView.findViewById(R.id.mImageHistory3);

        imageUri1 = null;
        imageUri2 = null;
        imageUri3 = null;

        // Case idTimeline is null
        if (idTimeLine == null) {
            mRadioCount.setChecked(true);
            inputCountDayLayout.setVisibility(View.VISIBLE);
            inputSpecialDayLayout.setVisibility(View.GONE);

            typeDayFlg = MODE_COUNT_DAY;
            mButtonUpdateDiary.setText("Add");
        } else {
            mButtonUpdateDiary.setText("Update");
            TimeLine timeLine = databaseService.getTimeline(idTimeLine);
            if (timeLine != null) {
                mInputSubject.setText(timeLine.getSubject());

                // IF type count date
                if (Integer.parseInt(timeLine.getType()) == MODE_COUNT_DAY) {
                    mRadioCount.setChecked(true);

                    inputCountDayLayout.setVisibility(View.VISIBLE);
                    inputSpecialDayLayout.setVisibility(View.GONE);

                    mInputCountDay.setText(timeLine.getCountDate());

                    typeDayFlg = MODE_COUNT_DAY;
                } else {
                    mRadioSpecial.setChecked(true);

                    inputCountDayLayout.setVisibility(View.GONE);
                    inputSpecialDayLayout.setVisibility(View.VISIBLE);

                    mInputSpecialDay.setText(timeLine.getDate());

                    typeDayFlg = MODE_SPECIAL_DAY;
                }

                mInputDescription.setText(timeLine.getContent());

                // Set image
                if (timeLine.getImagePath1() != null) {
                    imageUri1 = timeLine.getImagePath1();
                    Picasso.with(getApplicationContext()).load(imageUri1)
                            .error(R.drawable.change_image)
                            .into(mImageHistory1);
                }

                if (timeLine.getImagePath2() != null) {
                    imageUri2 = timeLine.getImagePath2();
                    Picasso.with(getApplicationContext()).load(imageUri2)
                            .error(R.drawable.change_image)
                            .into(mImageHistory2);
                }
                if (timeLine.getImagePath3() != null) {
                    imageUri3 = timeLine.getImagePath3();
                    Picasso.with(getApplicationContext()).load(imageUri3)
                            .error(R.drawable.change_image)
                            .into(mImageHistory3);
                }

            }

        }

        mInputCountDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                inputCountDayLayout.setErrorEnabled(false);
            }
        });

        mInputSpecialDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                inputSpecialDayLayout.setErrorEnabled(false);
            }
        });

        mRadioCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((RadioButton) v).isChecked();

                // Case choose radio count then visible input count day
                if (isChecked) {
                    inputCountDayLayout.setVisibility(View.VISIBLE);
                    inputSpecialDayLayout.setVisibility(View.GONE);

                    typeDayFlg = MODE_COUNT_DAY;
                } else {
                    inputCountDayLayout.setVisibility(View.GONE);
                    inputSpecialDayLayout.setVisibility(View.VISIBLE);

                    typeDayFlg = MODE_SPECIAL_DAY;
                }
            }
        });

        mRadioSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((RadioButton) v).isChecked();

                // Case choose radio count then visible input count day
                if (isChecked) {
                    inputSpecialDayLayout.setVisibility(View.VISIBLE);
                    inputCountDayLayout.setVisibility(View.GONE);

                    typeDayFlg = MODE_SPECIAL_DAY;
                } else {
                    inputSpecialDayLayout.setVisibility(View.GONE);
                    inputCountDayLayout.setVisibility(View.VISIBLE);

                    typeDayFlg = MODE_COUNT_DAY;
                }
            }
        });

        mButtonUpdateDiary.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                String dayStory = null;
                if (typeDayFlg == MODE_COUNT_DAY) {
                    dayStory = mInputCountDay.getText().toString();
                } else {
                    dayStory = mInputSpecialDay.getText().toString();
                }

                String subject = mInputSubject.getText().toString();
                int valid = isValid(dayStory, subject, typeDayFlg);

                // Validate
                if (valid == 0) {

                    String context = mInputDescription.getText().toString();
                    String countDate = mInputCountDay.getText().toString();
                    String startDate = databaseService.getStartDay();
                    String dateCounted = null;
                    try {
                        if (typeDayFlg == MODE_COUNT_DAY) {
                            dateCounted = DateUtils.addDate(startDate, Integer.parseInt(countDate));
                        } else {
                            dateCounted = "0";
                        }
                    } catch (LoveDaysCountDayException e) {
                        Toasty.error(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT, true).show();
                    }
                    String dateSpecial = mInputSpecialDay.getText().toString();

                    TimeLine timeLine = new TimeLine();
                    String id = genIdFromDate();
                    timeLine.setId(id);
                    timeLine.setSubject(subject);
                    timeLine.setContent(context);
                    timeLine.setType(String.valueOf(typeDayFlg));

                    if (typeDayFlg == MODE_COUNT_DAY) {
                        timeLine.setCountDate(countDate);
                        timeLine.setDate(dateCounted);
                    } else {
                        timeLine.setDate(dateSpecial);
                    }
                    timeLine.setImagePath1(imageUri1);
                    timeLine.setImagePath2(imageUri2);
                    timeLine.setImagePath3(imageUri3);

                    if (idTimeLine == null) {

                        databaseService.addTimeline(timeLine);
                    } else {
                        timeLine.setId(idTimeLine);
                        databaseService.updateTimeLine(timeLine);
                    }

                    showProcess();
                } else {

                    if (valid == 2) {
                        if (typeDayFlg == MODE_COUNT_DAY) {
                            inputCountDayLayout.setError(getString(R.string.error_validate_timeline_add));
                        } else {
                            inputSpecialDayLayout.setError(getString(R.string.error_validate_timeline_add));
                        }
                    }

                    if (valid == 1) {
                        mInputSubject.setError(getString(R.string.error_validate_timeline_subject));
                        mInputSubject.setFocusable(true);
                    }

                    if (valid == 3) {
                        mInputSpecialDay.setError(getString(R.string.vailidate_startday_error));
                        mInputSpecialDay.setFocusable(true);
                    }
                }
            }
        });

        mImageHistory1.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery(IMAGE_1);
            }
        });

        mImageHistory2.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery(IMAGE_2);
            }
        });

        mImageHistory3.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery(IMAGE_3);
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(dialogView);
        alert.setCancelable(true);
        dialogEditTimeLine = alert.create();
        // Wait
        waitProgress(dialogEditTimeLine, true);
    }

    /**
     * genaral ID
     * @return String
     */
    private String genIdFromDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

    /**
     * Pick image
     */
    private void pickFromGallery(int requestMode) {
        Intent intent = new Intent(Intent.ACTION_PICK)
                .setType("image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }

        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), requestMode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            if (requestCode == IMAGE_1) {
                try {
                    imageUri1 = data.getData().toString();
                    mImageHistory1.setImageURI(imageUri);
                } catch (Exception e) {

                }

            }

            if (requestCode == IMAGE_2) {
                try {
                    imageUri2 = data.getData().toString();
                    mImageHistory2.setImageURI(imageUri);
                } catch (Exception e) {

                }

            }

            if (requestCode == IMAGE_3) {
                try {
                    imageUri3 = data.getData().toString();
                    mImageHistory3.setImageURI(imageUri);
                } catch (Exception e) {

                }

            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {

        }
    }


    /**
     * Return 0 is validate success
     * Return 1 when subject fail
     * Return 2 when mock fail
     * @param mock
     * @return
     */
    private int isValid(String mock, String subject, int typeDayFlg) {

        // Valid subject
        if (subject == null || subject.equals("Subject") || subject.matches(BLANK)) {
            return 1;
        }

        if (mock == null || mock.matches(BLANK)) {
            return 2;
        }

        // Valid mock
        if (typeDayFlg == MODE_SPECIAL_DAY) {

            if(mock.length() == 10 && new DateUtils().isValidDate(mock)) {
                return 0;

            } else {
                return 3;
            }
        }

        return 0;
    }
    /**
     * Show process
     */
    private void showProcess() {
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
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
                            Toasty.success(getApplicationContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();
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
    protected void onStart() {
        super.onStart();
        databaseService = new DatabaseService(this, this);
        databaseService.open();
    }

    @Override
    public void addTimelineSuccess(String id) {
        waitProgress(dialogEditTimeLine, false);
        //Toasty.success(getApplicationContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();


        // Select data timeline in SQL
        timeLines = databaseService.getAllTimeline();

        if (timeLines != null && timeLines.size() > 0) {
            Collections.sort(timeLines);

            mAdapter = new TimeLineEditAdapter(setStatusTimeline(timeLines), this);
            recyclerView.setAdapter(mAdapter);

            recyclerView.smoothScrollToPosition(getPositionById(id, timeLines));
        }
    }

    @Override
    public void addTimeleError(String error) {

    }

    @Override
    public void updateTimelineSuccess(String id) {
        waitProgress(dialogEditTimeLine, false);
        // Select data timeline in SQL
        timeLines = databaseService.getAllTimeline();

        if (timeLines != null  && timeLines.size() > 0) {
            Collections.sort(timeLines);

            mAdapter = new TimeLineEditAdapter(setStatusTimeline(timeLines), this);
            recyclerView.setAdapter(mAdapter);

            recyclerView.smoothScrollToPosition(getPositionById(id, timeLines));
        }
    }

    @Override
    public void updateTimelineError(String error) {

    }

    @Override
    public void delTimelineSuccess(int position) {

        progressDialog.setTitleText(getString(R.string.confirm_delete_ok))
                .setContentText(getString(R.string.confirm_delete_ok_1))
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        progressDialog.cancel();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


        // Select data timeline in SQL
        timeLines = databaseService.getAllTimeline();

        if (timeLines != null  && timeLines.size() > 0) {
            Collections.sort(timeLines);

            mAdapter = new TimeLineEditAdapter(setStatusTimeline(timeLines), this);
            recyclerView.setAdapter(mAdapter);

            recyclerView.smoothScrollToPosition(position);
        } else {
            mAdapter = new TimeLineEditAdapter(timeLines, this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void delTimelineError(String error) {

    }

    @Override
    public void getTimelineError(String error) {

    }
}

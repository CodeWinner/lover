package com.example.lovedays.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovedays.BuildConfig;
import com.example.lovedays.R;
import com.example.lovedays.common.DateUtils;
import com.example.lovedays.common.SaveSharedPreferences;
import com.example.lovedays.common.exception.LoveDaysCountDayException;
import com.example.lovedays.main.adapter.PagerAdapterMain;
import com.example.lovedays.main.crop_image.BaseActivity;
import com.example.lovedays.main.database.DatabaseInfoAppListener;
import com.example.lovedays.main.database.DatabaseLoverOnListener;
import com.example.lovedays.main.database.DatabaseService;
import com.example.lovedays.main.fragment.FragmentCount;
import com.example.lovedays.main.fragment.FragmentMain;
import com.example.lovedays.main.helper.ProgressGenerator;
import com.example.lovedays.common.SingleClickListener;
import com.example.lovedays.main.helper.WaveHelper;
import com.example.lovedays.main.menu.MenuActivity;
import com.example.lovedays.main.time_line.TimeLineActivity;
import com.example.lovedays.main.units.InfoApp;
import com.example.lovedays.main.units.InfoPersonal;
import com.example.lovedays.main.units.TimeLine;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Side;

import static com.example.lovedays.common.DateUtils.getCHD;
import static com.example.lovedays.common.DateUtils.getNumDaysFromCurrentTime;
import static com.example.lovedays.common.DateUtils.getOld;
import static com.example.lovedays.common.LoveCommon.APP_LOVE_TEXT;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_MESS_NAME;
import static com.example.lovedays.common.LoveCommon.APP_MUSIC_MESS_PATH;
import static com.example.lovedays.common.LoveCommon.BACH_DUONG;
import static com.example.lovedays.common.LoveCommon.BAO_BINH;
import static com.example.lovedays.common.LoveCommon.BO_CAP;
import static com.example.lovedays.common.LoveCommon.CU_GIAI;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_1;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_10;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_2;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_3;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_4;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_5;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_6;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_7;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_8;
import static com.example.lovedays.common.LoveCommon.KEY_THEME_COLOR_9;
import static com.example.lovedays.common.LoveCommon.KIM_NGUU;
import static com.example.lovedays.common.LoveCommon.MA_KET;
import static com.example.lovedays.common.LoveCommon.NHAN_MA;
import static com.example.lovedays.common.LoveCommon.SONG_NGU;
import static com.example.lovedays.common.LoveCommon.SONG_TU;
import static com.example.lovedays.common.LoveCommon.SU_TU;
import static com.example.lovedays.common.LoveCommon.THIEN_BINH;
import static com.example.lovedays.common.LoveCommon.XU_NU;

public class MainInfoActivity extends BaseActivity implements
        ProgressGenerator.OnCompleteListener, DatabaseLoverOnListener, UCropFragmentCallback {

    //==================================================================
    // Contant
    //==================================================================
    private static final String ID_LOVER_1 = "1";
    private static final String ID_LOVER_2 = "2";
    private static final String APP_NAME = "Loves Day";
    // Nam = 1, Nu = 0, LBGT = 2
    private static final String SEX_TYLE_MAN = "1";
    private static final String SEX_TYLE_WOMAN = "0";
    private static final String SEX_TYLE_LBGT = "2";
    private static final String SAMPLE_CROPPED_IMAGE_NAME_1 = "LoveDayImage_lover1";
    private static final String SAMPLE_CROPPED_IMAGE_NAME_2 = "LoveDayImage_lover2";
    private static final String EXTENDTION_PNG = ".png";
    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;

    private int requestMode = BuildConfig.RequestMode;

    public  InfoApp infoApp;
    //==================================================================
    // View
    //==================================================================
    private Window window;
    private ViewPager mViewPager;
    private ImageView mImageHearth;
    private CircleIndicator circleIndicator;

    private RelativeLayout linearLayoutMain;
    private WaveHelper mWaveHelper;
    private WaveView waveView;

    private CircleImageView mImageLover1, mImageLover2;
    private TextView mLover1Name, mLover2Name;

    private ProgressBar mProgerBarWaitMainInfo;
    private ConstraintLayout mLayoutContentMainInfoLayout;
    private LinearLayout mLayoutImageLover1;
    private LinearLayout mLayoutImageLover2;
    private ImageView mImageSexLover1, mImageSexLover2;
    private UCropFragment fragment;
    private CircleImageView mImageCropLover;
    private TextView mOldLover1, mOldLover2;
    private TextView mNameCHDLover1, mNameCHDLover2;
    private ImageView mImageCHDLover1, mImageCHDLover2;
    private KonfettiView konfettiView;
    private ImageButton ibtMenu;
    //==================================================================
    // Values
    //==================================================================


    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private DatabaseService databaseService ;

    private Handler handler = new Handler();

    // Progress
    private SweetAlertDialog progressDialog;

    // Dialo update infor lover
    private AlertDialog dialogUpdateLocer;

    private String SEX_TYLE = SEX_TYLE_MAN;


    private int mBorderColor = Color.parseColor("#44e91e63");
    private int mBorderWidth = 0;
    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;

    private boolean mShowLoader;
    private String pathImageCrop;
    public String startDate;
    public long numDay = 0;
    public SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(this);
    public boolean isDateEven;
    public List<TimeLine> timeLines;
    //==================================================================
    // Control flg
    //==================================================================

    // Dang chon edit lover nao ? lover1 = 1, lover2 = 2
    private int CLICK_LOVER = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        Log.i("LIFE", "onCreate");
        // Init view
        initView();

        // view pager
        PagerAdapterMain pagerAdapter = new PagerAdapterMain(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        // Set indicator
        circleIndicator.setViewPager(mViewPager);

        // Get data lover 1
        InfoPersonal lover1 = databaseService.getPerson(ID_LOVER_1);
        setInforLover(lover1, Integer.parseInt(ID_LOVER_1));

        // Get data lover 2
        InfoPersonal lover2 = databaseService.getPerson(ID_LOVER_2);
        setInforLover(lover2, Integer.parseInt(ID_LOVER_2));

        // Animation hearth
        animationHearth();

        // Click lover1
        mImageLover1.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                CLICK_LOVER = 1;
                displayDialogSettingProfile(ID_LOVER_1);
            }
        });

        mImageLover2.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                CLICK_LOVER = 2;
                displayDialogSettingProfile(ID_LOVER_2);
            }
        });

        ibtMenu.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                Intent intent = new Intent(getApplicationContext() , MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
            }
        });

        // Show view
        waitProgressActivity();
    }

    private void setColorBar(int colorCd) {
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(colorCd);
        }
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
                aniWaveview(this.getResources().getColor(R.color.color_select_1_1), this.getResources().getColor(R.color.color_select_1));
                break;
            case KEY_THEME_COLOR_2:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_2));
                aniWaveview(this.getResources().getColor(R.color.color_select_2_1), this.getResources().getColor(R.color.color_select_2));
                break;
            case KEY_THEME_COLOR_3:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_3));
                aniWaveview(this.getResources().getColor(R.color.color_select_3_1), this.getResources().getColor(R.color.color_select_3));
                break;
            case KEY_THEME_COLOR_4:
                // Set color win bar
                setColorBar(this.getResources().getColor(R.color.color_bar_4));
                aniWaveview(this.getResources().getColor(R.color.color_select_4_1), this.getResources().getColor(R.color.color_select_4));
                break;
            case KEY_THEME_COLOR_5:
                setColorBar(this.getResources().getColor(R.color.color_bar_5));
                aniWaveview(this.getResources().getColor(R.color.color_select_5_1), this.getResources().getColor(R.color.color_select_5));
                break;
            case KEY_THEME_COLOR_6:
                setColorBar(this.getResources().getColor(R.color.color_bar_6));
                aniWaveview(this.getResources().getColor(R.color.color_select_6_1), this.getResources().getColor(R.color.color_select_6));
                break;
            case KEY_THEME_COLOR_7:
                setColorBar(this.getResources().getColor(R.color.color_bar_7));
                aniWaveview(this.getResources().getColor(R.color.color_select_7_1), this.getResources().getColor(R.color.color_select_7));
                break;
            case KEY_THEME_COLOR_8:
                setColorBar(this.getResources().getColor(R.color.color_bar_8));
                aniWaveview(this.getResources().getColor(R.color.color_select_8_1), this.getResources().getColor(R.color.color_select_8));
                break;
            case KEY_THEME_COLOR_9:
                setColorBar(this.getResources().getColor(R.color.color_bar_9));
                aniWaveview(this.getResources().getColor(R.color.color_select_9_1), this.getResources().getColor(R.color.color_select_9));
                break;
            case KEY_THEME_COLOR_10:
                setColorBar(this.getResources().getColor(R.color.color_bar_10));
                aniWaveview(this.getResources().getColor(R.color.color_select_10_1), this.getResources().getColor(R.color.color_select_10));
                break;
        }
    }
    /**
     * Check to day has even
     * @return
     */
    private boolean isCheckTodayEven(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        boolean isEven = false;
        if (timeLines != null) {
            for (int i = 0; i < timeLines.size(); i++) {
                if (currentDateandTime.equals(timeLines.get(i).getDate())) {
                    isEven = true;
                }
            }
        }

        if (isEven == false && numDay == 100) {
            isEven = true;
        }
         return isEven;
    }

    /**
     * Set wave view
     */
    private void aniWaveview(int color1_1, int color1) {

        numDay = 20;
        if (startDate != null) {
            try {
                numDay = getNumDaysFromCurrentTime(startDate) ;
            } catch (LoveDaysCountDayException e) {
                Toasty.error(this, getString(R.string.error), Toast.LENGTH_SHORT, true).show();
            }
        }

        numDay = (numDay%100) == 0 ? 100 : numDay%100;
        float trsF = (float) ((float)(numDay)/(100*1.0));
        // animation song
        waveView.setWaveColor(color1_1,color1);
        mBorderColor = Color.parseColor("#B0f06292");
        waveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new WaveHelper(waveView, trsF);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setInforLover(InfoPersonal lover, int forLover) {

        // For lover1
        if (lover != null && forLover == 1) {

            mLover1Name.setText(lover.getName());
            setImageSex(lover.getSex(), mImageSexLover1);

            final File file = new File(lover.getImage());
            // Set image
            if (lover.getImage() != null && file.exists()) {
                mImageLover1.setImageURI(null);
                mImageLover1.setImageURI(Uri.parse(lover.getImage()));
            } else {
                mImageLover1.setImageResource(R.drawable.change_image);
            }

            try {
                mOldLover1.setText(Integer.toString(getOld(lover.getBirthDay())));
                setCHD(mNameCHDLover1, mImageCHDLover1, lover.getBirthDay());
            } catch (LoveDaysCountDayException e) {
                Toasty.error(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT, true).show();
            }

        } else if (lover != null && forLover == 2) {

            mLover2Name.setText(lover.getName());
            setImageSex(lover.getSex(), mImageSexLover2);

            final File file = new File(lover.getImage());
            // Set image
            if (lover.getImage() != null && file.exists()) {
                mImageLover2.setImageURI(null);
                mImageLover2.setImageURI(Uri.parse(lover.getImage()));
            } else {
                mImageLover2.setImageResource(R.drawable.change_image);
            }

            try {
                mOldLover2.setText(Integer.toString(getOld(lover.getBirthDay())));
                setCHD(mNameCHDLover2, mImageCHDLover2, lover.getBirthDay());
            } catch (LoveDaysCountDayException e) {
                Toasty.error(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT, true).show();
            }

        }
    }

    /**
     * Find view by ID
     */
    private void initView(){
        // Open db
        databaseService = new DatabaseService(this, this);
        databaseService.open();

        // Create folder
        // createfolderSaveImage();

        // Init
        linearLayoutMain = findViewById(R.id.linearLayoutMain);
        mProgerBarWaitMainInfo = findViewById(R.id.progressMainConentInfo);
        mLayoutContentMainInfoLayout = findViewById(R.id.contentMainInfoLayout);
        mLayoutImageLover1 = findViewById(R.id.lImageLover1);
        mLayoutImageLover2 = findViewById(R.id.lImageLover2);
        mImageHearth = findViewById(R.id.mImageHeart);
        circleIndicator = findViewById(R.id.indicator);
        waveView = findViewById(R.id.wave);
        mViewPager = findViewById(R.id.view_pager_main);
        mImageLover1 = findViewById(R.id.mImageLover1);
        mImageLover2 = findViewById(R.id.mImageLover2);
        mLover1Name = findViewById(R.id.lover1Name);
        mLover2Name = findViewById(R.id.lover2Name);
        mImageSexLover1 = findViewById(R.id.imageSexLover1);
        mImageSexLover2 = findViewById(R.id.imageSexLover2);
        mOldLover1 = findViewById(R.id.mOldLover1);
        mOldLover2 = findViewById(R.id.mOldLover2);
        mNameCHDLover1 = findViewById(R.id.mNameCHDLover1);
        mNameCHDLover2 = findViewById(R.id.mNameCHDLover2);
        mImageCHDLover1 = findViewById(R.id.imageCHDLover1);
        mImageCHDLover2 = findViewById(R.id.imageCHDLover2);
        ibtMenu = findViewById(R.id.ibtMenu);
        // Process wait
        mProgerBarWaitMainInfo.setVisibility(View.VISIBLE);
        setVisibleGroupView(View.INVISIBLE);

        konfettiView = findViewById(R.id.konfettiView);

        // Select data start day
        startDate = databaseService.getStartDay();
        if (startDate == null) {
            databaseService.addDefaultInfo();
            startDate = databaseService.getStartDay();
        }

        timeLines = new ArrayList<>();
        timeLines = databaseService.getAllTimeline();

       settingDisplay();

    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * Seting display
     */
    private void settingDisplay() {

        setColotTheme(saveSharedPreferences.getAppTheme());
        infoApp = databaseService.getInforApp();
        FragmentMain.infoApp = infoApp;

        if (infoApp != null) {
            if (infoApp.getBackground() != null && !infoApp.getBackground().isEmpty()) {
                String path = getRealPathFromURI(Uri.parse(infoApp.getBackground()));
                File file = new File(path);

                if (file.exists()) {
                    Drawable d = Drawable.createFromPath(file.getAbsolutePath());
                    linearLayoutMain.setBackground(d);
                }
            }

//            // Load mussic name bg
//            if (infoApp.getMusicNameBackGround() != null && !infoApp.getMusicNameBackGround().isEmpty()) {
//                textNameSound.setText(infoApp.getMusicNameBackGround());
//                setAnimation(textNameSound);
//            }
//
//            // Load mussic name bg
//            if (infoApp.getMusicNameMess() != null && !infoApp.getMusicNameMess().isEmpty()) {
//                textNameMusicMessage.setText(infoApp.getMusicNameMess());
//                setAnimation(textNameMusicMessage);
//            }
//
//            // Set message
//            if (infoApp.getLoveText() != null) {
//                edtDescriptionLove.setText(infoApp.getLoveText());
//            }
        }
    }

    /**
     * Send data to fragmeny
     * @param mess
     * @param musicName
     * @param musicPath
     */
//    private void sendDataToFragment(String mess, String musicName, String musicPath) {
//        Bundle args = new Bundle();
//        args.putString(APP_LOVE_TEXT, mess);
//        args.putString(APP_MUSIC_MESS_NAME, musicName);
//        args.putString(APP_MUSIC_MESS_PATH, musicPath);
//        FragmentMain fragInfo = new FragmentMain();
//        fragInfo.setArguments(args);
//        transaction.replace(R.id.fragment_main, fragInfo);
//        transaction.commit();
//    }

    /**
     * Set visible group view
     * @param visible
     */
    public void setVisibleGroupView(int visible) {
        mLayoutImageLover1.setVisibility(visible);
        mLayoutImageLover2.setVisibility(visible);
        waveView.setVisibility(visible);
        mViewPager.setVisibility(visible);
        mImageHearth.setVisibility(visible);
        circleIndicator.setVisibility(visible);
        ibtMenu.setVisibility(visible);

    }

    /**
     * Radio sex
     * @param view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.mRadioButtonMan:
                if (checked)
                    SEX_TYLE = SEX_TYLE_MAN;
                    break;
            case R.id.mRadioButtonWomen:
                if (checked)
                    // Ninjas rule
                    SEX_TYLE = SEX_TYLE_WOMAN;
                    break;
            case R.id.mRadioButtonLBGT:
                if (checked)
                    // Ninjas rule
                    SEX_TYLE = SEX_TYLE_LBGT;
                break;
        }
    }

    /**
     * Checked radio Sex
     * @param radioGroup
     * @param value
     */
    private void checkRadioSex (View radioGroup, String value) {
        switch(value) {
            case SEX_TYLE_MAN:
                ((RadioButton)radioGroup.findViewById(R.id.mRadioButtonMan)).setChecked(true);
                break;
            case SEX_TYLE_WOMAN:
                ((RadioButton)radioGroup.findViewById(R.id.mRadioButtonWomen)).setChecked(true);
                break;
            case SEX_TYLE_LBGT:
                ((RadioButton)radioGroup.findViewById(R.id.mRadioButtonLBGT)).setChecked(true);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Seting name, birth day, image profile
     */
    public void displayDialogSettingProfile(final String id) {
        // Show wait progress
        showProcess();

        AlertDialog.Builder dialogBuilder =	new AlertDialog.Builder(this);
        LayoutInflater inflater	= this.getLayoutInflater();
        View dialogView	=	inflater.inflate(R.layout.custom_dialog_input_profile, null);

        // Init view
        final EditText mEditName = dialogView.findViewById(R.id.mEditName);
        final EditText mEditBirthDay = dialogView.findViewById(R.id.mEditBirthDay);
        mImageCropLover = dialogView.findViewById(R.id.imageCropLover);
        RadioGroup mRadioGroupSex = dialogView.findViewById(R.id.radioGroupSex);
        MaterialButton mButtonUpdate =  dialogView.findViewById(R.id.mButtonUpdate);

        // Set data default
        final boolean isExistPerson = databaseService.checkExistPerson(id);
        if (isExistPerson) {
            // Get infor person
            InfoPersonal lover = databaseService.getPerson(id);
            mEditName.setText(lover.getName());
            mEditBirthDay.setText(lover.getBirthDay());
            checkRadioSex(mRadioGroupSex, lover.getSex());
            SEX_TYLE = lover.getSex();

            final File file = new File(lover.getImage());
            // Set image
            if (lover.getImage() != null && file.exists()) {
                mImageCropLover.setImageURI(null);
                mImageCropLover.setImageURI(Uri.parse(lover.getImage()));

                pathImageCrop = lover.getImage();
            } else {
                mImageCropLover.setImageResource(R.drawable.change_image);
            }
        }

        // Crop image
        mImageCropLover.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery();
            }
        });

        // Click button image to crop
        mButtonUpdate.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {

                // Case date format dd/MM/yyyy
                String birthDay = mEditBirthDay.getText().toString().trim();
                if(birthDay.length() == 10 && new DateUtils().isValidDate(birthDay)) {

                    InfoPersonal personal = new InfoPersonal();
                    personal.setId(id);
                    personal.setName(mEditName.getText().toString().trim());
                    personal.setBirthDay(mEditBirthDay.getText().toString().trim());
                    personal.setSex(SEX_TYLE);
                    personal.setImage(pathImageCrop);

                    // Check exist data
                    if (isExistPerson) {
                        // Show wait progress
                        showProcess();

                        // If exist then update
                        databaseService.updatePerson(personal);
                    } else {
                        // Show wait progress
                        showProcess();

                        // If not exist then add
                        databaseService.addPerson(personal);
                    }

                } else {
                    cancelProcess();
                    Toasty.error(getApplicationContext(), getString(R.string.vailidate_startday_error), Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(dialogView);
        alert.setCancelable(true);
        dialogUpdateLocer = alert.create();
        // Wait
        waitProgress(dialogUpdateLocer, true);
      //  dialog.show();
    }

    /**
     * Cancel process wait
     */
    private void cancelProcess() {
        progressDialog.dismiss();
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


    private void animationTimeLove(WaveView waveView) {
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
    }
    /**
     * Animation for hearth
     */
    private void animationHearth() {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(mImageHearth,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(300);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get infor db
        mWaveHelper.start();

    }

    @Override
    public void onComplete() {
        //Update profile success
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void updPersonSuccess(String id) {
        // Update or add succec then set values for lover
        InfoPersonal lover = databaseService.getPerson(id);
        if (ID_LOVER_1.equals(id)) {

            setInforLover(lover, Integer.parseInt(ID_LOVER_1));
        } else if (ID_LOVER_2.equals(id)) {

            setInforLover(lover, Integer.parseInt(ID_LOVER_2));
        }

        waitProgress(dialogUpdateLocer, false);
    }

    @Override
    public void updPersonError(String error) {
        Toasty.error(this, getString(R.string.update_error), Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void getPersonError(String error) {
    }

    /**
     * Set icon sex
     * @param sexValue
     * @param view
     */
    private void setImageSex(String sexValue, ImageView view) {
        switch(sexValue) {
            case SEX_TYLE_MAN:
                view.setVisibility(View.VISIBLE);
                view.setImageResource(R.drawable.iconman);
                break;
            case SEX_TYLE_WOMAN:
                view.setVisibility(View.VISIBLE);
                view.setImageResource(R.drawable.iconwoman);
                break;
            case SEX_TYLE_LBGT:
                view.setVisibility(View.VISIBLE);
                view.setImageResource(R.drawable.iconlbgt);
                break;
             default:
                    view.setVisibility(View.INVISIBLE);
                    break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCHD(TextView view, ImageView imgView, String birthDay) throws LoveDaysCountDayException {
        int cHD = getCHD(birthDay);
        switch (cHD) {
            case BACH_DUONG:
                view.setText(getString(R.string.bd));
                imgView.setImageResource(R.drawable.cungbachduongicon);
                break;
            case KIM_NGUU:
                view.setText(getString(R.string.kn));
                imgView.setImageResource(R.drawable.cungkimnguuicon);
                break;
            case SONG_TU:
                view.setText(getString(R.string.st));
                imgView.setImageResource(R.drawable.cungsongtuicon);
                break;
            case CU_GIAI:
                view.setText(getString(R.string.cg));
                imgView.setImageResource(R.drawable.cungcugiaiicon);
                break;
            case SU_TU:
                view.setText(getString(R.string.sutu));
                imgView.setImageResource(R.drawable.cungsutuicon);
                break;
            case XU_NU:
                view.setText(getString(R.string.xn));
                imgView.setImageResource(R.drawable.cungxunuicon);
                break;
            case THIEN_BINH:
                view.setText(getString(R.string.tb));
                imgView.setImageResource(R.drawable.cungthienbinhicon);
                break;
            case BO_CAP:
                view.setText(getString(R.string.bc));
                imgView.setImageResource(R.drawable.cungbocapicon);
                break;
            case NHAN_MA:
                view.setText(getString(R.string.nm));
                imgView.setImageResource(R.drawable.cungnhanmaicon);
                break;
            case MA_KET:
                view.setText(getString(R.string.mk));
                imgView.setImageResource(R.drawable.cungmaketicon);
                break;
            case BAO_BINH:
                view.setText(getString(R.string.bd));
                imgView.setImageResource(R.drawable.cungbaobinhicon);
                break;
            case SONG_NGU:
                view.setText(getString(R.string.sn));
                imgView.setImageResource(R.drawable.cungsongnguicon);
                break;
        }
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
     * Wait process activity
     */
    private void waitProgressActivity() {
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
                        setUpFadeAnimationLogo (mLayoutContentMainInfoLayout);

                    }
                });
            }
        }).start();
    }

    /**
     * Animation text logo
     * @param constraintLayout
     */
    private void setUpFadeAnimationLogo(final ConstraintLayout constraintLayout) {
        // Start from 0.1f if you desire 90% fade animation
        final Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(500);

        setVisibleGroupView(View.VISIBLE);
        mProgerBarWaitMainInfo.setVisibility(View.GONE);
        constraintLayout.startAnimation(fadeIn);


        if (isCheckTodayEven()) {
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

    }

    /**
     * Pick image
     */
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

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
            if (requestCode == requestMode) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    Toast.makeText(MainInfoActivity.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    /**
     * Handle result crop
     * @param result
     */
    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
           // ResultActivity.startWithUri(SampleActivity.this, resultUri);
            mImageCropLover.setImageURI(null);
            mImageCropLover.setImageURI(resultUri);
            pathImageCrop = resultUri.getPath();
        } else {
            Toast.makeText(MainInfoActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(MainInfoActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainInfoActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crop image
     * @param uri
     */
    private void startCrop(@NonNull Uri uri) {
        // Set name pattern image
        String destinationFileName;
        if (CLICK_LOVER == 1) {
            destinationFileName = SAMPLE_CROPPED_IMAGE_NAME_1;
        } else {
            destinationFileName = SAMPLE_CROPPED_IMAGE_NAME_2;
        }

        destinationFileName += EXTENDTION_PNG;

        // Start crop image
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = advancedConfig(uCrop);

        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
            setupFragment(uCrop);
        } else {                                                        // else start uCrop Activity
            uCrop.start(MainInfoActivity.this);
        }

    }

    public void setupFragment(UCrop uCrop) {
        fragment = uCrop.getFragment(uCrop.getIntent(this).getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, UCropFragment.TAG)
                .commitAllowingStateLoss();

        setupViews(uCrop.getIntent(this).getExtras());
    }

    public void setupViews(Bundle args) {
        mLayoutContentMainInfoLayout.setVisibility(View.GONE);
        mStatusBarColor = args.getInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_statusbar));
        mToolbarColor = args.getInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        mToolbarCancelDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, R.drawable.ucrop_ic_cross);
        mToolbarCropDrawable = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, R.drawable.ucrop_ic_done);
        mToolbarWidgetColor = args.getInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = args.getString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = mToolbarTitle != null ? mToolbarTitle : getResources().getString(R.string.ucrop_label_edit_photo);

      //  setupAppBar();
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setFreeStyleCropEnabled(true);
        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */

        return uCrop.withOptions(options);
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }

        removeFragmentFromScreen();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        mLayoutContentMainInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }
}

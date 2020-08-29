package com.bacnk.lovedays.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bacnk.lovedays.main.adapter.PagerAdapterMain;
import com.bacnk.lovedays.main.database.DatabaseLoverOnListener;
import com.bacnk.lovedays.main.helper.ProgressGenerator;
import com.bacnk.lovedays.main.helper.WaveHelper;
import com.bacnk.lovedays.main.menu.MenuActivity;
import com.bacnk.lovedays.main.time_line.TimeLineActivity;
import com.bacnk.lovedays.main.units.InfoApp;
import com.bacnk.lovedays.main.units.InfoPersonal;
import com.bacnk.lovedays.main.units.TimeLine;
import com.bacnk.lovedays.BuildConfig;
import com.bacnk.lovedays.R;
import com.bacnk.lovedays.common.DateUtils;
import com.bacnk.lovedays.common.SaveSharedPreferences;
import com.bacnk.lovedays.common.exception.LoveDaysCountDayException;
import com.bacnk.lovedays.common.service.SoundService;
import com.bacnk.lovedays.main.crop_image.BaseActivity;
import com.bacnk.lovedays.main.database.DatabaseService;
import com.bacnk.lovedays.main.fragment.FragmentMain;
import com.bacnk.lovedays.common.SingleClickListener;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.bacnk.lovedays.common.DateUtils.getCHD;
import static com.bacnk.lovedays.common.DateUtils.getNumDaysFromCurrentTime;
import static com.bacnk.lovedays.common.DateUtils.getOld;
import static com.bacnk.lovedays.common.LoveCommon.BACH_DUONG;
import static com.bacnk.lovedays.common.LoveCommon.BAO_BINH;
import static com.bacnk.lovedays.common.LoveCommon.BO_CAP;
import static com.bacnk.lovedays.common.LoveCommon.CU_GIAI;
import static com.bacnk.lovedays.common.LoveCommon.DISPLAY_ADS;
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
import static com.bacnk.lovedays.common.LoveCommon.KIM_NGUU;
import static com.bacnk.lovedays.common.LoveCommon.MA_KET;
import static com.bacnk.lovedays.common.LoveCommon.NHAN_MA;
import static com.bacnk.lovedays.common.LoveCommon.SONG_NGU;
import static com.bacnk.lovedays.common.LoveCommon.SONG_TU;
import static com.bacnk.lovedays.common.LoveCommon.SU_TU;
import static com.bacnk.lovedays.common.LoveCommon.THIEN_BINH;
import static com.bacnk.lovedays.common.LoveCommon.XU_NU;
import static com.bacnk.lovedays.main.fragment.BaseFragment.SINGLE_CLICK_CAR_ANIM;

public class MainInfoActivity extends BaseActivity implements
        ProgressGenerator.OnCompleteListener, DatabaseLoverOnListener, UCropFragmentCallback,
        FragmentManager.OnBackStackChangedListener {

    public static Typeface custom_font ;
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

    public InfoApp infoApp;
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


    // Analys
    private FirebaseAnalytics mFirebaseAnalytics;

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private DatabaseService databaseService ;

    private Handler handler = new Handler();

    // Progress
    private ProgressDialog progressDialog;

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

    // Falg click double to back
    boolean doubleBackToExitPressedOnce = false;
    public MediaPlayer mediaPlayer;

    public String currentVersion;
    //==================================================================
    // Control flg
    //==================================================================

    // Dang chon edit lover nao ? lover1 = 1, lover2 = 2
    private int CLICK_LOVER = 1;
    private InterstitialAd mInterstitialAd;

    private InstallStateUpdatedListener installStateUpdatedListener;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        // Init view
        initView();

        // Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.id_admod_full));

        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }

            @Override
            public void onAdClosed() {
                DISPLAY_ADS = false;
                if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mInterstitialAd.loadAd(adRequest);
                }

            }
        });

        // Start sound background
        //start service and play music
        if (saveSharedPreferences.isMusicBackgroundOnOff()) {
            startService(new Intent(MainInfoActivity.this, SoundService.class));
        } else {
            stopService(new Intent(MainInfoActivity.this, SoundService.class));
        }

        // view pager
        PagerAdapterMain pagerAdapter = new PagerAdapterMain(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        // Set indicator
        circleIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (DISPLAY_ADS == true) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showInterstitial();
                        }
                    }, 2000);
                }
            }
        });

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

              // Show ad
                Intent intent = new Intent(MainInfoActivity.this , MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
                finish();
            }
        });


        // Regist topic notification
        // Analys
        if (!saveSharedPreferences.getTopicMess().equals(Locale.getDefault().getLanguage())) {
            FirebaseMessaging.getInstance().subscribeToTopic(Locale.getDefault().getLanguage()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        firebaseAnalysClick(Locale.getDefault().getLanguage(), "language_regist_topic");
                        saveSharedPreferences.saveTopicMess(Locale.getDefault().getLanguage());
                    }

                }
            });
        }

        mFirebaseAnalytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        // Show view
        waitProgressActivity();
    }




    /**
     * show ad
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
    /**
     * Set color bar
     * @param colorCd
     */
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
        custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/scripti.ttf");
        // Open db
        databaseService = new DatabaseService(this, this);
        databaseService.open();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Get current version
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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
        mLover1Name.setTypeface(custom_font);
        mLover2Name = findViewById(R.id.lover2Name);
        mLover2Name.setTypeface(custom_font);
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

    /**
     * Get path
     * @param contentURI
     * @return
     */
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

        }
    }

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
                    firebaseAnalysClick("sex_man", "sex");
                    break;
            case R.id.mRadioButtonWomen:
                if (checked)
                    // Ninjas rule
                    SEX_TYLE = SEX_TYLE_WOMAN;
                     firebaseAnalysClick("sex_woman", "sex");
                    break;
            case R.id.mRadioButtonLBGT:
                if (checked)
                    // Ninjas rule
                    SEX_TYLE = SEX_TYLE_LBGT;
                     firebaseAnalysClick("sex_lgbt", "sex");
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

    /**
     * Show dialog rating
     */
    public void displayDialogRate() {
        // Show wait progress
        showProcess();

        AlertDialog.Builder dialogBuilder =	new AlertDialog.Builder(this);
        LayoutInflater inflater	= this.getLayoutInflater();
        View dialogView	=	inflater.inflate(R.layout.custom_dialog_rating, null);

        // Init view
        final MaterialButton mButtonRatingNo = dialogView.findViewById(R.id.mButtonRatingNo);
        final MaterialButton mButtonRatingYes = dialogView.findViewById(R.id.mButtonRatingYes);

        // Click button update
        mButtonRatingNo.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                rateApp();

                saveSharedPreferences.saveRated(true);
            }
        });

        mButtonRatingYes.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                rateApp();

                saveSharedPreferences.saveRated(true);
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


    /*
     * Start with rating the app
     * Determine if the Play Store is installed on the device
     *
     * */
    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
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

        final int[] oldTextLength = {0};
        final boolean[] isRemove = {false};
        mEditBirthDay.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (oldTextLength[0] > mEditBirthDay.getText().length()) {
                    isRemove[0] = true;
                } else {
                    isRemove[0] = false;

                }

                if (!isRemove[0]) {
                    if (mEditBirthDay.getText().length() == 2 || mEditBirthDay.getText().length() == 5) {
                        mEditBirthDay.setText(mEditBirthDay.getText() + "/");
                        mEditBirthDay.setSelection(mEditBirthDay.getText().length());
                        isRemove[0] = false;
                    }
                }

                oldTextLength[0] = mEditBirthDay.getText().length();
            }
        });


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
                if (pathImageCrop != null && !pathImageCrop.isEmpty()) {
                    if (birthDay.length() == 10 && new DateUtils().isValidDate(birthDay)) {

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

                            // Case the fist setting then add timeline
                            TimeLine timeLine = new TimeLine();
                            timeLine.setId(genIdFromDate());

                            String subject = getString(R.string.default_birday) + " " + personal.getName();
                            timeLine.setSubject(subject);
                            timeLine.setContent(getString(R.string.default_content_birday));
                            timeLine.setType(2 + "");

                            // Birday this year
                            String birthDayTimeline = personal.getBirthDay().substring(0, 6).concat(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                            timeLine.setDate(birthDayTimeline);

                            databaseService.addTimelineNoListener(timeLine);
                        }

                    } else {
                        cancelProcess();
                        Toasty.error(getApplicationContext(), getString(R.string.vailidate_startday_error), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    cancelProcess();
                    Toasty.error(getApplicationContext(), getString(R.string.vailidate_image_error), Toast.LENGTH_SHORT, true).show();
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
     * genaral ID
     * @return String
     */
    private String genIdFromDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
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

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInterstitial();
                }
            }, 2000);
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
                        // do UI work here
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
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

    /**
     * Set framene
     * @param uCrop
     */
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SINGLE_CLICK_CAR_ANIM = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // case not rate then need rate
            if (saveSharedPreferences.getRated()) {
                super.onBackPressed();
                stopService(new Intent(MainInfoActivity.this, SoundService.class));
                return;
            } else {
                displayDialogRate();
            }

        }

        mProgerBarWaitMainInfo.setVisibility(View.VISIBLE);
        setVisibleGroupView(View.INVISIBLE);
        waitProgressActivity();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, this.getString(R.string.double_click_back), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onBackStackChanged() {
        Log.i("LIFE_FRAG", "onBackStackChanged");
    }
}

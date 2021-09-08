package com.bacnk.lovedays.main.menu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bacnk.lovedays.main.database.DatabaseInfoAppListener;
import com.bacnk.lovedays.main.helper.WaveHelper;
import com.bacnk.lovedays.main.units.InfoApp;
import com.bacnk.lovedays.main.units.SongInfor;
import com.bacnk.lovedays.BuildConfig;
import com.bacnk.lovedays.common.SaveSharedPreferences;
import com.bacnk.lovedays.common.SingleClickListener;
import com.bacnk.lovedays.common.service.SoundService;
import com.bacnk.lovedays.main.MainInfoActivity;
import com.bacnk.lovedays.main.database.DatabaseService;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.chip.Chip;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bacnk.lovedays.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.File;

import es.dmoral.toasty.Toasty;

import static com.bacnk.lovedays.common.LoveCommon.EXTENDTION_PNG;
import static com.bacnk.lovedays.common.LoveCommon.IMAGE_BACKGROUND_NAME;
import static com.bacnk.lovedays.common.LoveCommon.INTRO_ACTIVITY;
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
import static com.bacnk.lovedays.common.LoveCommon.MAIN_ACTIVITY;
import static com.bacnk.lovedays.common.LoveCommon.MENU_ACTIVITY;
import static com.bacnk.lovedays.common.LoveCommon.REQUEST_SELECT_PICTURE_FOR_FRAGMENT;
import static com.bacnk.lovedays.common.LoveCommon.TIMELINE_ACTIVITY;

public class MenuActivity extends AppCompatActivity implements DatabaseInfoAppListener, UCropFragmentCallback {

    private static final float LENGTH_A_BYTE = (float) 12.5;
    private static final float SECOND_A_BYTE = (float) 156.25;
    private static final int MAX_LENGTH_MESSAGE = 34;
    // Static
    private static final int IMAGE_RQ = 1;
    private static final int MUSIC_RQ = 2;
    private static final int MUSIC_RQ_MESS = 3;

    private ImageButton buttonBackHome;
    // View layout update background
    private Chip  chipChangeBG;
    private ImageView imageBackground;

    // View layout update music
    private ImageView imageSound;
    private TextView textNameSound;
    private SwitchMaterial switchOnOffSound;
    private CardView cardViewMusic;

    // View layout update message love
    private TextInputEditText edtDescriptionLove;
    private TextView textCount;
    private TextView btnSaveMess;
    private SwitchMaterial switchMusicMessage;
    private Chip chipChooseMusicMess;
    private TextView textNameMusicMessage;
    private LinearLayout linearLayoutMML;

    // View layout update color water
    private TextView colorSelect1;
    private TextView colorSelect2;
    private TextView colorSelect3;
    private TextView colorSelect4;
    private TextView colorSelect5;
    private TextView colorSelect6;
    private TextView colorSelect7;
    private TextView colorSelect8;
    private TextView colorSelect9;
    private TextView colorSelect10;
    private CoordinatorLayout mLayoutMenu;

    // View edit title
    private TextView btnSaveTile;
    private TextInputEditText mEditNameTitleBottom, mEditNameTitleTop;

    // View version
    private TextView txtOldVersion;
    private Chip btnUpgrateVersion;

    private UCropFragment fragment;
    private int requestMode = BuildConfig.RequestMode;
    public boolean isMusicBGChecked;
    public boolean isMusicMessChecked;
    public SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(this);
    private DatabaseService databaseService ;
    private int countText = 0;
    String [] a = null;
    private boolean isReached = false;
    private Uri uri;
    private SongInfor songBG;
    private SongInfor songMessage = new SongInfor();
    private Window window;
    private WaveView waveView;
    private int mBorderColor = Color.parseColor("#44e91e63");
    private int mBorderWidth = 0;
    private WaveHelper mWaveHelper;

    private boolean mShowLoader;
    private String mToolbarTitle;
    @DrawableRes
    private int mToolbarCancelDrawable;
    @DrawableRes
    private int mToolbarCropDrawable;
    // Enables dynamic coloring
    private int mToolbarColor;
    private int mStatusBarColor;
    private int mToolbarWidgetColor;

    private Chip btnRate,btnUpgrateVip;


    // Analys
    private FirebaseAnalytics mFirebaseAnalytics;

    private InterstitialAd mInterstitialAd, mInterstitialAdBack;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();

        // Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAdBack = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.id_admod_full));
        mInterstitialAdBack.setAdUnitId(getString(R.string.id_admod_full));
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }

        // Ads template
        MobileAds.initialize(this, getString(R.string.id_app_admod));
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.id_admod_xenke))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(0xFFFFFF)).build();

                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        if (!mInterstitialAdBack.isLoading() && !mInterstitialAdBack.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAdBack.loadAd(adRequest);
        }

        mInterstitialAdBack.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }

            @Override
            public void onAdClosed() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MenuActivity.this , MainInfoActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                        finish();
                    }
                }, 0);
            }
        });

        aniWaveview(this.getResources().getColor(R.color.color_select_1), this.getResources().getColor(R.color.color_select_1_1));
        // Set color win bar
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.color_menu_bar));
        }

        buttonBackHome.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                showInterstitialBack();
            }
        });

        // Processing for layout update background
        chipChangeBG.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery();
                firebaseAnalysClick("change_background", "menu");
            }
        });

        //===============================================
        // Processing dor layout update music
        setAnimation(textNameSound);

        imageSound.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                stopService(new Intent(MenuActivity.this, SoundService.class));
                pickSoundFromGallery(MUSIC_RQ);
                firebaseAnalysClick("change_music_background", "menu");
            }
        });

        switchOnOffSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSharedPreferences.saveMusicBackgroundOnOff(isChecked);
                if (isChecked) {
                    buttonView.setText("On");
                    imageSound.setVisibility(View.VISIBLE);
                    cardViewMusic.setVisibility(View.VISIBLE);
                    startService(new Intent(MenuActivity.this, SoundService.class));

                    firebaseAnalysClick("change_sound_bg_ON", "menu");
                } else {
                    buttonView.setText("Off");
                    imageSound.setVisibility(View.INVISIBLE);
                    cardViewMusic.setVisibility(View.INVISIBLE);
                    stopService(new Intent(MenuActivity.this, SoundService.class));

                    firebaseAnalysClick("change_sound_bg_OFF", "menu");
                }
            }
        });

        //===============================================
        // Processing dor layout update love message
        setAnimation(textNameMusicMessage);

        // On OFF music mesage
        switchMusicMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSharedPreferences.saveMusicBackgroundMessOnOff(isChecked);
                if (isChecked) {
                    buttonView.setText("On");
                    linearLayoutMML.setVisibility(View.VISIBLE);

                    firebaseAnalysClick("change_sound_mess_ON", "menu");
                } else {
                    buttonView.setText("Off");
                    linearLayoutMML.setVisibility(View.INVISIBLE);

                    firebaseAnalysClick("change_sound_mess_OFF", "menu");
                }
            }
        });

        // Pick sound message
        chipChooseMusicMess.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickSoundFromGallery(MUSIC_RQ_MESS);

                firebaseAnalysClick("change_music_message", "menu");
            }
        });

        // Save message and music mess
        btnSaveMess.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                databaseService.updateInforMess(edtDescriptionLove.getText().toString(),songMessage.getSongName(), songMessage.getPath());
                Toasty.success(getApplicationContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();

                showInterstitial();
                firebaseAnalysClick("save_message", "menu");
            }
        });

        // Add message love
        edtDescriptionLove.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                a = edtDescriptionLove.getText().toString().split("\n");

               // if (a.length > 0) {
                    countText = a[a.length - 1].length();
               // }

                if (edtDescriptionLove.getText().toString().endsWith("\n")) {
                    countText = 0;
                }

                if(countText >= MAX_LENGTH_MESSAGE && !isReached) {
                    edtDescriptionLove.append("\n");
                    isReached = true;
                    countText = a[a.length - 1].length();
                }

                textCount.setText(countText+"");

                // if edittext has less than 10chars & boolean has changed, reset
                if(countText != MAX_LENGTH_MESSAGE  && isReached) isReached = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //===============================================
        // Processing dor layout update color water
        colorSelect1.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
                firebaseAnalysClick("color_1", "choose_color_water");
            }
        });
        colorSelect2.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
                firebaseAnalysClick("color_2", "choose_color_water");
            }
        });
        colorSelect3.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
                firebaseAnalysClick("color_3", "choose_color_water");
            }
        });
        colorSelect4.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
                firebaseAnalysClick("color_4", "choose_color_water");
            }
        });
        colorSelect5.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
                firebaseAnalysClick("color_5", "choose_color_water");
            }
        });
        colorSelect6.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);

                firebaseAnalysClick("color_6", "choose_color_water");
            }
        });
        colorSelect7.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);

                firebaseAnalysClick("color_7", "choose_color_water");
            }
        });
        colorSelect8.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);

                firebaseAnalysClick("color_8", "choose_color_water");
            }
        });
        colorSelect9.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);

                firebaseAnalysClick("color_9", "choose_color_water");
            }
        });
        colorSelect10.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);

                firebaseAnalysClick("color_10", "choose_color_water");
            }
        });

        // process title
        btnSaveTile.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                saveSharedPreferences.saveTitleDisplay(mEditNameTitleTop.getText().toString(), mEditNameTitleBottom.getText().toString());
                Toasty.success(getApplicationContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();
                firebaseAnalysClick("change_title", "menu");
            }
        });

        btnRate.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                rateApp();

            }
        });

        btnUpgrateVip.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                firebaseAnalysClick("upgrate_vip", "menu");
                //Toast.makeText(getApplicationContext(), getString(R.string.menu_upgrate_vip_content), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=com.bacnk.lovedaysvip");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MenuActivity.this, "Unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnUpgrateVersion.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                    //show anything
                    rateApp();
                    firebaseAnalysClick("upgrate_version_yes", "menu");
            }
        });

        // Analys
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseAnalytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);
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

    /**
     * Show ads full
     */
    private void showInterstitialBack() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAdBack != null && mInterstitialAdBack.isLoaded()) {
            mInterstitialAdBack.show();
        } else {
            Intent intent = new Intent(MenuActivity.this , MainInfoActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            finish();
        }
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
     * Analys click
     * @param event
     * @param type
     */
    private void firebaseAnalysClick(String event, String type) {

        // [START image_view_event]
        Bundle bundle = new Bundle();
        bundle.putString("value", event);
        bundle.putString("type", type);
        mFirebaseAnalytics.logEvent(event, bundle);
        bundle.clear();
    }

    /**
     * Innit view
     */
    private void initView() {
        // Open db
        databaseService = new DatabaseService(this, this);
        databaseService.open();

        MENU_ACTIVITY = true;
        INTRO_ACTIVITY = false;
        MAIN_ACTIVITY = false;
        TIMELINE_ACTIVITY = false;

        mLayoutMenu = findViewById(R.id.mLayoutMenu);
        waveView = findViewById(R.id.wave);
        chipChangeBG = findViewById(R.id.chipChangeBG);
        imageBackground = findViewById(R.id.imageBackground);
        imageSound = findViewById(R.id.imageSound);
        textNameSound = findViewById(R.id.textNameSound);
        switchOnOffSound = findViewById(R.id.switchOnOffSound);
        cardViewMusic = findViewById(R.id.cardViewMusic);
        edtDescriptionLove = findViewById(R.id.edtDescriptionLove);
        textCount = findViewById(R.id.textCount);
        switchMusicMessage = findViewById(R.id.switchMusicMessage);
        chipChooseMusicMess = findViewById(R.id.chipChooseMusicMess);
        textNameMusicMessage = findViewById(R.id.textNameMusicMessage);
        linearLayoutMML = findViewById(R.id.linearLayoutMML);
        btnSaveMess = findViewById(R.id.btnSaveMess);
        btnSaveTile = findViewById(R.id.btnSaveTitle);
        mEditNameTitleBottom = findViewById(R.id.mEditNameTitleBottom);
        mEditNameTitleTop = findViewById(R.id.mEditNameTitleTop);
        btnRate = findViewById(R.id.btnRate);
        btnUpgrateVip = findViewById(R.id.btnUpgrateVip);
        txtOldVersion = findViewById(R.id.txtOldVersion);
        btnUpgrateVersion = findViewById(R.id.btnUpgrateVersion);

        colorSelect1 = findViewById(R.id.colorSelect1);
        colorSelect2 = findViewById(R.id.colorSelect2);
        colorSelect3 = findViewById(R.id.colorSelect3);
        colorSelect4 = findViewById(R.id.colorSelect4);
        colorSelect5 = findViewById(R.id.colorSelect5);
        colorSelect6 = findViewById(R.id.colorSelect6);
        colorSelect7 = findViewById(R.id.colorSelect7);
        colorSelect8 = findViewById(R.id.colorSelect8);
        colorSelect9 = findViewById(R.id.colorSelect9);
        colorSelect10 = findViewById(R.id.colorSelect10);

        buttonBackHome = findViewById(R.id.buttonBackHome);

        mEditNameTitleBottom.setText(saveSharedPreferences.getTitleDisplay().getTitleBottom());
        mEditNameTitleTop.setText(saveSharedPreferences.getTitleDisplay().getTitleTop());
        settingDisplay();
    }

    /**
     * Set ting display
     */
    private void settingDisplay() {

        try {
            txtOldVersion.setText(getString(R.string.menu_desp_old_ver) + " " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        isMusicBGChecked = saveSharedPreferences.isMusicBackgroundOnOff();
        switchOnOffSound.setChecked(isMusicBGChecked);
        if (isMusicBGChecked) {
            switchOnOffSound.setText("On");
            imageSound.setVisibility(View.VISIBLE);
            cardViewMusic.setVisibility(View.VISIBLE);
        } else {
            switchOnOffSound.setText("Off");
            imageSound.setVisibility(View.INVISIBLE);
            cardViewMusic.setVisibility(View.INVISIBLE);
        }

        isMusicMessChecked = saveSharedPreferences.isMusicBackgroundMessOnOff();
        switchMusicMessage.setChecked(isMusicMessChecked);
        if (isMusicMessChecked) {
            switchMusicMessage.setText("On");
            linearLayoutMML.setVisibility(View.VISIBLE);
        } else {
            switchMusicMessage.setText("Off");
            linearLayoutMML.setVisibility(View.INVISIBLE);
        }

        // Set color them
        setColotTheme(saveSharedPreferences.getAppTheme());

        // get infor db
        InfoApp infoApp = databaseService.getInforApp();

        if (infoApp != null) {
            // Load image background
            if (infoApp.getBackground() != null && !infoApp.getBackground().isEmpty()) {

                Picasso.with(this).load(infoApp.getBackground()).fit().centerCrop().into(imageBackground);
            }

            // Load mussic name bg
            if (infoApp.getMusicNameBackGround() != null && !infoApp.getMusicNameBackGround().isEmpty()) {
                textNameSound.setText(infoApp.getMusicNameBackGround());
                setAnimation(textNameSound);
            }

            // Load mussic name bg
            if (infoApp.getMusicNameMess() != null && !infoApp.getMusicNameMess().isEmpty()) {
                textNameMusicMessage.setText(infoApp.getMusicNameMess());
                setAnimation(textNameMusicMessage);
            }

            // Set message
            if (infoApp.getLoveText() != null) {
                edtDescriptionLove.setText(infoApp.getLoveText());
            }
        }
    }
    /**
     * Click color select
     * @param v
     */
    private void clickColorSelect(View v) {
        colorSelect1.setBackgroundResource(R.drawable.color_select_1);
        colorSelect2.setBackgroundResource(R.drawable.color_select_2);
        colorSelect3.setBackgroundResource(R.drawable.color_select_3);
        colorSelect4.setBackgroundResource(R.drawable.color_select_4);
        colorSelect5.setBackgroundResource(R.drawable.color_select_5);
        colorSelect6.setBackgroundResource(R.drawable.color_select_6);
        colorSelect7.setBackgroundResource(R.drawable.color_select_7);
        colorSelect8.setBackgroundResource(R.drawable.color_select_8);
        colorSelect9.setBackgroundResource(R.drawable.color_select_9);
        colorSelect10.setBackgroundResource(R.drawable.color_select_10);

        int id = v.getId();
        switch (id) {
            case R.id.colorSelect1:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_1);
                v.setBackgroundResource(R.drawable.color_select_1_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_1_1), this.getResources().getColor(R.color.color_select_1));
                break;
            case R.id.colorSelect2:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_2);
                v.setBackgroundResource(R.drawable.color_select_2_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_2_1), this.getResources().getColor(R.color.color_select_2));
                break;
            case R.id.colorSelect3:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_3);
                v.setBackgroundResource(R.drawable.color_select_3_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_3_1), this.getResources().getColor(R.color.color_select_3));
                break;
            case R.id.colorSelect4:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_4);
                v.setBackgroundResource(R.drawable.color_select_4_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_4_1), this.getResources().getColor(R.color.color_select_4));
                break;
            case R.id.colorSelect5:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_5);
                v.setBackgroundResource(R.drawable.color_select_5_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_5_1), this.getResources().getColor(R.color.color_select_5));
                break;
            case R.id.colorSelect6:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_6);
                v.setBackgroundResource(R.drawable.color_select_6_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_6_1), this.getResources().getColor(R.color.color_select_6));
                break;
            case R.id.colorSelect7:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_7);
                v.setBackgroundResource(R.drawable.color_select_7_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_7_1), this.getResources().getColor(R.color.color_select_7));
                break;
            case R.id.colorSelect8:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_8);
                v.setBackgroundResource(R.drawable.color_select_8_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_8_1), this.getResources().getColor(R.color.color_select_8));
                break;
            case R.id.colorSelect9:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_9);
                v.setBackgroundResource(R.drawable.color_select_9_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_9_1), this.getResources().getColor(R.color.color_select_9));
                break;
            case R.id.colorSelect10:
                saveSharedPreferences.saveAppTheme(KEY_THEME_COLOR_10);
                v.setBackgroundResource(R.drawable.color_select_10_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_10_1), this.getResources().getColor(R.color.color_select_10));
                break;
        }
    }

    /**
     * Click color select
     * @param colorCd
     */
    private void setColotTheme(int colorCd) {
        colorSelect1.setBackgroundResource(R.drawable.color_select_1);
        colorSelect2.setBackgroundResource(R.drawable.color_select_2);
        colorSelect3.setBackgroundResource(R.drawable.color_select_3);
        colorSelect4.setBackgroundResource(R.drawable.color_select_4);
        colorSelect5.setBackgroundResource(R.drawable.color_select_5);
        colorSelect6.setBackgroundResource(R.drawable.color_select_6);
        colorSelect7.setBackgroundResource(R.drawable.color_select_7);
        colorSelect8.setBackgroundResource(R.drawable.color_select_8);
        colorSelect9.setBackgroundResource(R.drawable.color_select_9);
        colorSelect10.setBackgroundResource(R.drawable.color_select_10);

        switch (colorCd) {
            case KEY_THEME_COLOR_1:
                colorSelect1.setBackgroundResource(R.drawable.color_select_1_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_1_1), this.getResources().getColor(R.color.color_select_1));
                break;
            case KEY_THEME_COLOR_2:
                colorSelect2.setBackgroundResource(R.drawable.color_select_2_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_2_1), this.getResources().getColor(R.color.color_select_2));
                break;
            case KEY_THEME_COLOR_3:
                colorSelect3.setBackgroundResource(R.drawable.color_select_3_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_3_1), this.getResources().getColor(R.color.color_select_3));
                break;
            case KEY_THEME_COLOR_4:
                colorSelect4.setBackgroundResource(R.drawable.color_select_4_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_4_1), this.getResources().getColor(R.color.color_select_4));
                break;
            case KEY_THEME_COLOR_5:
                colorSelect5.setBackgroundResource(R.drawable.color_select_5_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_5_1), this.getResources().getColor(R.color.color_select_5));
                break;
            case KEY_THEME_COLOR_6:
                colorSelect6.setBackgroundResource(R.drawable.color_select_6_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_6_1), this.getResources().getColor(R.color.color_select_6));
                break;
            case KEY_THEME_COLOR_7:
                colorSelect7.setBackgroundResource(R.drawable.color_select_7_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_7_1), this.getResources().getColor(R.color.color_select_7));
                break;
            case KEY_THEME_COLOR_8:
                colorSelect8.setBackgroundResource(R.drawable.color_select_8_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_8_1), this.getResources().getColor(R.color.color_select_8));
                break;
            case KEY_THEME_COLOR_9:
                colorSelect9.setBackgroundResource(R.drawable.color_select_9_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_9_1), this.getResources().getColor(R.color.color_select_9));
                break;
            case KEY_THEME_COLOR_10:
                colorSelect10.setBackgroundResource(R.drawable.color_select_10_1);
                aniWaveview(this.getResources().getColor(R.color.color_select_10_1), this.getResources().getColor(R.color.color_select_10));
                break;
        }
    }
    /**
     * Animation text right to left
     * @param textView
     */
    private void setAnimation(TextView textView) {
        int length = textView.length();
        float fromxDelta = 0;
        float duration = 0;
        if (length > 15) {
            fromxDelta = length * LENGTH_A_BYTE;
            duration = SECOND_A_BYTE * length;
        } else {
            fromxDelta = 400;
            duration = 5000;
        }
        TranslateAnimation animation = new TranslateAnimation((float) (fromxDelta*0.5), (float) (-fromxDelta*1.5), 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)

        animation.setDuration((long) duration); // animation duration
        animation.setRepeatCount(Animation.INFINITE); // animation repeat count
        animation.setFillAfter(false);
        textView.startAnimation(animation);//yo
    }
    /**
     * Set wave view
     */
    private void aniWaveview(int color1_1, int color1) {

        // animation song
        waveView.setWaveColor(color1_1,color1);
        mBorderColor = Color.parseColor("#B0f06292");
        waveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new WaveHelper(waveView, 0.9f);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    private void pickSoundFromGallery(int requestMode) {
        Intent videoIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(videoIntent, "Select Audio"), requestMode);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            uri = data.getData();
            if (requestCode == IMAGE_RQ) {
                try {

                    if (uri != null) {
                        startCrop(uri);
                    } else {
                        Toast.makeText(MenuActivity.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);

            } else if (requestCode == MUSIC_RQ) {
                try {

                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MUSIC_RQ);
                    } else {
                        songBG = getAudioPath(uri);
                        databaseService.updateMusicBackground(songBG.getSongName(), songBG.getPath());
                        textNameSound.setText(songBG.getSongName());
                        setAnimation(textNameSound);

                        startService(new Intent(MenuActivity.this, SoundService.class));
                    }
                } catch (Exception e) {
                    //handle exception
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == MUSIC_RQ_MESS) {
                try {

                    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MUSIC_RQ_MESS);
                    } else {
                        songMessage = getAudioPath(uri);
                        textNameMusicMessage.setText(songMessage.getSongName());
                        setAnimation(textNameMusicMessage);
                    }
                } catch (Exception e) {
                    //handle exception
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
            }
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
            imageBackground.setImageURI(null);
            imageBackground.setImageURI(resultUri);
            databaseService.updateImageBackground(resultUri.toString());
        } else {
            Toast.makeText(MenuActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Crop image
     * @param uri
     */
    private void startCrop(@NonNull Uri uri) {
        // Set name pattern image
        String destinationFileName = IMAGE_BACKGROUND_NAME;

        destinationFileName += EXTENDTION_PNG;

        // Start crop image
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = advancedConfig(uCrop);

        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
            setupFragment(uCrop);
        } else {                                                        // else start uCrop Activity
            uCrop.start(MenuActivity.this);
        }

    }

    /**
     * Fragment crop
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
        mLayoutMenu.setVisibility(View.GONE);
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

    /**
     * Set infor song
     * @param uri
     * @return
     */
    private SongInfor getAudioPath(Uri uri) {

        String[] data = {MediaStore.Audio.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        String name = getNameSongFromPath(path);

        SongInfor songInfor = new SongInfor();
        songInfor.setPath(uri.toString());
        songInfor.setSongName(name);
        return songInfor;
    }


    /**
     * Get name song from path
     * @param path
     * @return
     */
    private String getNameSongFromPath(String path) {
        if (path != null) {
            String [] s = path.split("/");
            return s[s.length - 1];
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MUSIC_RQ:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    songBG = getAudioPath(uri);
                    textNameSound.setText(songBG.getSongName());
                    setAnimation(textNameSound);

                    databaseService.updateMusicBackground(songBG.getSongName(), songBG.getPath());
                    startService(new Intent(MenuActivity.this, SoundService.class));

                }
                break;
            case MUSIC_RQ_MESS:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    songMessage = getAudioPath(uri);
                    textNameMusicMessage.setText(songMessage.getSongName());
                    setAnimation(textNameMusicMessage);

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
////        Intent intent = new Intent(getApplicationContext() , MainInfoActivity.class);
////        startActivity(intent);
////        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
////        finish();
        showInterstitialBack();
    }

    @Override
    public void updPersonSuccess(String startDay) {

    }

    @Override
    public void updPersionError(String error) {

    }

    @Override
    public void getPersonError(String error) {

    }

    @Override
    public void updateInforError(String error) {
        Toasty.error(this, getString(R.string.error), Toast.LENGTH_SHORT, true).show();
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(MenuActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MenuActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
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

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        mLayoutMenu.setVisibility(View.VISIBLE);
    }
}

package com.example.lovedays.main.menu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.lovedays.common.SaveSharedPreferences;
import com.example.lovedays.common.SingleClickListener;
import com.example.lovedays.common.exception.LoveDaysCountDayException;
import com.example.lovedays.main.MainInfoActivity;
import com.example.lovedays.main.database.DatabaseInfoAppListener;
import com.example.lovedays.main.database.DatabaseService;
import com.example.lovedays.main.helper.WaveHelper;
import com.example.lovedays.main.units.InfoApp;
import com.example.lovedays.main.units.SongInfor;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovedays.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import es.dmoral.toasty.Toasty;

import static com.example.lovedays.common.DateUtils.getNumDaysFromCurrentTime;
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

public class MenuActivity extends AppCompatActivity implements DatabaseInfoAppListener {

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


    public boolean isMusicBGChecked;
    public boolean isMusicMessChecked;
    public SaveSharedPreferences saveSharedPreferences = new SaveSharedPreferences(this);
    private DatabaseService databaseService ;
    private int countText = 0;
    String [] a = null;
    private boolean isReached = false;
    private Uri uri;
    private SongInfor songBG;
    private SongInfor songMessage;
    private Window window;
    private WaveView waveView;
    private int mBorderColor = Color.parseColor("#44e91e63");
    private int mBorderWidth = 0;
    private WaveHelper mWaveHelper;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();

        aniWaveview(this.getResources().getColor(R.color.color_select_1), this.getResources().getColor(R.color.color_select_1_1));
        // Set color win bar
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.color_menu_bar));
        }

        buttonBackHome.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                Intent intent = new Intent(getApplicationContext() , MainInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });

        // Processing for layout update background
        chipChangeBG.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickFromGallery(IMAGE_RQ);
            }
        });

        //===============================================
        // Processing dor layout update music
        setAnimation(textNameSound);

        imageSound.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickSoundFromGallery(MUSIC_RQ);
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
                } else {
                    buttonView.setText("Off");
                    imageSound.setVisibility(View.INVISIBLE);
                    cardViewMusic.setVisibility(View.INVISIBLE);
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
                } else {
                    buttonView.setText("Off");
                    linearLayoutMML.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Pick sound message
        chipChooseMusicMess.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                pickSoundFromGallery(MUSIC_RQ_MESS);
            }
        });

        // Save message and music mess
        btnSaveMess.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                databaseService.updateInforMess(edtDescriptionLove.getText().toString(),songMessage.getSongName(), songMessage.getPath());
                Toasty.success(getApplicationContext(), getString(R.string.update_success), Toast.LENGTH_SHORT, true).show();
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

                countText = a[a.length - 1].length();
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
            }
        });
        colorSelect2.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect3.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect4.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect5.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect6.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect7.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect8.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect9.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
        colorSelect10.setOnClickListener(new SingleClickListener() {
            @Override
            public void performClick(View v) {
                clickColorSelect(v);
            }
        });
    }

    /**
     * Innit view
     */
    private void initView() {
        // Open db
        databaseService = new DatabaseService(this, this);
        databaseService.open();

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

        settingDisplay();
    }

    /**
     * Set ting display
     */
    private void settingDisplay() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MUSIC_RQ_MESS);
        }

        if (resultCode == RESULT_OK) {
            uri = data.getData();
            if (requestCode == IMAGE_RQ) {
                try {
                    String uriImageBG = data.getData().toString();
                    databaseService.updateImageBackground(uriImageBG);
                    imageBackground.setImageURI(uri);
                } catch (Exception e) {
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }

            }

            // Mussic background
            if (requestCode == MUSIC_RQ) {
                try {
                        songBG = getAudioPath(uri);
                        databaseService.updateMusicBackground(songBG.getSongName() ,songBG.getPath());
                        textNameSound.setText(songBG.getSongName());
                        setAnimation(textNameSound);
                } catch (Exception e) {
                    //handle exception
                    e.printStackTrace();
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
                //   String path1 = uri.getPath();

            }

            // Music message
            if (requestCode == MUSIC_RQ_MESS) {
                try {
                    //    String path = myFile.getAbsolutePath();
                        Log.i("URI", uri.toString());
                        songMessage = getAudioPath(uri);
                        textNameMusicMessage.setText(songMessage.getSongName());
                        setAnimation(textNameMusicMessage);
                } catch (Exception e) {
                    //handle exception
                    e.printStackTrace();
                    Toast.makeText(MenuActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        songInfor.setPath(path);
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
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext() , MainInfoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
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
}

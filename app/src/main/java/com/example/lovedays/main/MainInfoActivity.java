package com.example.lovedays.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.lovedays.BuildConfig;
import com.example.lovedays.R;
import com.example.lovedays.main.adapter.PagerAdapterMain;
import com.example.lovedays.main.crop_image.BaseActivity;
import com.example.lovedays.main.database.DatabaseOnListener;
import com.example.lovedays.main.database.DatabaseService;
import com.example.lovedays.main.helper.ProgressGenerator;
import com.example.lovedays.main.helper.WaveHelper;
import com.example.lovedays.main.units.InfoPersonal;
import com.gelitenight.waveview.library.WaveView;
import com.google.android.material.button.MaterialButton;
import com.q42.android.scrollingimageview.ScrollingImageView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.sql.SQLException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class MainInfoActivity extends BaseActivity implements
        ProgressGenerator.OnCompleteListener, DatabaseOnListener, UCropFragmentCallback {

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
    //==================================================================
    // View
    //==================================================================
    private Window window;
    private ViewPager mViewPager;
    private ImageView mImageHearth;
    private CircleIndicator circleIndicator;

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
    //==================================================================
    // Control flg
    //==================================================================

    // Dang chon edit lover nao ? lover1 = 1, lover2 = 2
    private int CLICK_LOVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);

        // Init view
        initView();

        // animation song
        waveView.setWaveColor(
                Color.parseColor("#40f06292"),
                Color.parseColor("#80f06292"));
        mBorderColor = Color.parseColor("#B0f06292");
        waveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);


        // Set color win bar
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.Pi300));
        }

        // view pager
        PagerAdapterMain pagerAdapter = new PagerAdapterMain(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        // Set indicator
        circleIndicator.setViewPager(mViewPager);

        // Get data lover 1
        InfoPersonal lover1 = databaseService.getPerson(ID_LOVER_1);
        if (lover1 != null) {
            mLover1Name.setText(lover1.getName());
            setImageSex(lover1.getSex(), mImageSexLover1);
            // Set image
            if (lover1.getImage() != null) {
                mImageLover1.setImageURI(null);
                mImageLover1.setImageURI(Uri.parse(lover1.getImage()));
            }
        }

        // Get data lover 2
        InfoPersonal lover2 = databaseService.getPerson(ID_LOVER_2);
        if (lover2 != null) {
            mLover2Name.setText(lover2.getName());
            setImageSex(lover2.getSex(), mImageSexLover2);

            // Set image
            if (lover2.getImage() != null) {
                mImageLover2.setImageURI(null);
                mImageLover2.setImageURI(Uri.parse(lover2.getImage()));
            }
        }

        // Animation hearth
        animationHearth();

        // Click lover1
        mImageLover1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_LOVER = 1;
                displayDialogSettingProfile(ID_LOVER_1);
            }
        });

        mImageLover2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLICK_LOVER = 2;
                displayDialogSettingProfile(ID_LOVER_2);
            }
        });

        // Show view
        waitProgressActivity();
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

        // Process wait
        mProgerBarWaitMainInfo.setVisibility(View.VISIBLE);
        setVisibleGroupView(View.INVISIBLE);
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
            // Set image
            if (lover.getImage() != null) {
                mImageCropLover.setImageURI(null);
                mImageCropLover.setImageURI(Uri.parse(lover.getImage()));

                pathImageCrop = lover.getImage();
            }
        }

        // Click button image to crop
        mImageCropLover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        // Click button update
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
     * Show process
     */
    private void showProcess() {
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void checkValidate() {

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
        mWaveHelper.start();
    }

    @Override
    public void onComplete() {
        //Update profile success
    }


    @Override
    public void updPersonSuccess(String id) {
        // Update or add succec then set values for lover
        InfoPersonal lover = databaseService.getPerson(id);
        if (lover != null && ID_LOVER_1.equals(id)) {

            mLover1Name.setText(lover.getName());
            setImageSex(lover.getSex(), mImageSexLover1);

            if (lover.getImage() != null) {
                mImageLover1.setImageURI(null);
                mImageLover1.setImageURI(Uri.parse(lover.getImage()));
            }
        } else if (lover != null && ID_LOVER_2.equals(id)) {

            mLover2Name.setText(lover.getName());
            setImageSex(lover.getSex(), mImageSexLover2);

            if (lover.getImage() != null) {
                mImageLover2.setImageURI(null);
                mImageLover2.setImageURI(Uri.parse(lover.getImage()));
            }
        }

        waitProgress(dialogUpdateLocer, false);
    }

    @Override
    public void updPersionError(String error) {

    }

    @Override
    public void getPersonError(String error) {
        Log.i("GET_PERSON", error);
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
    }

    private void createfolderSaveImage() {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + APP_NAME);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
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
                    Log.i("CROP_IMAGE", "START_CROP");
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
            Log.i("CROP_IMAGE", resultUri.toString());
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
}

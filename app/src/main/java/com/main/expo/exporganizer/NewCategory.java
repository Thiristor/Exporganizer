package com.main.expo.exporganizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.main.expo.utils.GUIUtils;
import com.main.expo.beans.Categoria;
import com.main.expo.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cmon.main.expo.db.DBHelper;

/**
 * Created by Sergio on 06/02/2018.
 */

public class NewCategory extends Activity {

    private DBHelper catdbh;

    private Bitmap bmp;
    ImageView imageCategory;
    byte[] newImage = null;
    Uri tempUri;
    final int CAMERA_CAPTURE = 1;
    final int CAMERA_CROP = 2;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    View rootLayout;
    FloatingActionButton actionButton;
    private float revealX;
    private float revealY;
    private boolean animEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category);

        catdbh = new DBHelper(this, "DBGlobal", null, 1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setupEnterAnimation();
        }

        imageCategory = findViewById(R.id.imageCategory);

        //InitAnim();

//        //PARA ACOTAR LA ACTIVITY
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int heigh = dm.widthPixels;
//
//        getWindow().setLayout((int)(width), (int)(heigh));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.change_bound_with_arc);
        transition.setDuration(300);
        getWindow().setSharedElementEnterTransition(transition);
        getWindow().getEnterTransition().setStartDelay(300);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                rootLayout = findViewById(R.id.root_layout);
                animateRevealShow(rootLayout);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    private void InitAnim(){
        rootLayout = findViewById(R.id.root_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            rootLayout.setVisibility(View.INVISIBLE);

            final Intent intent = getIntent();

            revealX = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //RadialAnim(rootLayout);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            System.out.println("animEnd: " + animEnd);
//            if(!animEnd){
//                RadialAnimHide(rootLayout);
//            }else{
//                super.onBackPressed();
//            }
//        }
//    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        actionButton = findViewById(R.id.floatingCategory2);
        GUIUtils.animateRevealShow(this, viewRoot, 0, R.color.colorAccent,
                cx, cy, new GUIUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        initViews();
                    }
                }, actionButton);
    }

    private void initViews() {
        actionButton = findViewById(R.id.floatingCategory2);
        actionButton.setVisibility(View.INVISIBLE);
        new Handler(Looper.getMainLooper()).post(() -> {
            Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            animation.setDuration(1300);
//            rootLayout.startAnimation(animation);

        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void RadialAnim(View view) {
//        view.findViewById(R.id.floatingCategory2).setVisibility(View.GONE);
//
//
//        int startRadius = 0;
//        int endRadius = Math.max(view.getWidth(), view.getHeight());
//
//        int cx = (view.getLeft() + view.getRight()) / 2;
//        int cy = (view.getTop() + view.getBottom()) / 2;
//
//        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
//
//        anim.setDuration(800);
////        anim.setStartDelay(80);
////        anim.setInterpolator(new FastOutLinearInInterpolator());
//        anim.start();
////        view.setVisibility(View.VISIBLE);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public void RadialAnimHide(final View view) {
//        final FloatingActionButton actionButton = findViewById(R.id.floatingCategory2);
//
//        int startRadius = view.getWidth();
//        int endRadius = actionButton.getWidth()/2;
//
//        int cx = (view.getLeft() + view.getRight()) / 2;
//        int cy = (view.getTop() + view.getBottom()) / 2;
//
//        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
//
//        anim.setDuration(800);
////        anim.setStartDelay(80);
////        anim.setInterpolator(new FastOutLinearInInterpolator());
//
//        anim.start();
//
//        anim.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                Toast.makeText(NewCategory.this, "FIN", Toast.LENGTH_SHORT).show();
//                animEnd = true;
//                onBackPressed();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//    }

    public void OpenCamera (View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
        }
        if (photoFile != null) {
            tempUri = FileProvider.getUriForFile(this,
                    "com.main.expo.exporganizer.fileprovider",
                    photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(intent, CAMERA_CAPTURE);
        }
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == CAMERA_CAPTURE){
//                Bundle ext = data.getExtras();
//                System.out.println("PRE");
//                System.out.println("URI: " + data.getData());
//                bmp = (Bitmap) ext.get("data");
//                tempUri = ImageUtils.getImageUri(getApplicationContext(), bmp);
                performCrop();
            }else if(requestCode == CAMERA_CROP){
                Picasso.get().load(tempUri).into(imageCategory);

                try {
                    newImage = ImageUtils.getImageByteFromUri(this, tempUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(tempUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1.5);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            //set permissions to read/write uri
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CAMERA_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public void InsertCategory(View view){

        SQLiteDatabase db = catdbh.getWritableDatabase();

        if(db != null) {
            EditText txtName = (EditText) findViewById(R.id.txtName);
            EditText txtDescription = (EditText) findViewById(R.id.txtDescription);

            ContentValues values = new ContentValues();
            values.put(Categoria.COLUMN_NAME_TITLE, txtName.getText().toString());
            values.put(Categoria.COLUMN_NAME_DESCRIPTION, txtDescription.getText().toString());

            values.put(Categoria.COLUMN_NAME_IMAGE, newImage);


            long newRowId = db.insert(Categoria.TABLE_NAME, null, values);
            System.out.println("newRowId: " +  newRowId);

            //Cerramos la base de datos
            db.close();

            //Ponemos el id de la categoria como nombre de la transición
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageCategory.setTransitionName(String.valueOf(newRowId));
            }

            finish();
        }
    }
}

package com.main.expo.exporganizer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;
import com.main.expo.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cmon.main.expo.db.DBHelper;

/**
 * Created by Sergio on 07/02/2018.
 */

public class NewItem extends Activity {

    private DBHelper catdbh;
    private ImageView imageItem;
    private Uri tempUri;
    private String mCurrentPhotoPath;
    final int CAMERA_CAPTURE = 1;
    final int CAMERA_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catdbh = new DBHelper(this, "DBGlobal", null, 1);

        setContentView(R.layout.new_item);

        imageItem = findViewById(R.id.imageItem);

        Setup();
    }

    public void Setup(){

            View rootView = findViewById(android.R.id.content);

            final List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(
                    rootView, TextInputLayout.class);

            ImageView button = findViewById(R.id.createButton2);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean noErrors = true;
                    for (TextInputLayout textInputLayout : textInputLayouts) {
                        String editTextString = textInputLayout.getEditText().getText().toString();
                        if (editTextString.isEmpty()) {
                            textInputLayout.setError("No puede ser vacio");
                            noErrors = false;
                        } else {
                            textInputLayout.setError(null);
                        }
                    }

                    if (noErrors) {
                        // All fields are valid!
                        InsertItem();
                    }
                }
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == CAMERA_CAPTURE){
                performCrop();
            }else if(requestCode == CAMERA_CROP){
                Picasso.get().load(tempUri).into(imageItem);
            }
        }
    }

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

    //TODO MOVER A ...
    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(tempUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 2);
            //indicate output X and Y - 256
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 1024);
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

    //TODO MOVER A FILEUTIL
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

    public void InsertItem(){

        SQLiteDatabase db = catdbh.getWritableDatabase();

        if(db != null) {
            EditText txtName = (EditText) findViewById(R.id.txtName2);
            EditText txtDescription = (EditText) findViewById(R.id.txtDescription2);
            EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity2);
            EditText txtSeries = (EditText) findViewById(R.id.txtSeries2);
            EditText txtPrice = (EditText) findViewById(R.id.txtPrice2);

            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_NAME_TITLE, txtName.getText().toString());
            values.put(Item.COLUMN_NAME_DESCRIPTION, txtDescription.getText().toString());
            values.put(Item.COLUMN_NAME_IMAGE, mCurrentPhotoPath);
            values.put(Item.COLUMN_NAME_QUANTITY, txtQuantity.getText().toString());
            values.put(Item.COLUMN_NAME_SOLD, 0);
            values.put(Item.COLUMN_NAME_PRICE, txtPrice.getText().toString());
            values.put(Item.COLUMN_NAME_SERIES, txtSeries.getText().toString());
            values.put(Item.COLUMN_NAME_CATEGORY, getIntent().getStringExtra("categoryId"));

            long newRowId = db.insert(Item.TABLE_NAME, null, values);
            System.out.println("newRowId: " +  newRowId + " _ID: " + getIntent().getStringExtra("categoryName"));

            //Cerramos la base de datos
            db.close();

            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}

package com.main.expo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//    Uri tempUri = getImageUri(getApplicationContext(), bmp);

    // CALL THIS METHOD TO GET THE ACTUAL PATH
//            File finalFile = new File(getRealPathFromURI(tempUri));

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static byte[] getImageByte(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bArray = bytes.toByteArray();
        return bArray;
    }

    public static Bitmap getImageUriFromByte(Context context, byte[] bArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
        //return getImageUri(context, bitmap);
        return bitmap;
    }

    public static byte[] getImageByteFromUri(Context context, Uri tmpUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), tmpUri);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bytes);
        byte[] bArray = bytes.toByteArray();
        return bArray;
    }
}

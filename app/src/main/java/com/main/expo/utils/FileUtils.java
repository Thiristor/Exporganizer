package com.main.expo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtils {
    /**
     * Get a bitmap from file and try to resize it to be 300x300 pixels.
     *
     * @param filepath Path to the image file.
     * @param reqWidth Requested width.
     * @param reqHeight Requested height.
     * @return Bitmap of image.
     */
    public static Bitmap getResizedBitmap(String filepath, int reqWidth, int reqHeight) {
        Bitmap bitmap;

        // Decode bitmap to get current dimensions.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, options);

        // Calculate sample size of bitmap.
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap again, setting the new dimensions.
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filepath, options);

        // Return resized bitmap.
        return bitmap;
    }

    /**
     * Calculate an acceptable size based on a requested size.
     *
     * @param options Bitmap options.
     * @param reqWidth Requested width.
     * @param reqHeight Requested height.
     * @return Sample size based on requested dimensions.
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image.
        final int height = options.outHeight;
        final int width = options.outWidth;

        // Initialize sample size.
        int inSampleSize = 1;

        // If image is larger than requested in at least one dimension.
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width.
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as sample size value. This will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        // Return sample size.
        return inSampleSize;
    }
}

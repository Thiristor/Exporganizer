package com.main.expo.beans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.main.expo.exporganizer.R;
import com.main.expo.utils.FileUtils;

/**
 * Created by Sergio on 06/02/2018.
 */

public class Categoria implements BaseColumns , Parcelable{
    public static final String TABLE_NAME = "Categorias";
    public static final String COLUMN_NAME_IMAGE = "Foto";
    public static final String COLUMN_NAME_TITLE = "Nombre";
    public static final String COLUMN_NAME_DESCRIPTION = "Descripcion";

    private int id;
    //private byte[] imageId;
    private String imagePath;
    private String name;
    private String description;

    public Categoria(int id, String imagePath, String name, String description) {
        this.id = id;
        //this.imageId = imageId;
        this.imagePath = imagePath;
        this.name = name;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public byte[] getImageId() {
//        return imageId;
//    }
//
//    public void setImageId(byte[] imageId) {
//        this.imageId = imageId;
//    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        //dest.writeByteArray(imageId);
        dest.writeString(imagePath);
        dest.writeString(name);
        dest.writeString(description);
    }

    public Categoria(Parcel in) {
        id = in.readInt();
        //imageId = in.createByteArray();
        imagePath = in.readString();
        name = in.readString();
        description = in.readString();
    }

    public static final Parcelable.Creator<Categoria> CREATOR
            = new Parcelable.Creator<Categoria>() {
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };

    // TODO Image methods

    public boolean hasImage() {

        return getImagePath() != null && !getImagePath().isEmpty();
    }

    public Drawable getThumbnail(Context context) {

        return getScaledImage(context, 128, 128);
    }

    public Drawable getImage(Context context) {

        return getScaledImage(context, 512, 512);
    }

    private Drawable getScaledImage(Context context, int reqWidth, int reqHeight) {

        // If profile has a Image.
        if (hasImage()) {

            // Decode the input stream into a bitmap.
            Bitmap bitmap = FileUtils.getResizedBitmap(getImagePath(), reqWidth, reqHeight);

            // If was successfully created.
            if (bitmap != null) {

                // Return a drawable representation of the bitmap.
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        // Return the default image drawable.
        return context.getResources().getDrawable(R.drawable.bell);
    }
}

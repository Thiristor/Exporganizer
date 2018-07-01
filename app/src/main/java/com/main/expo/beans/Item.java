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
 * Created by Sergio on 07/02/2018.
 */

public class Item implements BaseColumns, Parcelable {

    public static final String TABLE_NAME = "Items";
    public static final String COLUMN_NAME_IMAGE = "Foto";
    public static final String COLUMN_NAME_TITLE = "Nombre";
    public static final String COLUMN_NAME_DESCRIPTION = "Descripcion";
    public static final String COLUMN_NAME_QUANTITY = "Cantidad";
    public static final String COLUMN_NAME_SOLD = "Vendido";
    public static final String COLUMN_NAME_PRICE = "Precio";
    public static final String COLUMN_NAME_SERIES = "Serie";
    public static final String COLUMN_NAME_CATEGORY = "Categoria";

    private int id;
    private String imagePath;
    private String name;
    private String description;
    private int quantity;
    private int sold;
    private float price;
    private String series;
    private String category;

    public Item(int id, String imagePath, String name, String description, int quantity, int sold, float price, String series, String category) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.sold = sold;
        this.price = price;
        this.series = series;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(imagePath);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(quantity);
        dest.writeInt(sold);
        dest.writeFloat(price);
        dest.writeString(series);
        dest.writeString(category);
    }
    public Item(Parcel in) {
        this.id = in.readInt();
        this.imagePath = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.quantity = in.readInt();
        this.sold = in.readInt();
        this.price = in.readFloat();
        this.series = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
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

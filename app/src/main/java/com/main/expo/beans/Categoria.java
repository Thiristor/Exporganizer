package com.main.expo.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * Created by Sergio on 06/02/2018.
 */

public class Categoria implements BaseColumns , Parcelable{
    public static final String TABLE_NAME = "Categorias";
    public static final String COLUMN_NAME_IMAGE = "Foto";
    public static final String COLUMN_NAME_TITLE = "Nombre";
    public static final String COLUMN_NAME_DESCRIPTION = "Descripcion";

    private int id;
    private byte[] imageId;
    private String name;
    private String description;

    public Categoria(int id, byte[] imageId, String name, String description) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.description = description;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImageId() {
        return imageId;
    }

    public void setImageId(byte[] imageId) {
        this.imageId = imageId;
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
        dest.writeByteArray(imageId);
        dest.writeString(name);
        dest.writeString(description);
    }

    public Categoria(Parcel in) {
        id = in.readInt();
        imageId = in.createByteArray();
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
}

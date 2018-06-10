package com.main.expo.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

public class Evento implements BaseColumns, Parcelable {

    public static final String TABLE_NAME = "Eventos";
    public static final String COLUMN_NAME_IMAGE = "Foto";
    public static final String COLUMN_NAME_TITLE = "Nombre";
    public static final String COLUMN_NAME_DESCRIPTION = "Descripcion";
    public static final String COLUMN_NAME_INVENTORY = "Inventario";
    //TODO - EVENTO BEAN AÑADIR GRAFICAS U OTROS DATOS EN BD

    private int id;
    private int imageId;
    private String name;
    private String description;
    private int inventory;

    public Evento(int id, int imageId, String name, String description, int inventory) {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.description = description;
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
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

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeInt(imageId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(inventory);
    }

    public Evento(Parcel in) {
        id = in.readInt();
        imageId = in.readInt();
        name = in.readString();
        description = in.readString();
        inventory = in.readInt();
    }

    public static final Parcelable.Creator<Evento> CREATOR
            = new Parcelable.Creator<Evento>() {
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
}

package com.main.expo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cmon.main.expo.db.DBHelper;

public class DBUtils {

    private DBHelper catdbh;
    private SQLiteDatabase db;

    public DBUtils(Context context) {
        catdbh = new DBHelper(context, "DBGlobal", null, 1);
    }

    public List<Item> GetItemsFromCategory(String categoryId){

        List<Item>itemList = new ArrayList<>();

        db = catdbh.getReadableDatabase();

        if (db != null) {
            String[] projection = {
                    Item._ID,
                    Item.COLUMN_NAME_TITLE,
                    Item.COLUMN_NAME_DESCRIPTION,
                    Item.COLUMN_NAME_IMAGE,
                    Item.COLUMN_NAME_QUANTITY,
                    Item.COLUMN_NAME_SOLD,
                    Item.COLUMN_NAME_PRICE,
                    Item.COLUMN_NAME_SERIES,
                    Item.COLUMN_NAME_CATEGORY
            };

            String selection = Item.COLUMN_NAME_CATEGORY + " = ?";
            String[] selectionArgs = { categoryId };

            Cursor cursor = db.query(
                    Item.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (cursor != null) {
                try{
                    while(cursor.moveToNext()) {

                        itemList.add(
                                new Item(cursor.getInt(cursor.getColumnIndexOrThrow(Item._ID)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_IMAGE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_TITLE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_DESCRIPTION)),
                                        cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_QUANTITY)),
                                        cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SOLD)),
                                        cursor.getFloat(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_PRICE)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SERIES)),
                                        cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_CATEGORY))));
                    }
                } finally {
                    cursor.close();
                }
            }
            db.close();
        }
        return itemList;
    }

    //TODO Hacer genérica
    public Item GetItem(String itemId){

        Item itemResult = null;

        db = catdbh.getReadableDatabase();

        if (db != null) {
            String[] projection = {
                    Item._ID,
                    Item.COLUMN_NAME_TITLE,
                    Item.COLUMN_NAME_DESCRIPTION,
                    Item.COLUMN_NAME_IMAGE,
                    Item.COLUMN_NAME_QUANTITY,
                    Item.COLUMN_NAME_SOLD,
                    Item.COLUMN_NAME_PRICE,
                    Item.COLUMN_NAME_SERIES,
                    Item.COLUMN_NAME_CATEGORY
            };

            String selection = Item._ID + " = ?";
            String[] selectionArgs = { itemId };

            Cursor cursor = db.query(
                    Item.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (cursor != null) {
                try{
                    while(cursor.moveToNext()) {
                        itemResult = new Item(cursor.getInt(cursor.getColumnIndexOrThrow(Item._ID)),
                                cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_IMAGE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_TITLE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_DESCRIPTION)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_QUANTITY)),
                                cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SOLD)),
                                cursor.getFloat(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_PRICE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SERIES)),
                                cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_CATEGORY)));
                    }
                } finally {
                    cursor.close();
                }
            }
            db.close();
        }
        return itemResult;
    }

    //TODO Cambiar para borrar items de la categoria por su ID, NO por su nombre, se podrian borrar otros items por error
    public void DeleteFromTable(String type, String id, String categoryName) {
        db = catdbh.getWritableDatabase();

        if(db != null) {
            String selection = Categoria._ID + " LIKE ?";
            String[] selectionArgs = { id };
            db.delete(type, selection, selectionArgs);

            if(type.equals(Categoria.TABLE_NAME)){
                String selection2 = Item.COLUMN_NAME_CATEGORY + " LIKE ?";
                String[] selectionArgs2 = { categoryName };
                db.delete(Item.TABLE_NAME, selection2, selectionArgs2);
            }
        }
    }

    public void UpdateSoldItem(String id, int sold){
            db = catdbh.getReadableDatabase();

            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_NAME_SOLD, sold);

            String selection = Item._ID + " LIKE ?";
            String[] selectionArgs = {id};

            int count = db.update(
                    Item.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
    }
}

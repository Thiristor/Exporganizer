package com.main.expo.exporganizer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;

import java.util.Locale;

import cmon.main.expo.db.DBHelper;

/**
 * Created by Sergio on 07/02/2018.
 */

public class NewItem extends Activity {

    private DBHelper catdbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        catdbh = new DBHelper(this, "DBGlobal", null, 1);

        setContentView(R.layout.new_item);

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int heigh = dm.widthPixels;

        getWindow().setLayout((int)(width), (int)(heigh));*/
    }

    public void InsertItem(View view){

        SQLiteDatabase db = catdbh.getWritableDatabase();

        if(db != null) {
            EditText txtName = (EditText) findViewById(R.id.txtName);
            EditText txtDescription = (EditText) findViewById(R.id.txtDescription);
            EditText txtQuantity = (EditText) findViewById(R.id.txtQuantity);
            EditText txtSeries = (EditText) findViewById(R.id.txtSeries);
            EditText txtPrice = (EditText) findViewById(R.id.txtPrice);

            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_NAME_TITLE, txtName.getText().toString());
            values.put(Item.COLUMN_NAME_DESCRIPTION, txtDescription.getText().toString());
            values.put(Item.COLUMN_NAME_IMAGE, R.drawable.bell);
            values.put(Item.COLUMN_NAME_QUANTITY, txtQuantity.getText().toString());
            values.put(Item.COLUMN_NAME_SOLD, 0);
            values.put(Item.COLUMN_NAME_PRICE, txtPrice.getText().toString());
            values.put(Item.COLUMN_NAME_SERIES, txtSeries.getText().toString());
            values.put(Item.COLUMN_NAME_CATEGORY, getIntent().getStringExtra("categoryId"));

            long newRowId = db.insert(Item.TABLE_NAME, null, values);
            System.out.println("newRowId: " +  newRowId + " _ID: " + getIntent().getStringExtra("categoryName"));

            //Cerramos la base de datos
            db.close();

            finish();
        }
    }
}

package com.main.expo.exporganizer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.expo.beans.Item;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLOutput;

import cmon.main.expo.db.DBHelper;

public class ItemContent extends AppCompatActivity {

    private Item item;
    private int position;
    private DBHelper catdbh;

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemDescription;
    private TextView itemSeries;
    private TextView itemQuantity;
    private TextView itemSold;
    private TextView itemPrice;
    private boolean isItemSold;
    private boolean isItemDeleted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_content);

        catdbh =
                new DBHelper(this, "DBGlobal", null, 1);

        item = (Item) getIntent().getParcelableExtra("item");
        position = (int) getIntent().getIntExtra("position", 0);

        GetViews();
        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LoadData();
    }

    @Override
    public void onBackPressed() {
        System.out.println("SENJUTO - BACK : " + isItemSold + " POS: " + position);
        if (isItemDeleted) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isItemDeleted", isItemDeleted);
            resultIntent.putExtra("itemPosition", position);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else if (isItemSold) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isItemSold", isItemSold);
            resultIntent.putExtra("itemPosition", position);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void GetViews(){
        itemImage = (ImageView) findViewById(R.id.itemImage);
        itemName = (TextView)findViewById(R.id.itemName);
        itemDescription = (TextView)findViewById(R.id.itemDescription);
        itemSeries = (TextView)findViewById(R.id.itemSeries);
        itemQuantity = (TextView)findViewById(R.id.itemQuantity);
        itemSold = (TextView)findViewById(R.id.itemSold);
        itemPrice = (TextView)findViewById(R.id.itemPrice);
    }


    private void LoadData(){
        File file = new File(item.getImagePath());

        Picasso.get().load(file).into(itemImage);
        System.out.println("IMAGE PATH: " + file.getAbsolutePath());
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemSeries.setText(item.getSeries());
        itemQuantity.setText("Te quedan " + String.valueOf((item.getQuantity())-item.getSold()));
        itemSold.setText("Has vendido " + String.valueOf(item.getSold()));
        itemPrice.setText("Precio: " + String.valueOf(item.getPrice() + "€"));
    }

    public void DeleteItem(View view){
        new AlertDialog.Builder(this)
                .setTitle("¿Desea borrar el articulo?")
                .setMessage("Si borra el articulo se perderan sus datos y no podra recuperarlo.")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = catdbh.getWritableDatabase();

                        if(db != null) {
                            String selection = Item.COLUMN_NAME_TITLE + " LIKE ?";
                            String[] selectionArgs = { item.getName() };
                            db.delete(Item.TABLE_NAME, selection, selectionArgs);
                        }

                        isItemDeleted = true;
                        onBackPressed();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void SoldItem(View view){
        if(item.getSold() < item.getQuantity()) {
            SQLiteDatabase db = catdbh.getReadableDatabase();

            item.setSold(item.getSold() + 1);

            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_NAME_SOLD, item.getSold());

            String selection = Item.COLUMN_NAME_TITLE + " LIKE ?";
            String[] selectionArgs = {item.getName()};

            int count = db.update(
                    Item.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        isItemSold = true;

        onResume();
    }
}

package com.main.expo.exporganizer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.expo.beans.Item;

import cmon.main.expo.db.DBHelper;

public class ItemContent extends AppCompatActivity {

    private Item item;
    private DBHelper catdbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_content);

        catdbh =
                new DBHelper(this, "DBGlobal", null, 1);

        item = (Item) getIntent().getParcelableExtra("item");

        LoadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LoadData();
    }

    public void LoadData(){
        TextView itemName = (TextView)findViewById(R.id.itemName);
        TextView itemDescription = (TextView)findViewById(R.id.itemDescription);
        TextView itemSeries = (TextView)findViewById(R.id.itemSeries);
        TextView itemQuantity = (TextView)findViewById(R.id.itemQuantity);
        TextView itemSold = (TextView)findViewById(R.id.itemSold);
        TextView itemPrice = (TextView)findViewById(R.id.itemPrice);

        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemSeries.setText(item.getSeries());
        itemQuantity.setText("Te quedan " + String.valueOf((item.getQuantity())-item.getSold()));
        itemSold.setText("Has vendido " + String.valueOf(item.getSold()));
        itemPrice.setText("Precio: " + String.valueOf(item.getPrice() + "€"));
    }

    public void DeleteItem(View view){
        SQLiteDatabase db = catdbh.getWritableDatabase();

        if(db != null) {
            String selection = Item.COLUMN_NAME_TITLE + " LIKE ?";
            String[] selectionArgs = { item.getName() };
            db.delete(Item.TABLE_NAME, selection, selectionArgs);
        }

        onBackPressed();
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

        onResume();
    }
}

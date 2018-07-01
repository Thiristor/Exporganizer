package com.main.expo.exporganizer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.main.expo.beans.Item;
import com.main.expo.utils.DBUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.SQLOutput;

import cmon.main.expo.db.DBHelper;

public class ItemContent extends AppCompatActivity {

    private Item item;
    private int position;
    private DBUtils dbUtils;

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

        dbUtils = new DBUtils(this);

        item = (Item) getIntent().getParcelableExtra("item");
        position = (int) getIntent().getIntExtra("position", 0);

        GenerateQR(item.getId());

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

                        dbUtils.DeleteFromTable(Item.TABLE_NAME, String.valueOf(item.getId()), null);

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
            item.setSold(item.getSold() + 1);

            dbUtils.UpdateSoldItem(String.valueOf(item.getId()), item.getSold());
        }

        isItemSold = true;

        onResume();
    }

    private void GenerateQR(int id){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String text = "ITEM:".concat(String.valueOf(id));
        System.out.println("SENJUTO - QR: " + text);

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView codeImage = findViewById(R.id.qrcode);
            codeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void UndoSold(View view){
        if(item.getSold() != 0) {
            item.setSold(item.getSold() - 1);
            dbUtils.UpdateSoldItem(String.valueOf(item.getId()), item.getSold());
        }
    }
}

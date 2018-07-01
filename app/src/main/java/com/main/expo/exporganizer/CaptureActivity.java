package com.main.expo.exporganizer;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import com.main.expo.beans.Item;
import com.main.expo.utils.DBUtils;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CaptureActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private DBUtils dbUtils;

    private Item item = null;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
            SearchItem(lastText, imageView);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        dbUtils = new DBUtils(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    //----------------------------------------

    private void SearchItem(String textCode, ImageView imageView){
        String[] split = textCode.split(":");
        String header = split[0];
        String id = split[1];
        if(header.equals("ITEM")){
            item = dbUtils.GetItem(id);
            if(item != null){
                if(item.getSold() < item.getQuantity()) {
                    item.setSold(item.getSold() + 1);
                    dbUtils.UpdateSoldItem(String.valueOf(item.getId()), item.getSold());

                    File f = new File(item.getImagePath());
                    Picasso.get().load(f).fit().into(imageView);

                    Snackbar.make(imageView, "Articulo vendido", Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    System.out.println("SENJUTO - DESHACER: ");
                                    item.setSold(item.getSold() - 1);
                                    dbUtils.UpdateSoldItem(String.valueOf(item.getId()), item.getSold());
                                }
                            }).show();
                }else{
                    Snackbar.make(imageView, "Sin existencias", Snackbar.LENGTH_LONG)
                            .show();
                }
            }else{
                Snackbar.make(imageView, "No existe en el inventario", Snackbar.LENGTH_LONG)
                        .show();
            }

        }else if(header.equals("CATEGORY")){

        }else{

        }
    }
}

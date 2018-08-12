package com.main.expo.exporganizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.main.expo.adapter.GridViewAdapter;
import com.main.expo.adapter.GridViewItemAdapter;
import com.main.expo.adapter.ItemTouchHelperAdapter;
import com.main.expo.adapter.ItemsAdapter;
import com.main.expo.adapter.ListViewAdapter;
import com.main.expo.adapter.ListViewItemAdapter;
import com.main.expo.adapter.SimpleItemTouchHelperCallback;
import com.main.expo.adapter.TestAdapter;
import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;
import com.main.expo.utils.DBUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cmon.main.expo.db.DBHelper;

public class CategoryContent extends Fragment implements SearchView.OnQueryTextListener {

    private DBUtils dbUtils;

    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewItemAdapter listViewAdapter;
    private GridViewItemAdapter gridViewAdapter;
    private List<Item> itemList;
    private int currentViewMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    private Categoria categoria;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayaoutManager;

    View rootView;

    private boolean itemInserted = false;
    private boolean isItemSold = false;
    private boolean isItemDeleted = false;
    private int itemPosition = 0;
    static final int ITEM_OPEN = 1;
    static final int ITEM_NEW = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_category_content, container, false);

        initBottomBar();

        dbUtils = new DBUtils(getContext());

//        categoria = (Categoria) getActivity().getIntent().getParcelableExtra("categoria");
        categoria = (Categoria) getArguments().getParcelable("categoria");

        System.out.println("SENJUTO - CATEGORIA ID: " + categoria.getId());

//        stubList = findViewById(R.id.stub_list2);
//        stubGrid = findViewById(R.id.stub_grid2);

        //Inflate ViewStub before get view

//        stubList.inflate();
//        stubGrid.inflate();
//
//        listView = findViewById(R.id.mylistview);
//        gridView = findViewById(R.id.mygridview);

        //Get list of products
        LoadData();

        //Get current view mode in share references
//        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW); //Default

        //Register item click
//        listView.setOnItemClickListener(onItemClickListener);
//        gridView.setOnItemClickListener(onItemClickListener);

        //SwitchView();
        LoadRecycler();

        //DRAG AND DROP
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    public void initBottomBar (){
        BottomAppBar bottomAppBar = rootView.findViewById(R.id.bottom_appbar);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);

        bottomAppBar.replaceMenu(R.menu.bottom_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                else {
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                }
            }
        });

        Menu menu = bottomAppBar.getMenu();
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

    }

    public void LoadRecycler() {
        mRecyclerView = rootView.findViewById(R.id.itemrecycler);
        mLayaoutManager = new LinearLayoutManager(getContext());


        mAdapter = new ItemsAdapter(itemList, R.layout.list_item, new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position, ImageView imageViewName) {
//                GenerateSheet();
                OpenItem(position);
            }
        });

        mRecyclerView.setHasFixedSize(true);

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayaoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    /*@Override
    protected void onSaveInstanceState(Bundle state) {
        System.out.println("Guardando estado...");
        super.onSaveInstanceState(state);
        state.putParcelable("state",listView.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        System.out.println("Cargando estado...");
        super.onRestoreInstanceState(state);
        // Set new items
        if(state.getParcelable("state") != null) {
            listView.onRestoreInstanceState(state.getParcelable("state"));
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("SENJUTO - KOKODA: " + resultCode + " " + requestCode);
        if(resultCode == Activity.RESULT_OK){
            System.out.println("SENJUTO - RESULT OK");
            if(requestCode == ITEM_OPEN){
                isItemSold = data.getExtras().getBoolean("isItemSold");
                itemPosition = data.getExtras().getInt("itemPosition");
                isItemDeleted = data.getExtras().getBoolean("isItemDeleted");

            }else if(requestCode == ITEM_NEW){
                itemInserted = true;
            }
        }else{
            System.out.println("SENJUTO - RESULT NO OK");
        }
    }

    @Override
    public void onResume() {
        System.out.println("SENJUTO - ON RESUME category");

//        ((ItemsAdapter) mAdapter).clear();
//        itemList = LoadData();
//        ((ItemsAdapter) mAdapter).SetList(itemList);
////        mAdapter.notifyDataSetChanged();
//        mAdapter.notifyItemChanged(0);
        itemList = LoadData();
        /*if(itemInserted){
            System.out.println("SENJUTO - ITEM INSERTED");
            //((ItemsAdapter) mAdapter).clear();
            itemList = LoadData();
            ((ItemsAdapter) mAdapter).SetList(itemList);
            //TODO PROBLEMA CON LOS COLORES AL NOTIFICAR
            //mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemInserted(itemList.size());
            itemInserted = false;
        }else if(isItemDeleted){
            System.out.println("SENJUTO - ITEMDELETED : " + isItemDeleted + "POS: " + itemPosition);
            itemList = LoadData();
            ((ItemsAdapter) mAdapter).SetList(itemList);
            mAdapter.notifyItemRemoved(itemPosition);
            isItemDeleted = false;
            isItemSold = false;
        }else if(isItemSold){
            System.out.println("SENJUTO - ITEMSOLD : " + isItemSold + "POS: " + itemPosition);
//            ((ItemsAdapter) mAdapter).clear();
            itemList = LoadData();
            ((ItemsAdapter) mAdapter).SetList(itemList);
//            mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(itemPosition);
            isItemSold = false;
        }else{
            System.out.println("SENJUTO - NOTHING");
        }*/


        //TODO Cambiar donde se coloca la posicion tras introducir nueva categoria
        //mLayaoutManager.scrollToPosition(0);

        //LoadData();
        //SwitchView();
        //LoadRecycler();
        super.onResume();
    }

    private void SwitchView() {
        if(VIEW_MODE_LISTVIEW == currentViewMode){
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.INVISIBLE);
        }else{
            stubList.setVisibility(View.GONE);
            stubGrid.setVisibility(View.VISIBLE);
        }
        SetAdapters();
    }

    private void SetAdapters() {

        if(VIEW_MODE_LISTVIEW == currentViewMode){
            listViewAdapter = new ListViewItemAdapter(getContext(), R.layout.list_item, itemList);
            listView.setAdapter(listViewAdapter);
        }else{
            gridViewAdapter = new GridViewItemAdapter(getContext(), R.layout.grid_item, itemList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.item_menu_1:
//                if(VIEW_MODE_LISTVIEW == currentViewMode){
//                    currentViewMode = VIEW_MODE_GRIDVIEW;
//                }else{
//                    currentViewMode = VIEW_MODE_LISTVIEW;
//                }
//                SwitchView();
//                //save mode in sharedpreferences
//                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("currentViewMode", currentViewMode);
//                editor.commit();
//
//                break;
//
//            case R.id.item_menu_2:
//                DeleteCategory();
//                break;
//        }
//        return true;
//    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            //Do anything when user click to item
            //Toast.makeText(getApplicationContext(), categoryList.get(position).getName(), Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent(getContext(), ItemContent.class);
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        }
    };

    // ---------------------------------------------------------- Intents Methods

    public void OpenItem(int position){
        Intent intent = new Intent(getContext(), ItemContent.class);
        intent.putExtra("item", itemList.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, ITEM_OPEN);
    }

    public void NewItem(View view){
        Intent intent = new Intent(getContext(), NewItem.class);
        intent.putExtra("categoryId", String.valueOf(categoria.getId()));
//        startActivity(intent);
        startActivityForResult(intent, ITEM_NEW);
    }

    // ---------------------------------------------------------- DB Methods

    public List<Item> LoadData(){
        System.out.println("SENJUTO - Loading Category data");
        itemList = new ArrayList<>();
        itemList = dbUtils.GetItemsFromCategory(String.valueOf(categoria.getId()));
        return itemList;
    }

    private void DeleteCategory() {
        dbUtils.DeleteFromTable(Categoria.TABLE_NAME,
                String.valueOf(categoria.getId()),
                categoria.getName());
        getActivity().onBackPressed();
    }

    // ---------------------------------------------------------- GenerateSheet

    public void GenerateSheet() {
        // 72 PPI = 595 x 842 | 300 PP1 = 2480 x 3508
        int widthA4 = 595 ;
        int heightA4 = 842;
        int y = 70;
        //Create a new image bitmap and attach a brand new canvas to it
        Bitmap tempBitmap = Bitmap.createBitmap(widthA4 , heightA4, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        tempBitmap.setHasAlpha(true);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(0,0,widthA4,heightA4), 2, 2, paint);

        for(Item item: itemList){
            paint.setColor(Color.BLACK);
            paint.setTextSize(56);
            canvas.drawText(item.getName(), 20, y, paint);
            Bitmap tmpBitmap = GenerateQR(item.getId());
            canvas.drawBitmap(tmpBitmap, 300,y-70, paint);

            y = y + tmpBitmap.getHeight();
        }
        File reportFile = null;
        FileOutputStream os = null;
        try {
            reportFile = createImageFile();
            os = new FileOutputStream(reportFile);

            tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap GenerateQR(int id){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        String text = "ITEM:".concat(String.valueOf(id));

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "INFORME_QR";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Item> filteredModelList = filter(itemList, query);
        ((ItemsAdapter) mAdapter).replaceAll(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private static List<Item> filter(List<Item> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Item> filteredModelList = new ArrayList<>();
        for (Item model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }
}

package com.main.expo.exporganizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.main.expo.adapter.GridViewAdapter;
import com.main.expo.adapter.GridViewItemAdapter;
import com.main.expo.adapter.ListViewAdapter;
import com.main.expo.adapter.ListViewItemAdapter;
import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import cmon.main.expo.db.DBHelper;

public class CategoryContent extends AppCompatActivity {

    private DBHelper catdbh;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_content);

        catdbh =
                new DBHelper(this, "DBGlobal", null, 1);

        categoria = (Categoria) getIntent().getParcelableExtra("categoria");

        stubList = findViewById(R.id.stub_list2);
        stubGrid = findViewById(R.id.stub_grid2);

        //Inflate ViewStub before get view

        stubList.inflate();
        stubGrid.inflate();

        listView = findViewById(R.id.mylistview);
        gridView = findViewById(R.id.mygridview);

        //Get list of products
        LoadData();

        //Get current view mode in share references
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW); //Default

        //Register item click
        listView.setOnItemClickListener(onItemClickListener);
        gridView.setOnItemClickListener(onItemClickListener);

        SwitchView();
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
    protected void onResume() {
        super.onResume();

        System.out.println("ON RESUUUME 2");

        LoadData();
        SwitchView();
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
            listViewAdapter = new ListViewItemAdapter(this, R.layout.list_item, itemList);
            listView.setAdapter(listViewAdapter);
        }else{
            gridViewAdapter = new GridViewItemAdapter(this, R.layout.grid_item, itemList);
            gridView.setAdapter(gridViewAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_menu_1:
                if(VIEW_MODE_LISTVIEW == currentViewMode){
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                }else{
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                SwitchView();
                //save mode in sharedpreferences
                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", currentViewMode);
                editor.commit();

                break;

            case R.id.item_menu_2:
                DeleteCategory();
                break;
        }
        return true;
    }

    private void DeleteCategory() {
        SQLiteDatabase db = catdbh.getWritableDatabase();

        if(db != null) {
            String selection = Categoria.COLUMN_NAME_TITLE + " LIKE ?";
            String[] selectionArgs = { categoria.getName() };
            db.delete(Categoria.TABLE_NAME, selection, selectionArgs);

            String selection2 = Item.COLUMN_NAME_CATEGORY + " LIKE ?";
            String[] selectionArgs2 = { categoria.getName() };
            db.delete(Item.TABLE_NAME, selection2, selectionArgs2);
        }

        onBackPressed();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            //Do anything when user click to item
            //Toast.makeText(getApplicationContext(), categoryList.get(position).getName(), Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent(CategoryContent.this, ItemContent.class);
            intent.putExtra("item", itemList.get(position));
            startActivity(intent);
        }
    };

    public void NewItem(View view){
        Intent intent = new Intent(CategoryContent.this, NewItem.class);
        intent.putExtra("categoryId", String.valueOf(categoria.getId()));
        startActivity(intent);
    }

    public List<Item> LoadData(){

        itemList = new ArrayList<>();

        SQLiteDatabase db = catdbh.getReadableDatabase();

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
            String[] selectionArgs = { String.valueOf(categoria.getId()) };

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
                while(cursor.moveToNext()) {

                    itemList.add(
                            new Item(cursor.getInt(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_IMAGE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_TITLE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_DESCRIPTION)),
                                    cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_QUANTITY)),
                                    cursor.getInt(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SOLD)),
                                    cursor.getFloat(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_PRICE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_SERIES)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Item.COLUMN_NAME_CATEGORY))));
                }
            }
        }

        return itemList;
    }


}

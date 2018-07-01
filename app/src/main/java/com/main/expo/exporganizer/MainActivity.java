package com.main.expo.exporganizer;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.main.expo.adapter.GridViewAdapter;
import com.main.expo.adapter.ListViewAdapter;
import com.main.expo.beans.Categoria;

import java.util.List;

import cmon.main.expo.db.DBHelper;

public class MainActivity extends AppCompatActivity {

    private DBHelper catdbh;

    private ViewStub stubGrid;
    private ViewStub stubList;
    private ListView listView;
    private GridView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private List<Categoria> categoryList;
    private int currentViewMode = 0;

    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;

    private float revealX;
    private float revealY;

    View.OnTouchListener listener;

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        // Do your calcluations
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainLayout = findViewById(R.id.main_layout_element);

        RelativeLayout noticias_button = findViewById(R.id.noticias_button);
        RelativeLayout eventos_button = findViewById(R.id.eventos_button);

        listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                revealX = motionEvent.getRawX();
                revealY = motionEvent.getRawY();

                return false;
            }
        };
        //mainLayout.setOnTouchListener(listener);
        noticias_button.setOnTouchListener(listener);
        eventos_button.setOnTouchListener(listener);

        /*catdbh = new DBHelper(this, "DBGlobal", null, 1);

        stubList = findViewById(R.id.stub_list);
        stubGrid = findViewById(R.id.stub_grid);

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

        SwitchView();*/
    }

    public void presentActivity(View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");

        Intent intent = new Intent(MainActivity.this, TestActivuty.class);

//        int revealX = (int) (view.getX() + view.getWidth() / 2);
//        int revealY = (int) (view.getY() + view.getHeight() / 2);

//        int[] screenPos = new int[2];
//        view.getLocationOnScreen(screenPos);

        intent.putExtra(TestActivuty.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(TestActivuty.EXTRA_CIRCULAR_REVEAL_Y, revealY);

                ActivityCompat.startActivity(this, intent, options.toBundle());

    }

    public void presentActivity2(View view) {
        Intent intent = new Intent(MainActivity.this, EventsActivity.class);
        startActivity(intent);
    }

    public void ReadQR(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("ON RESUUUME");

//        LoadData();
//        SwitchView();
    }

//    private void SwitchView() {
//        if(VIEW_MODE_LISTVIEW == currentViewMode){
//            stubList.setVisibility(View.VISIBLE);
//            stubGrid.setVisibility(View.INVISIBLE);
//        }else{
//            stubList.setVisibility(View.GONE);
//            stubGrid.setVisibility(View.VISIBLE);
//        }
//        SetAdapters();
//    }
//
//    private void SetAdapters() {
//        if(VIEW_MODE_LISTVIEW == currentViewMode){
//            listViewAdapter = new ListViewAdapter(this, R.layout.list_item, categoryList);
//            listView.setAdapter(listViewAdapter);
//        }else{
//            gridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, categoryList);
//            gridView.setAdapter(gridViewAdapter);
//        }
//    }
//
//    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//            //Do anything when user click to item
//            //Toast.makeText(getApplicationContext(), categoryList.get(position).getName(), Toast.LENGTH_SHORT ).show();
//
//            Intent intent = new Intent(MainActivity.this, CategoryContent.class);
//            intent.putExtra("categoria", categoryList.get(position));
//            startActivity(intent);
//        }
//    };
//
    public void NewCategory(View view){
        /*Intent intent = new Intent(MainActivity.this, NewCategory.class);
        startActivity(intent);*/

        Intent intent = new Intent(MainActivity.this, TestActivuty.class);
        startActivity(intent);
    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
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
//        }
//        return true;
//    }
//
//    public void StartDB(View view){
//        //Abrimos la base de datos en modo escritura
//
//        SQLiteDatabase db = catdbh.getWritableDatabase();
//
//        //Si hemos abierto correctamente la base de datos
//        if(db != null)
//        {
//            //Insertamos categoría de ejemplo
//            // Create a new map of values, where column names are the keys
//
//            ContentValues values = new ContentValues();
//            values.put(Categoria.COLUMN_NAME_TITLE, "Figuras");
//            values.put(Categoria.COLUMN_NAME_DESCRIPTION, "Descripción para posters");
//            values.put(Categoria.COLUMN_NAME_IMAGE, R.drawable.bell);
//
//            long newRowId = db.insert(Categoria.TABLE_NAME, null, values);
//
//            //Cerramos la base de datos
//            db.close();
//        }
//    }
//
//    public void GetData(View view) {
//        System.out.println("EMPIEZA GET");
//        //Abrimos la base de datos en modo lectura
//        DBHelper catdbh =
//                new DBHelper(this, "DBGlobal", null, 1);
//
//        SQLiteDatabase db = catdbh.getReadableDatabase();
//
//        //Si hemos abierto correctamente la base de datos
//        if (db != null) {
//
//            System.out.println("DB NOT NULL");
//
//            // Define a projection that specifies which columns from the database
//            // you will actually use after this query.
//            String[] projection = {
//                    Categoria._ID,
//                    Categoria.COLUMN_NAME_TITLE,
//                    Categoria.COLUMN_NAME_DESCRIPTION
//            };
//
//        /* Filter results WHERE "title" = 'My Title'
//        String selection = DBHelper.Categoria.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };*/
//
//        /* How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                DBHelper.Categoria.COLUMN_NAME_SUBTITLE + " DESC";*/
//
//            Cursor cursor = db.query(
//                    Categoria.TABLE_NAME,                     // The table to query
//                    projection,                               // The columns to return
//                    null,                                // The columns for the WHERE clause
//                    null,                            // The values for the WHERE clause
//                    null,                                     // don't group the rows
//                    null,                                     // don't filter by row groups
//                    null                                 // The sort order
//            );
//
//            if (cursor != null) {
//
//                System.out.println("CURSOR NOT NULL");
//
//                cursor.moveToFirst();
//                String itemName = cursor.getString(
//                        cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_TITLE)
//                );
//
//                TextView texto = (TextView) findViewById(R.id.textoPrueba);
//                texto.setText(itemName);
//
//                System.out.println("itemName: " + itemName);
//            }
//        }
//    }
//
//    public List<Categoria> LoadData(){
//
//        categoryList = new ArrayList<>();
//
//        GridView gridView = (GridView)  findViewById(R.id.gridView);
//
//        DBHelper catdbh =
//                new DBHelper(this, "DBGlobal", null, 1);
//
//        SQLiteDatabase db = catdbh.getReadableDatabase();
//
//        if (db != null) {
//            String[] projection = {
//                    Categoria._ID,
//                    Categoria.COLUMN_NAME_TITLE,
//                    Categoria.COLUMN_NAME_DESCRIPTION,
//                    Categoria.COLUMN_NAME_IMAGE
//            };
//
//            Cursor cursor = db.query(
//                    Categoria.TABLE_NAME,                     // The table to query
//                    projection,                               // The columns to return
//                    null,                                // The columns for the WHERE clause
//                    null,                            // The values for the WHERE clause
//                    null,                                     // don't group the rows
//                    null,                                     // don't filter by row groups
//                    null                                 // The sort order
//            );
//
//            if (cursor != null) {
//                while(cursor.moveToNext()) {
//
//                    categoryList.add(
//                            new Categoria(cursor.getInt(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_IMAGE)),
//                                    cursor.getString(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_TITLE)),
//                                    cursor.getString(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_DESCRIPTION))));
//                }
//            }
//
//
//            gridView.setAdapter(adaptador);*/
//        }
//
//        return categoryList;
//    }
}

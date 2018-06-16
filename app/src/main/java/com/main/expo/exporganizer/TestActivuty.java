package com.main.expo.exporganizer;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.main.expo.adapter.TestAdapter;
import com.main.expo.beans.Categoria;

import java.util.ArrayList;
import java.util.List;

import cmon.main.expo.db.DBHelper;

public class TestActivuty extends AppCompatActivity {

    private List<Categoria> categorias;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayaoutManager;

    private RecyclerView.LayoutManager layoutManager;

    View rootLayout;

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private float revealX;
    private float revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activuty);

        // START REVEAL ANIMATION
        InitAnim();

        categorias = this.LoadData();

        mRecyclerView = findViewById(R.id.testrecyclerview);
        mLayaoutManager = new LinearLayoutManager(this);

        mAdapter = new TestAdapter(categorias, R.layout.test_recycler, new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Categoria categoria, int position, ImageView imageViewName) {
                //TODO IMPORTANTE - Continuar la transición entre category content
                OpenCategory(position);
            }
        });

        layoutManager = new GridLayoutManager(this, 2);

        //TODO Para poner el primer elemento ocupando 2 huecos
        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isHeader(position)) {
                    //Returns span count 2 if method isHeader() returns true.
                    //You can use your own logic here.
                    return  2;
                } else {
                    return 1;
                }
            }
        });*/

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void InitAnim() {
        rootLayout = findViewById(R.id.root_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            rootLayout.setVisibility(View.INVISIBLE);

            final Intent intent = getIntent();

            revealX = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            RadialAnim(rootLayout);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void RadialAnim(View view) {
        int startRadius = 0;
        int endRadius = Math.max(view.getWidth(), view.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(view, (int) revealX, (int) revealY, startRadius, endRadius);

        anim.setDuration(800);

        anim.start();
        view.setVisibility(View.VISIBLE);
    }

//    public static boolean isHeader(int position) {
//        return position == 0;//you can use some other logic in here
//    }

    public List<Categoria> LoadData() {

        ArrayList<Categoria> categoryList = new ArrayList<Categoria>();

        DBHelper catdbh =
                new DBHelper(this, "DBGlobal", null, 1);

        SQLiteDatabase db = catdbh.getReadableDatabase();

        if (db != null) {
            String[] projection = {
                    Categoria._ID,
                    Categoria.COLUMN_NAME_TITLE,
                    Categoria.COLUMN_NAME_DESCRIPTION,
                    Categoria.COLUMN_NAME_IMAGE
            };

            Cursor cursor = db.query(
                    Categoria.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    System.out.println(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_IMAGE));

                    categoryList.add(
                            new Categoria(cursor.getInt(cursor.getColumnIndexOrThrow(Categoria._ID)),
                                    //cursor.getBlob(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_IMAGE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_IMAGE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_TITLE)),
                                    cursor.getString(cursor.getColumnIndexOrThrow(Categoria.COLUMN_NAME_DESCRIPTION))));
                }
            }
        }
        if (!categoryList.isEmpty() || categoryList != null) {
            return categoryList;
        }else{
            return null;
        }
    }

    public void OpenCategory(int position){
        Intent intent = new Intent(this, CategoryContent.class);
        intent.putExtra("categoria", categorias.get(position));
        startActivity(intent);

        //TODO Error memoria entre actividades
    }

    //PRESENT ACTIVITY
    public void NewCategory(View view){

        Pair<View, String> p1 = Pair.create((View)view, "transition");

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, p1);

        Intent intent = new Intent(this, NewCategory.class);

//        int revealX = (int) (view.getX() + view.getWidth() / 2);
//        int revealY = (int) (view.getY() + view.getHeight() / 2);

//        int[] screenPos = new int[2];
//        view.getLocationOnScreen(screenPos);

        intent.putExtra(NewCategory.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(NewCategory.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    protected void onResume() {

        categorias = LoadData();
        ((TestAdapter) mAdapter).SetList(categorias);
        mAdapter.notifyDataSetChanged();
        //TODO Cambiar donde se coloca la posicion tras introducir nueva categoria
        layoutManager.scrollToPosition(categorias.size()-1);
        super.onResume();
    }
}

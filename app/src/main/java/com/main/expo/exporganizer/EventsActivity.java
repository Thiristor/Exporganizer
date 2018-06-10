package com.main.expo.exporganizer;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.expo.adapter.EventsAdapter;
import com.main.expo.adapter.TestAdapter;
import com.main.expo.beans.Categoria;
import com.main.expo.beans.Evento;
import com.main.expo.exporganizer.fragments.EventFragment;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends FragmentActivity {

    private List<Evento> eventos;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayaoutManager;

    private TextView textoFragment;

    public static final String EXTRA_EVENT_ITEM = "EXTRA_EVENT_ITEM";
    public static final String EXTRA_EVENT_IMAGE = "EXTRA_EVENT_IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        textoFragment = findViewById(R.id.nombreEvento);

        eventos = this.LoadData();

        mRecyclerView = findViewById(R.id.eventsrecyclerview);
        mLayaoutManager = new LinearLayoutManager(this);

        mAdapter = new EventsAdapter(eventos, R.layout.event_item, new EventsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento evento, int position, ImageView imageViewName) {
                //Toast.makeText(TestActivuty.this, name + " - " + position, Toast.LENGTH_LONG).show();
                //DeleteName(position);
                //TODO
                //OpenEvent(position);

//                ImageView eventImage;
//                eventImage = findViewById(R.id.imageViewPoster);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    OpenEvent(imageViewName, position);
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayaoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Evento> LoadData() {
        return new ArrayList<Evento>(){{
            add(new Evento(1,R.drawable.japanweeknd, "Evento 1", "El evento 1", 1));
            add(new Evento(2,R.drawable.madridotaku, "Evento 2", "El evento 2", 2));
            add(new Evento(1,R.drawable.japanweeknd, "Evento 1", "El evento 1", 1));
            add(new Evento(2,R.drawable.madridotaku, "Evento 2", "El evento 2", 2));
        }};
    }

    public void OpenEvent(ImageView eventImage, int position){

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            fragmento.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
////            fragmento.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
//        }

        Intent intent = new Intent(this, EventFragment.class);


        intent.putExtra(EXTRA_EVENT_ITEM, eventos.get(position));
        intent.putExtra(EXTRA_EVENT_IMAGE, ViewCompat.getTransitionName(eventImage));
//        ViewCompat.getTransitionName(eventImage);
        System.out.println("START: " + ViewCompat.getTransitionName(eventImage));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        eventImage,
                        ViewCompat.getTransitionName(eventImage));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options.toBundle());
        }
    }

    public void NewEvent(View view){

    }
}

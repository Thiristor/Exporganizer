package com.main.expo.exporganizer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.expo.beans.Evento;
import com.main.expo.exporganizer.CategoryContent;
import com.main.expo.exporganizer.EventsActivity;
import com.main.expo.exporganizer.R;

//extends android.support.v4.app.Fragment

public class EventFragment extends AppCompatActivity {

    public static final String EXTRA_EVENT_ITEM = "EXTRA_EVENT_ITEM";
    public static final String EXTRA_EVENT_IMAGE = "EXTRA_EVENT_IMAGE";

    final static int resultID = 0;
    Bitmap bmp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_fragment);


        //supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        Evento eventItem = extras.getParcelable(EventsActivity.EXTRA_EVENT_ITEM);

        imageView = (ImageView) findViewById(R.id.fragmentImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.setBackground(getResources().getDrawable(eventItem.getImageId()));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(EventsActivity.EXTRA_EVENT_IMAGE);

            System.out.println("END: " + imageTransitionName);

            imageView.setTransitionName(imageTransitionName);
        }
    }

    public void OpenCamera (View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, resultID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Bundle ext = data.getExtras();
            bmp = (Bitmap) ext.get("data");
            imageView.setImageBitmap(bmp);
        }
    }

//    public EventFragment() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        return inflater.inflate(R.layout.activity_event_fragment, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        final ImageView imageView = (ImageView) view.findViewById(R.id.fragmentImage);
//        final TextView textView = (TextView) view.findViewById(R.id.nombreFragment);
//
//        textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                // Tell the framework to start.
//                textView.getViewTreeObserver().removeOnPreDrawListener(this);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    getActivity().startPostponedEnterTransition();
//                }
//                return true;
//            }
//        });
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
////            setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_bottom));
//            //setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.slide_bottom));
//        }
//
//
//        }
}

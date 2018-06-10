package com.main.expo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.expo.beans.Categoria;
import com.main.expo.exporganizer.R;

import java.util.List;

/**
 * Created by Sergio on 06/02/2018.
 */

public class GridViewAdapter extends ArrayAdapter<Categoria> {
    public GridViewAdapter(Context context, int resource, List<Categoria> objects) {
        super(context, resource, objects);
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }
        Categoria categoria = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle =  (TextView) v.findViewById(R.id.txtTitle);

//        img.setImageResource(categoria.getImageId());
        txtTitle.setText(categoria.getName());

        return v;
    }

    @Override
    public int getViewTypeCount() {
        if(getCount() < 1){
            return 1;
        }else{
            return getCount();
        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}

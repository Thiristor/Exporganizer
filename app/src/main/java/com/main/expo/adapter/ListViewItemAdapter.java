package com.main.expo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Item;
import com.main.expo.exporganizer.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Sergio on 07/02/2018.
 */

public class ListViewItemAdapter extends ArrayAdapter<Item>{

    private RelativeLayout relativeLayout;
    private Item item;

    public ListViewItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null){
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }
        item = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtTitle =  (TextView) v.findViewById(R.id.txtTitle);
        TextView txtTotal =  (TextView) v.findViewById(R.id.txtTotal);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.listItem_relative);

        //img.setImageDrawable(item.getImage(img.getContext()));
        File f = new File(item.getImagePath());
        Picasso.get().load(f).into(img);

        txtTitle.setText(item.getName());
        if(item.getSold() > 0){
            txtTotal.setText(String.valueOf(item.getSold()*item.getPrice()) + "€");
        }else{
            //txtTotal.setVisibility(View.INVISIBLE);
        }

        ChangeColor();

        return v;
    }


    private void ChangeColor(){
        int rest = item.getQuantity() - item.getSold();
        if(item.getSold() > 0) {
            if (rest > item.getQuantity() / 2) {
                relativeLayout.setBackgroundColor(Color.parseColor("#77DD77")); //Naranja FFB347
            } else if (rest > (item.getQuantity() / 2) / 2) {
                relativeLayout.setBackgroundColor(Color.parseColor("#FFB347")); //Rojo FF6961
            }else{
                relativeLayout.setBackgroundColor(Color.parseColor("#FF6961")); //Verde 77DD77
            }
        }
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

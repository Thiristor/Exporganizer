package com.main.expo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.expo.beans.Item;
import com.main.expo.exporganizer.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    private List<Item> item;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageViewName;
        public TextView txtTotal;
        public ImageView imageColorBar;

        public ViewHolder (View itemView){
            super(itemView);
            this.textViewName = (TextView)itemView.findViewById(R.id.txtTitle);
            this.imageViewName = (ImageView)itemView.findViewById(R.id.imageView);
            this.txtTotal =  (TextView)itemView.findViewById(R.id.txtTotal);
            this.imageColorBar = (ImageView) itemView.findViewById(R.id.colorbar);
        }

        public void Bind(final Item item, final ItemsAdapter.OnItemClickListener listener){
            // Procesamos los datos a renderizar

            textViewName.setText(item.getName().concat(" : " + String.valueOf(item.getId())));
            String total = String.valueOf(item.getSold()*item.getPrice()) + "€";
            txtTotal.setText(total);

            //img.setImageDrawable(item.getImage(img.getContext()));
            File f = new File(item.getImagePath());
            Picasso.get().load(f).fit().into(imageViewName);

            ChangeColor(item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, getAdapterPosition(), imageViewName);
                }
            });
        }

        private void ChangeColor(Item item){
            int rest = item.getQuantity() - item.getSold();
            if(item.getSold() > 0) {
                if (rest > item.getQuantity() / 2) {
                    imageColorBar.setBackgroundColor(Color.parseColor("#77DD77")); //Naranja FFB347
                } else if (rest > (item.getQuantity() / 2) / 2) {
                    imageColorBar.setBackgroundColor(Color.parseColor("#FFB347")); //Rojo FF6961
                }else if(rest == 0){
                    imageColorBar.setBackgroundColor(Color.parseColor("#0A0A0A")); //NEGRO 0A0A0A
                }else{
                    imageColorBar.setBackgroundColor(Color.parseColor("#FF6961")); //Verde 77DD77
                }
            }else{
                //TODO FALLA AL CAMBIAR EL ANCHO
                //imageColorBar.getLayoutParams().width = 0;
                imageColorBar.setBackgroundColor(Color.parseColor("#3B83BD")); //Verde 77DD77
            }
        }
    }

    public ItemsAdapter(List<Item> item, int layout, ItemsAdapter.OnItemClickListener listener){
        this.item = item;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    public void SetList (List<Item> item){
        this.item = item;
    }


    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ItemsAdapter.ViewHolder vh = new ItemsAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ItemsAdapter.ViewHolder holder, int position) {
        holder.Bind(item.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Item item, int position, ImageView imageViewName);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        final int size = item.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                item.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }
}

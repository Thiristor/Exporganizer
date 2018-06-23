package com.main.expo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.expo.beans.Categoria;
import com.main.expo.exporganizer.R;
import com.main.expo.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{

    private List<Categoria> categorias;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageViewName;

        public ViewHolder (View itemView){
            super(itemView);
            this.textViewName = (TextView)itemView.findViewById(R.id.textViewTitle);
            this.imageViewName = (ImageView)itemView.findViewById(R.id.imageViewPoster);
        }

        public void Bind(final Categoria categoria, final OnItemClickListener listener){
            // Procesamos los datos a renderizar

            ViewCompat.setTransitionName(imageViewName, String.valueOf(categoria.getId()));

            textViewName.setText(categoria.getName());

            //Bitmap imageUri = ImageUtils.getImageUriFromByte(null, categoria.getImageId());

//            Picasso.get().load(imageUri).fit().into(imageViewName);


            //imageViewName.setImageBitmap(imageUri);
            //imageViewName.setImageURI(Uri.fromFile(new File(categoria.getImagePath())));
            //imageViewName.setImageDrawable(categoria.getImage(imageViewName.getContext()));
            File f = new File(categoria.getImagePath());
            Picasso.get().load(f).fit().into(imageViewName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(categoria, getAdapterPosition(), imageViewName);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    System.out.println("LONG CLICK: " + getAdapterPosition());

                    return true;
                }
            });
        }
    }

    public TestAdapter(List<Categoria> categorias, int layout, OnItemClickListener listener){
        this.categorias = categorias;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    public void SetList (List<Categoria> categorias){
        this.categorias = categorias;
    }

    public static boolean isHeader(int position) {
        return position == 0;//you can use some other logic in here
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Bind(categorias.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Categoria categoria, int position, ImageView imageViewName);
    }
}

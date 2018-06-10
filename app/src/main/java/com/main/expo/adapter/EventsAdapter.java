package com.main.expo.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Evento;
import com.main.expo.exporganizer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{
    private List<Evento> eventos;
    private int layout;
    private EventsAdapter.OnItemClickListener itemClickListener;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public ImageView imageViewName;

        public ViewHolder (View itemView){
            super(itemView);
            this.textViewName = (TextView)itemView.findViewById(R.id.textViewTitle);
            this.imageViewName = (ImageView)itemView.findViewById(R.id.imageViewPoster);
        }

        public void Bind(final Evento evento, final EventsAdapter.OnItemClickListener listener){
            // Procesamos los datos a renderizar

            ViewCompat.setTransitionName(imageViewName, evento.getName());

            textViewName.setText(evento.getName());
            Picasso.get().load(evento.getImageId()).into(imageViewName);
            //imageViewName.setImageResource(evento.getImageId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(evento, getAdapterPosition(), imageViewName);
                }
            });
        }
    }

    public EventsAdapter(List<Evento> eventos, int layout, EventsAdapter.OnItemClickListener listener){
        this.eventos = eventos;
        this.layout = layout;
        this.itemClickListener = listener;
    }

    public void SetList (List<Evento> eventos){
        this.eventos = eventos;
    }

    public static boolean isHeader(int position) {
        return position == 0;//you can use some other logic in here
    }
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        EventsAdapter.ViewHolder vh = new EventsAdapter.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, int position) {
        holder.Bind(eventos.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Evento evento, int position, ImageView imageViewName);
    }
}


package com.main.expo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.SortedList;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    //private List<Item> item;
    private int layout;
    private OnItemClickListener itemClickListener;

    private Context context;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        /*if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(item, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(item, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);*/

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
//        item.remove(position);
        mSortedList.remove(mSortedList.get(position));
        notifyItemRemoved(position);
    }

    // TODO PRUEBA SORTEDLIST

    private final SortedList<Item> mSortedList = new SortedList<>(Item.class, new SortedList.Callback<Item>() {
        @Override
        public int compare(Item a, Item b) {
            System.out.println("SENJUTO - COMPARE");
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            System.out.println("SENJUTO - onINSERTED");
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            System.out.println("SENJUTO - onREMOVED");
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            System.out.println("SENJUTO - onMOVED");
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            System.out.println("SENJUTO - onCHANGED");
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Item oldItem, Item newItem) {
            System.out.println("SENJUTO - areContentsTheSame");
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Item item1, Item item2) {
            System.out.println("SENJUTO - areItemsTheSame " + (item1.getId() == item2.getId()) );
            return item1.getName() == item2.getName();
        }
    });

    private static final Comparator<Item> mComparator = new Comparator<Item>() {
        @Override
        public int compare(Item a, Item b) {
//            int compared = String.valueOf(a.getId()).compareTo(String.valueOf(b.getId()));
            int compared = a.getName().compareTo(b.getName());
            System.out.println("SENJUTO - COMPARE: " + a.getName() + " | " + b.getName()
                    + " | " + compared);
            return a.getName().compareTo(b.getName());
        }
    };

    public void replaceAll(List<Item> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final Item model = mSortedList.get(i);
            if (!models.contains(model)) {
                System.out.println("SENJUTO - SE BORRA: " + model.getName());
                mSortedList.remove(model);

            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }








    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private ImageView imageViewName;
        private TextView txtTotal;
        private ImageView imageColorBar;
        private ImageView imageGradient;

        public ViewHolder (View itemView){
            super(itemView);
            this.textViewName = (TextView)itemView.findViewById(R.id.txtTitle);
            this.imageViewName = (ImageView)itemView.findViewById(R.id.imageView);
            this.txtTotal =  (TextView)itemView.findViewById(R.id.txtTotal);
            this.imageColorBar = (ImageView) itemView.findViewById(R.id.colorbar);
            this.imageGradient = (ImageView) itemView.findViewById(R.id.item_gradient);
        }

        public void Bind(final Item item, final ItemsAdapter.OnItemClickListener listener){
            // Procesamos los datos a renderizar

            textViewName.setText(item.getName().concat(" : " + String.valueOf(item.getId())));
            String total = String.valueOf(item.getSold()*item.getPrice()) + "€";
            txtTotal.setText(total);

//            img.setImageDrawable(item.getImage(img.getContext()));
            File f = new File(item.getImagePath());
            Picasso.get().load(f).fit().into(imageViewName);
            Picasso.get().load(R.drawable.itembanner).fit().into(imageGradient);

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
        //this.item = item;
        this.layout = layout;
        this.itemClickListener = listener;
        mSortedList.addAll(item);
    }

    public void SetList (List<Item> item){
//        this.item = item;
        mSortedList.addAll(item);
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
//        holder.Bind(item.get(position), itemClickListener);
        holder.Bind(mSortedList.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
//        return item.size();
        return mSortedList.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Item item, int position, ImageView imageViewName);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /*public void clear() {
        final int size = item.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                item.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }*/
}

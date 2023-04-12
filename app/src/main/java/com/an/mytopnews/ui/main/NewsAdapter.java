package com.an.mytopnews.ui.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.compose.ui.platform.ComposeView;
import androidx.recyclerview.widget.RecyclerView;

import com.an.ListItemView;
import com.an.ListItemViewHolder;
import com.an.mytopnews.R;


public class NewsAdapter extends RecyclerView.Adapter<ListItemViewHolder>  {


    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ListItemView listItemView = new ListItemView(parent.getContext());
//        return new ListItemViewHolder(new ListItemView(parent.getContext()));
        throw new UnsupportedOperationException();
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
//        Fruit fruit = mFruitList.get(position);
//        holder.fruitImage.setImageResource(fruit.getImageId());
//        holder.fruitName.setText(fruit.getName());
    }
    @Override
    public int getItemCount(){
        return 10;
    }

}

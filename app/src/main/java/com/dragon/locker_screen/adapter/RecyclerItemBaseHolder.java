package com.dragon.locker_screen.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Maker on 2020/7/29.
 * Description:
 */
public class RecyclerItemBaseHolder extends RecyclerView.ViewHolder {

    RecyclerView.Adapter recyclerBaseAdapter;

    public RecyclerItemBaseHolder(View itemView) {
        super(itemView);
    }

    public RecyclerView.Adapter getRecyclerBaseAdapter() {
        return recyclerBaseAdapter;
    }

    public void setRecyclerBaseAdapter(RecyclerView.Adapter recyclerBaseAdapter) {
        this.recyclerBaseAdapter = recyclerBaseAdapter;
    }
}
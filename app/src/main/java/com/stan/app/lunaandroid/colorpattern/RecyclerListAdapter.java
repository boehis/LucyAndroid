/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stan.app.lunaandroid.colorpattern;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.stan.app.lunaandroid.R;
import com.stan.app.lunaandroid.colorpattern.helper.ItemTouchHelperAdapter;
import com.stan.app.lunaandroid.colorpattern.helper.ItemTouchHelperViewHolder;
import com.stan.app.lunaandroid.colorpattern.helper.OnStateChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 *
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final ArrayList<Integer> colorList;

    private final OnStateChangeListener onStateChangeListener;

    RecyclerListAdapter(OnStateChangeListener dragStartListener, ArrayList<Integer> colorList) {
        onStateChangeListener = dragStartListener;
        this.colorList = colorList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.setColor(colorList.get(position));
        holder.onItemClear();
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onStateChangeListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.copyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAdapterPosition();
                add(colorList.get(index), index+1);
            }
        });
    }

    public void add(Integer color) {
        add(color, getItemCount());
    }
    public void add(Integer color, int pos) {
        colorList.add(pos,color);
        onStateChangeListener.onListUpdated(colorList);
        notifyItemInserted(pos);
    }

    public ArrayList<Integer> getColorList() {
        return colorList;
    }

    @Override
    public void onItemDismiss(int position) {
        colorList.remove(position);
        onStateChangeListener.onListUpdated(colorList);
        notifyItemRemoved(position);
    }

    public void dismissAll() {
        colorList.clear();
        onStateChangeListener.onListUpdated(colorList);
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(colorList, fromPosition, toPosition);
        onStateChangeListener.onListUpdated(colorList);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        final ImageView handleView;
        final ImageButton copyColor;
        private float[] hsv = new float[3];


        ItemViewHolder(View itemView) {
            super(itemView);
            handleView = itemView.findViewById(R.id.handle);
            copyColor = itemView.findViewById(R.id.clone_color_button);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.HSVToColor(150,hsv));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.HSVToColor(hsv));
        }

        public void setColor(Integer color) {
            Color.colorToHSV(color, hsv);
        }
    }
}

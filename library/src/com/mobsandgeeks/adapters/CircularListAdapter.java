/*
 * Copyright (c) 2012 Mobs & Geeks
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

package com.mobsandgeeks.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * A circular adapter that makes your {@link ListView} appear circular.
 * 
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * @version 0.1
 */
public class CircularListAdapter extends BaseAdapter {
    // Debug
    static final boolean DEBUG = false;
    static final String TAG = CircularListAdapter.class.getSimpleName();
    
    // Attributes
    private BaseAdapter mListAdapter;
    private int mListAdapterCount;
    
    /**
     * Constructs a {@linkplain CircularListAdapter}.
     * 
     * @param listAdapter A {@link ListAdapter} that has to behave circular.
     */
    public CircularListAdapter(BaseAdapter listAdapter) {
        if(listAdapter == null) {
            throw new IllegalArgumentException("listAdapter cannot be null.");
        }
        
        this.mListAdapter = listAdapter;
        this.mListAdapterCount = listAdapter.getCount();
    }
    
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mListAdapter.getView(position % mListAdapterCount, convertView, parent);
    }

    @Override
    public Object getItem(int position) {
        return mListAdapter.getItem(position % mListAdapterCount);
    }

    @Override
    public long getItemId(int position) {
        return mListAdapter.getItemId(position % mListAdapterCount);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mListAdapter.areAllItemsEnabled();
    }

    @Override
    public int getItemViewType(int position) {
        return mListAdapter.getItemViewType(position % mListAdapterCount);
    }

    @Override
    public int getViewTypeCount() {
        return mListAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mListAdapter.isEmpty();
    }

    @Override
    public boolean isEnabled(int position) {
        return mListAdapter.isEnabled(position % mListAdapterCount);
    }

    @Override
    public void notifyDataSetChanged() {
        mListAdapter.notifyDataSetChanged();
        mListAdapterCount = mListAdapter.getCount();
        
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        mListAdapter.notifyDataSetInvalidated();
        super.notifyDataSetInvalidated();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return mListAdapter.getDropDownView(position % mListAdapterCount, 
                convertView, parent);
    }

    @Override
    public boolean hasStableIds() {
        return mListAdapter.hasStableIds();
    }
    
}

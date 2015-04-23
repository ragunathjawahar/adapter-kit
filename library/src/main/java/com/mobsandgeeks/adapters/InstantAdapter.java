/* 
 * Copyright (c) 2013 Mobs & Geeks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobsandgeeks.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Class that constructs a custom {@link Adapter} by mapping <b>Instant*</b> annotated
 * methods from you model to {@link View}s on your layout. Methods can be annotated using the
 * {@link InstantText} annotation.
 * 
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * 
 * @param <T> The model you want to back using the {@link InstantAdapter}.
 */
public class InstantAdapter<T> extends ArrayAdapter<T> {

    private Context mContext;
    private InstantAdapterCore<T> mInstantAdapterCore;

    /**
     * Constructs a new {@link InstantAdapter} for your model.
     * 
     * @param context The {@link Context} to use.
     * @param layoutResourceId The resource id of your XML layout.
     * @param dataType The data type backed by your adapter.
     * @param list The {@link List} of instances backed by your adapter. 
     */
    public InstantAdapter(final Context context, final int layoutResourceId,
            final Class<?> dataType, final List<T> list) {
        super(context, layoutResourceId, list);
        mContext = context;
        mInstantAdapterCore = new InstantAdapterCore<T>(context, this, layoutResourceId,
                dataType);
    }

    /**
     * Returns a {@link View} that has been inflated from the {@code layoutResourceId} argument in
     * the constructor. This method handles recycling as well. Subclasses are recommended to chain
     * upwards by calling {@code super.getView(position, convertView, parent)} and abstain from
     * recycling views unless and until you know what you are doing ;)
     * <p>
     * You are free to use the {@code View.setTag()} and {@code View.getTag()} methods in your
     * subclasses.
     * </p>
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mInstantAdapterCore.createNewView(mContext, parent);
        }

        T instance = getItem(position);
        mInstantAdapterCore.bindToView(parent, view, instance, position);

        return view;
    }

    /**
     * Sets a {@link ViewHandler} for a View with the given id.
     * 
     * @param viewId Id of the view you want to handle.
     * @param viewHandler A {@link ViewHandler} instance for your view with the given id.
     */
    public void setViewHandler(final int viewId, final ViewHandler<T> viewHandler) {
        mInstantAdapterCore.setViewHandler(viewId, viewHandler);
    }

}

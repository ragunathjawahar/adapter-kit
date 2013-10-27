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
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Class constructs a custom {@link CursorAdapter} by mapping <b>Instant*</b> annotated
 * methods from you model to {@link View}s on your layout. Methods can be annotated using the
 * {@link InstantText} annotation.
 * 
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * 
 * @param <T> The model you want to back using the {@link InstantAdapter}.
 */
public abstract class InstantCursorAdapter<T> extends CursorAdapter {

    private InstantAdapterCore<T> mInstantAdapterCore;

    /**
     * Constructs a new {@link InstantCursorAdapter} backed by your {@link Cursor}.
     * 
     * @param context The {@link Context} to use.
     * @param layoutResourceId The resource id of your XML layout.
     * @param dataType The data type backed by your adapter.
     * @param cursor The {@link Cursor} to be used.
     */
    public InstantCursorAdapter(final Context context, final int layoutResourceId,
            final Class<?> dataType, final Cursor cursor) {
        super(context, cursor, false);
        mInstantAdapterCore = new InstantAdapterCore<T>(context, this, layoutResourceId,
                dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void bindView(final View view, final Context context, final Cursor cursor) {
        T instance = getInstance(cursor);
        mInstantAdapterCore.bindToView(null, view, instance, cursor.getPosition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        return mInstantAdapterCore.createNewView(context, parent);
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

    /**
     * Method returns an instance of your model from the Cursor.
     * 
     * @param cursor The cursor backed by the {@link InstantCursorAdapter}.
     * 
     * @return An instance associated with the cursor's current position.
     */
    public abstract T getInstance(Cursor cursor);

}

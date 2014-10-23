package com.birin.gridlistviewadapters;

import android.content.ContentProvider;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

/**
 * Adapter that exposes data from a {@link android.database.Cursor Cursor} to a
 * {@link android.widget.ListView ListView} widget to place data in Grid like
 * list-view.
 * 
 * <p>
 * Note : This class is extension of
 * {@link android.support.v4.widget.CursorAdapter} such that it places data
 * within grid, This {@link CursorGridAdapter} works well with
 * {@link ContentProvider} & {@link CursorLoader} mechanism, this class doesn't
 * provide support for any traditional auto-re-query method
 * {@link android.support.v4.widget.CursorAdapter} since query was performed on
 * UI-thread & its already deprecated from latest CursorAdapter source.
 * </p>
 * 
 * @param <CVH>
 *            The CardViewHolder class with which this adapter is operating
 * 
 * @see BaseGridAdapter parent class For Full features of this class
 */
public abstract class CursorGridAdapter<CVH> extends
		BaseGridAdapter<Cursor, CVH> implements Filterable,
		CursorFilter.CursorFilterClient {

	/*
	 * Copyright (C) 2011 The Android Open Source Project
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
	 */

	protected Cursor mCursor;
	protected CursorFilter mCursorFilter;
	protected FilterQueryProvider mFilterQueryProvider;

	public CursorGridAdapter(Context context, int totalCardsInRow, Cursor c) {
		super(context, totalCardsInRow);
		init(c);
	}

	void init(Cursor c) {
		boolean cursorPresent = c != null;
		mCursor = c;
		if (cursorPresent == true) {
			updateGridWithNewSize(c.getCount());
		}
	}

	/**
	 * Returns the cursor.
	 * 
	 * @return the cursor.
	 */
	public Cursor getCursor() {
		return mCursor;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	/**
	 * Change the underlying cursor to a new cursor. If there is an existing
	 * cursor it will be closed.
	 * 
	 * @param cursor
	 *            The new cursor to be used
	 */
	public void changeCursor(Cursor cursor) {
		Cursor old = swapCursor(cursor);
		if (old != null) {
			old.close();
		}
	}

	/**
	 * Swap in a new Cursor, returning the old Cursor. Unlike
	 * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
	 * closed.
	 * 
	 * @param newCursor
	 *            The new cursor to be used.
	 * @return Returns the previously set Cursor, or null if there wasa not one.
	 *         If the given new Cursor is the same instance is the previously
	 *         set Cursor, null is also returned.
	 */
	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		}
		Cursor oldCursor = mCursor;
		mCursor = newCursor;
		invalidateStructure();
		if (newCursor != null) {
			updateGridWithNewSize(newCursor.getCount());
		} else {
			notifyDataSetChanged();
		}
		return oldCursor;
	}

	/**
	 * <p>
	 * Converts the cursor into a CharSequence. Subclasses should override this
	 * method to convert their results. The default implementation returns an
	 * empty String for null values or the default String representation of the
	 * value.
	 * </p>
	 * 
	 * @param cursor
	 *            the cursor to convert to a CharSequence
	 * @return a CharSequence representing the value
	 */
	public CharSequence convertToString(Cursor cursor) {
		return cursor == null ? "" : cursor.toString();
	}

	/**
	 * Runs a query with the specified constraint. This query is requested by
	 * the filter attached to this adapter.
	 * 
	 * The query is provided by a {@link android.widget.FilterQueryProvider}. If
	 * no provider is specified, the current cursor is not filtered and
	 * returned.
	 * 
	 * After this method returns the resulting cursor is passed to
	 * {@link #changeCursor(Cursor)} and the previous cursor is closed.
	 * 
	 * This method is always executed on a background thread, not on the
	 * application's main thread (or UI thread.)
	 * 
	 * Contract: when constraint is null or empty, the original results, prior
	 * to any filtering, must be returned.
	 * 
	 * @param constraint
	 *            the constraint with which the query must be filtered
	 * 
	 * @return a Cursor representing the results of the new query
	 * 
	 * @see #getFilter()
	 * @see #getFilterQueryProvider()
	 * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
	 */
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (mFilterQueryProvider != null) {
			return mFilterQueryProvider.runQuery(constraint);
		}

		return mCursor;
	}

	public Filter getFilter() {
		if (mCursorFilter == null) {
			mCursorFilter = new CursorFilter(this);
		}
		return mCursorFilter;
	}

	/**
	 * Returns the query filter provider used for filtering. When the provider
	 * is null, no filtering occurs.
	 * 
	 * @return the current filter query provider or null if it does not exist
	 * 
	 * @see #setFilterQueryProvider(android.widget.FilterQueryProvider)
	 * @see #runQueryOnBackgroundThread(CharSequence)
	 */
	public FilterQueryProvider getFilterQueryProvider() {
		return mFilterQueryProvider;
	}

	/**
	 * Sets the query filter provider used to filter the current Cursor. The
	 * provider's
	 * {@link android.widget.FilterQueryProvider#runQuery(CharSequence)} method
	 * is invoked when filtering is requested by a client of this adapter.
	 * 
	 * @param filterQueryProvider
	 *            the filter query provider or null to remove it
	 * 
	 * @see #getFilterQueryProvider()
	 * @see #runQueryOnBackgroundThread(CharSequence)
	 */
	public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
		mFilterQueryProvider = filterQueryProvider;
	}

	/**
	 * Copyright 2014-present Biraj Patel
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
	 */

	public void validatePositionOrThrow(int position) {
		if (position < 0 || position >= getAbsoluteCardsCount()) {
			throw new IndexOutOfBoundsException("Position requested "
					+ position + " Available Cursor size "
					+ getAbsoluteCardsCount());
		}
	}

	@Override
	public int getAbsoluteCardsCount() {
		if (mCursor != null) {
			return mCursor.getCount();
		} else {
			return 0;
		}
	}

	@Override
	public Cursor getCardData(int absoluteCardPosition) {
		validatePositionOrThrow(absoluteCardPosition);
		if (mCursor == null) {
			throw new IllegalStateException(
					"this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(absoluteCardPosition)) {
			throw new IllegalStateException("couldn't move cursor to position "
					+ absoluteCardPosition);
		}
		return mCursor;
	}

}

/**
 * Copyright 2014-present Biraj Patel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.birin.easylistviewadapters;

import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.birin.easylistviewadapters.utils.ChildViewsClickHandler;
import com.birin.easylistviewadapters.utils.OnLoadMoreRequestListener;
import com.birin.easylistviewadapters.utils.ViewHolderPositionTagger;

/**
 * The Class EasyBaseAdapter is a "EASY" wrapper around
 * android.widget.BaseAdapter so that user has to perform minimum operations of
 * 1) Recycling views & holders 2) browsing data through data-structure 3)
 * mapping various row-types to row-views 4) dispatching load-more events 5)
 * Handling mapping of row-type count 6) Handling children view with click
 * handling. This class has been written with 'default' access so that only
 * library knows about this class, To use this class's functionality use the
 * direct children which are available with public access modifier, eg.
 * EasyListAdapter, EasyCursorAdapter.
 * 
 * @param <E>
 *            The POJO class with which this adapter is operating.
 * @param <T>
 *            The RowViewSetter class with which this adapter is operating
 */
abstract class EasyBaseAdapter<E, T extends BaseRowViewSetter<E, Object>>
		extends BaseAdapter {

	private TreeMap<Integer, T> registeredRowViewSetters = new TreeMap<Integer, T>();

	private Context context;
	private LayoutInflater inflater;
	private OnLoadMoreRequestListener loadMoreRequestListener;
	private ChildViewsClickHandler childViewsClickHandler;
	private ViewHolderPositionTagger positionTagger;

	protected EasyBaseAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		BaseRowViewSetter<?, ?>[] inputRowViewSetters = getSupportedRowViewSetters();
		ensureInputRowViewSetterArray(inputRowViewSetters);
		registerRowViewSetters(inputRowViewSetters);
		ensureRegisteredRowTypes();
		positionTagger = getPositionTagger();
		childViewsClickHandler = new ChildViewsClickHandler(
				childrenOnClickListener);
	}

	public LayoutInflater getLayoutInflater() {
		return inflater;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Gets the position tagger instance, this class helps us tag our current
	 * row position to views present in user's ViewHolder class in each
	 * getView() call which will help us build memory efficient click mechanism
	 * by using only single instance of view click listener.
	 * 
	 * @return the position tagger instance.
	 * @see ViewHolderPositionTagger
	 */
	protected ViewHolderPositionTagger getPositionTagger() {
		return new ViewHolderPositionTagger();
	}

	/**
	 * Validate user provided RowViewSetter array length & not null value.
	 * 
	 * @param rowViewSetters
	 *            RowViewSetter classes returned by getSupportedRowViewSetters()
	 *            method.
	 */
	private void ensureInputRowViewSetterArray(
			BaseRowViewSetter<?, ?>[] rowViewSetters) {
		if (rowViewSetters == null || rowViewSetters.length == 0) {
			throw new IllegalArgumentException(
					"Atleast register 1 RowViewSetter  class");
		}
	}

	/**
	 * In this method we will maintain TreeMap which will map user provided
	 * row-type-ids to RowViewSetter classes mapping, this will enable us to
	 * retrieve particular RowViewSetter instance based on identifying
	 * row-type-id, which inturn will be used to draw rows in getView() method.
	 * 
	 * @param rowViewSetters
	 *            Which were passed by child class of this class.
	 */
	private void registerRowViewSetters(BaseRowViewSetter<?, ?>[] rowViewSetters) {
		for (BaseRowViewSetter<?, ?> rowViewSetter : rowViewSetters) {
			registerSingleRowViewSetter(rowViewSetter);
		}
	}

	/**
	 * Register single row view setter into map, if already registered throws an
	 * exception as it means client is trying to register multiple
	 * row-view-setter with same row-type value.
	 * 
	 * @param rowViewSetter
	 *            the RowViewSetter to be registered.
	 */
	@SuppressWarnings("unchecked")
	private void registerSingleRowViewSetter(
			BaseRowViewSetter<?, ?> rowViewSetter) {
		T userProvidedRowViewSetter = (T) rowViewSetter;
		int rowType = userProvidedRowViewSetter.getRowType();
		if (registeredRowViewSetters.containsKey(rowType) == false) {
			registeredRowViewSetters.put(rowType, userProvidedRowViewSetter);
		} else {
			throw new IllegalStateException(
					"Two different RowViewSetter classes "
							+ "can not have same Row-Type, make sure you pass unique row "
							+ "types in each getRowType() method.");
		}
	}

	/**
	 * Validate registered row view setters to ensure valid row types.
	 */
	private void ensureRegisteredRowTypes() {
		for (Integer rowType : registeredRowViewSetters.keySet()) {
			ensureRowType(rowType);
		}
	}

	/**
	 * Ensure row type validity, throws exception if not valid , where valid
	 * values are from 0 to getViewTypeCount() - 1
	 * 
	 * @param rowType
	 *            the row type
	 * @return true, if successful
	 */
	private void ensureRowType(Integer rowType) {
		boolean isValid = rowType >= 0 && rowType < getViewTypeCount();
		if (isValid == false) {
			throw new IllegalArgumentException("Illegal rowtype : " + rowType
					+ " Valid rowTypes are from 0 to "
					+ (getViewTypeCount() - 1) + " both inclusive");
		}
	}

	@Override
	public int getViewTypeCount() {
		return registeredRowViewSetters.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public E getItem(int position) {
		return getRowData(position);
	}

	@Override
	public int getItemViewType(int position) {
		return getRowViewType(getRowData(position));
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		T rowViewSetter = getRowViewSetter(position);
		E rowData = getRowData(position);
		Object rowViewHolder = null;
		if (null == rowView) {
			Row<Object> row = rowViewSetter.getNewRow(parent);
			rowView = row.getRowView();
			rowViewHolder = row.getRowViewHolder();
			rowViewSetter.registerChildrenViewClickEvents(rowViewHolder,
					childViewsClickHandler);
			rowView.setTag(rowViewHolder);
		} else {
			rowViewHolder = rowView.getTag();
		}
		// Set views
		positionTagger
				.scanAndTagViewsWithPositionValue(rowViewHolder, position);
		rowViewSetter.setRowView(rowData, rowViewHolder, position);
		checkAndDispatchLoadMoreEvent(position);
		return rowView;
	}

	/**
	 * Gets the RowViewSetter instance from map for data pointed by its
	 * position.
	 * 
	 * @param position
	 *            the position for which we need its RowViewSetter
	 * @return the RowViewSetter instance for row-type of this position.
	 */
	private T getRowViewSetter(int position) {
		int rowType = getItemViewType(position);
		ensureRowType(rowType);
		return registeredRowViewSetters.get(rowType);
	}

	/**
	 * Check and dispatch load more event, checks if its last position & also if
	 * listener is applied to adapter.
	 * 
	 * @param position
	 *            the position currently being drawn.
	 */
	private void checkAndDispatchLoadMoreEvent(int position) {
		if (position == getCount() - 1 && isLoadMoreRequestListenerSet()) {
			dispatchLoadMoreRequest();
		}
	}

	public boolean isLoadMoreRequestListenerSet() {
		return (this.loadMoreRequestListener != null);
	}

	private void dispatchLoadMoreRequest() {
		loadMoreRequestListener.onLoadMoreRequested();
	}

	/**
	 * Sets the on load more request listener.
	 * 
	 * @param loadMoreRequestListener
	 *            Client can register for load-more events by passing reference
	 *            of {@link OnLoadMoreRequestListener}
	 */
	public void setOnLoadMoreRequestListener(
			OnLoadMoreRequestListener loadMoreRequestListener) {
		this.loadMoreRequestListener = loadMoreRequestListener;
	}

	/**
	 * The children on click listener, this is a single object which will be
	 * registered for all children so in turn it becomes very memory efficient.
	 */
	private OnClickListener childrenOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			dispatchChildClickEvent(view);
		}
	};

	/**
	 * Dispatch child click event by finding its data from structure & passing
	 * to corresponding view-setter class.
	 * 
	 * @param view
	 *            the child view on which event occurred.
	 */
	private void dispatchChildClickEvent(View view) {
		int position = ChildViewsClickHandler.getPositionFromViewTag(view);
		int eventId = ChildViewsClickHandler.getEventIdFromViewTag(view);
		T rowViewSetter = getRowViewSetter(position);
		E rowData = getRowData(position);
		rowViewSetter.onChildViewClicked(view, rowData, eventId);
	}

	/**
	 * Validates if given row position is between data size range or throws
	 * exception.
	 * 
	 * @param position
	 *            the position to be verified.
	 */
	public abstract void validatePositionOrThrow(final int position);
	
	/**
	 * Gets the row data for a given position, this has been marked as default
	 * since only children should be able to traverse the data-structure &
	 * return POJO/Cursor.
	 * 
	 * @param position
	 *            the position
	 * @return the row data
	 */
	abstract E getRowData(int position);

	/**
	 * Gets array of BaseRowViewSetters this adapter will going to handle, this
	 * method is marked default since it will be implemented only by
	 * EasyListAdapter/EasyCursorAdapter in this package so that they can return
	 * ListRowViewSetter/CursorRowViewSetter respectively.
	 * 
	 * @return the supported row view setters
	 */
	abstract BaseRowViewSetter<?, ?>[] getSupportedRowViewSetters();

	/**
	 * Gets the row view type for given data, Library users should implement
	 * this method & return correct rowType whose RowViewSetter was registered
	 * with this adapter.
	 * 
	 * @param rowData
	 *            The row data for which adapter is looking for row-type.
	 * @return the row-view type of given data.
	 */
	public abstract int getRowViewType(E rowData);
}

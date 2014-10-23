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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * The Class EasyListAdapter is to be used when you want to show list-view using
 * java.util.List of data.
 * 
 * @param <E>
 *            The data type with which this adapter will operate.
 */
public abstract class EasyListAdapter<E> extends
		EasyBaseAdapter<E, BaseRowViewSetter<E, Object>> {

	public EasyListAdapter(Context context) {
		super(context);
	}

	private List<E> dataList = new ArrayList<E>();

	/**
	 * returns POJO present at given position.
	 * 
	 * @param position
	 *            the position
	 * @return the row data
	 */
	public E getRowData(int position) {
		validatePositionOrThrow(position);
		return dataList.get(position);
	}

	@Override
	public void validatePositionOrThrow(int position) {
		if (position < 0 || position >= getCount()) {
			throw new IndexOutOfBoundsException("Position requested "
					+ position + " Available list size " + getCount());
		}
	}

	@Override
	public int getCount() {
		if (null != dataList) {
			return dataList.size();
		}
		return 0;
	}

	@Override
	BaseRowViewSetter<?, ?>[] getSupportedRowViewSetters() {
		return getListRowViewSetters();
	}

	/**
	 * Adds the new item into existing list.
	 * 
	 * @param dataList
	 *            The new data list to be appended at end of current list
	 * 
	 * @see EasyListAdapter#swapDataList(List)
	 * 
	 */
	public void addItemsInList(List<E> newDataList) {
		if (newDataList != null && newDataList.isEmpty() == false) {
			this.dataList.addAll(newDataList);
		}
		notifyDataSetChanged();
	}

	/**
	 * if you want to change the complete data list then consider using this
	 * function, this will clear current data & replace with new data list
	 * passing null instead of valid data will simply clear whole data.
	 * 
	 * @param dataList
	 *            the new data list.
	 */
	public void swapDataList(List<E> newDataList) {
		this.dataList.clear();
		addItemsInList(newDataList);
	}

	/**
	 * Deletes item from list for given position
	 * 
	 * @param position
	 *            the position at which item needs to be removed.
	 */
	public void deleteItemFromList(int position) {
		validatePositionOrThrow(position);
		dataList.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * Deletes given object from list.
	 * 
	 * @param data
	 *            the object to be deleted.
	 * @return true, if successful
	 */
	public boolean deleteItemFromList(E data) {
		boolean isRemoved = dataList.remove(data);
		notifyDataSetChanged();
		return isRemoved;
	}

	/**
	 * Clears the complete data list & updates UI.
	 */
	public void clearList() {
		swapDataList(null);
	}

	/**
	 * Gets Supported ListRowViewSetters, This view setters will be used to map
	 * provided POJO data & ViewHolder, Making different RowViewSetter classes
	 * for different row-types helps build modular code & easy to reuse.
	 * 
	 * @return The array of ListRowViewSetter, which will be provided by users
	 *         of lib.
	 */
	protected abstract ListRowViewSetter<?, ?>[] getListRowViewSetters();

}

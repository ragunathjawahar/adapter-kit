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
package com.birin.gridlistviewadapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public abstract class ListGridAdapter<T, CVH> extends BaseGridAdapter<T, CVH> {

	private List<T> dataList = new ArrayList<T>();

	public ListGridAdapter(Context context, int totalCardsInRow) {
		super(context, totalCardsInRow);
	}

	@Override
	public T getCardData(int absoluteCardPosition) {
		validatePositionOrThrow(absoluteCardPosition);
		return dataList.get(absoluteCardPosition);
	}

	@Override
	public void validatePositionOrThrow(int position) {
		if (position < 0 || position >= getAbsoluteCardsCount()) {
			throw new IndexOutOfBoundsException("Position requested "
					+ position + " Available list size "
					+ getAbsoluteCardsCount());
		}
	}

	@Override
	public int getAbsoluteCardsCount() {
		if (null != dataList) {
			return dataList.size();
		}
		return 0;
	}

	/**
	 * Adds the new item into existing list.
	 * 
	 * @param dataList
	 *            The new data list to be appended at end of current list
	 * 
	 * @see ListGridAdapter#swapDataList(List)
	 * 
	 */
	public void addItemsInGrid(List<T> newDataList) {
		int newDataSize = 0;
		if (newDataList != null && newDataList.isEmpty() == false) {
			this.dataList.addAll(newDataList);
			newDataSize = getAbsoluteCardsCount();
		}
		updateGridWithNewSize(newDataSize);
	}

	/**
	 * if you want to change the complete data list then consider using this
	 * function, this will clear current data & replace with new data list
	 * passing null instead of valid data will simply clear whole data.
	 * 
	 * @param dataList
	 *            the new data list.
	 */
	public void swapDataList(List<T> newDataList) {
		this.dataList.clear();
		invalidateStructure();
		addItemsInGrid(newDataList);
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
		invalidateStructure();
		updateGridWithNewSize(getAbsoluteCardsCount());
	}

	/**
	 * Deletes given object from list.
	 * 
	 * @param data
	 *            the object to be deleted.
	 * @return true, if successful
	 */
	public boolean deleteItemFromList(T data) {
		boolean isRemoved = dataList.remove(data);
		if (isRemoved == true) {
			invalidateStructure();
			updateGridWithNewSize(getAbsoluteCardsCount());
		}
		return isRemoved;
	}

	/**
	 * Clears the complete data list & updates UI.
	 */
	public void clearList() {
		swapDataList(null);
	}
}

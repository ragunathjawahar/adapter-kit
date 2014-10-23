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

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.birin.easylistviewadapters.utils.ChildViewsClickHandler;

interface BaseRowViewSetter<E, RVH> {

	/**
	 * Get the type of row that will be created by this RowViewSetter
	 * 
	 * @return An integer representing the type of this row. Note: Integers must
	 *         be in the range 0 to (Total Supported RowViewSetters - 1).
	 */
	public abstract int getRowType();

	/**
	 * This method should return Row<RVH> object which contains 1) Row View
	 * which will be used to draw this row. 2) Row View Holder, this holder
	 * class should hold all child view references to avoid call to
	 * {@link View#findViewById(int)} on each list item scroll.
	 * 
	 * @param parent
	 *            The parent view which is passed by android.widget.BaseAdapter
	 *            during call to
	 *            {@link android.widget.BaseAdapter#getView(int, View, android.view.ViewGroup)}
	 * @return The newly created view for a this RowViewSetter class.
	 * @see Row
	 */
	public abstract Row<RVH> getNewRow(final ViewGroup parent);

	/**
	 * Set the View values by using passed data & view-holder object,essentially
	 * this method is also responsible for setting up View onClickListeners for
	 * its child Views.
	 * 
	 * @param data
	 *            Data for given row position, the data present at given
	 *            position is retrieved by library & passed to subclasses so
	 *            that, library users do NOT have to look for their data for
	 *            given position.
	 * @param rowViewHolder
	 *            the row view holder
	 * @param position
	 *            the position
	 */
	public abstract void setRowView(final E data, final RVH rowViewHolder,
			final int position);

	/**
	 * 
	 * This method should return Row<RVH> object which contains 1) Row View
	 * which will be used to draw this row. 2) Row View Holder this holder class
	 * should hold all child view references to avoid call to
	 * {@link View#findViewById(int)} on each list item scroll.
	 * 
	 * <p>
	 * NOTE: Using this is functionality to optional, alternatively user can
	 * write their own code to handle clicks for child views present in a row.
	 * </p>
	 * 
	 * 
	 * @param childViewsClickHandler
	 *            This class enables you to register children view present in
	 *            your row by registering only single {@link OnClickListener}
	 *            listener for all children click events, & at runtime it will
	 *            be determined for which row & which child event occurred, for
	 *            detailed description check {@link ChildViewsClickHandler}, All
	 *            registered views will get onClick Events in
	 *            {@link #onChildViewClicked} method for this class.
	 * 
	 * @see ChildViewsClickHandler
	 * @return The newly created view for a this RowViewSetter class.
	 */
	public abstract void registerChildrenViewClickEvents(
			final RVH rowViewHolder,
			final ChildViewsClickHandler childViewsClickHandler);

	/**
	 * On event of user-click, for any of children view which were registered in
	 * {@link #registerChildrenViewClickEvents(RVH, ChildViewsClickHandler)}
	 * this method will be called & user get few input params like,
	 * 
	 * @param clickedChildView
	 *            Registered child-view which was clicked.
	 * @param data
	 *            Data for given row position, the data present at given
	 *            position is retrieved by library & passed to view-setter class
	 *            so that, library users do NOT have to look for their data on
	 *            child-view click events.
	 * @param eventId
	 *            Event Id for clicked view, Pass different Event ID for all
	 *            children who are registering for ClickEvents this enables you
	 *            to identify click event based on Integer id, instead of
	 *            setting up different onClick handlers for each children.
	 */
	public void onChildViewClicked(final View clickedChildView,
			final E rowData, final int eventId);
}

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

/**
 * The Class Row, which bundles {@link View} with its ViewHolder, which enables
 * user to initialize their RowView & RowViewHolder within single method call of
 * {@link BaseRowViewSetter#getNewRow(android.view.ViewGroup)}.
 * 
 * <p>
 * It was even possible to separate out the initialization of both components
 * but it was bundled together for easy-initialization of RowViewHolder in case
 * of which RowView was created programmatically rather than XML inflation.
 * </p>
 * 
 * @param <RVH>
 *            the generic type which corresponds to a ViewHolder.
 */
public class Row<RVH> {

	private View rowView;

	private RVH rowViewHolder;

	public Row(View rowView, RVH rowViewHolder) {
		this.rowView = rowView;
		this.rowViewHolder = rowViewHolder;
	}

	public View getRowView() {
		return rowView;
	}

	public RVH getRowViewHolder() {
		return rowViewHolder;
	}

}

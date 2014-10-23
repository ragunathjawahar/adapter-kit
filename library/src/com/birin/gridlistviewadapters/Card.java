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

import android.view.View;

/**
 * The Class Card, which bundles {@link View} with its ViewHolder, which enables
 * user to initialize their CardView & CardViewHolder within single method call
 * of {@link BaseGridAdapter#getNewCard(int)}.
 * 
 * <p>
 * It was even possible to separate out the initialization of both components
 * but it was bundled together for easy-initialization of CardViewHolder in case
 * of which CardView was created programmatically rather than XML inflation.
 * </p>
 * 
 * @param <CVH>
 *            the generic type which corresponds to a ViewHolder instance for
 *            given card.
 */
public class Card<CVH> {

	private View cardView;

	private CVH cardViewHolder;

	public Card(View cardView, CVH cardViewHolder) {
		this.cardView = cardView;
		this.cardViewHolder = cardViewHolder;
	}

	public View getCardView() {
		return cardView;
	}

	public CVH getCardViewHolder() {
		return cardViewHolder;
	}

}

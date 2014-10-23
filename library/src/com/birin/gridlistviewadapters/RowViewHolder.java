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

/**
 * The Class RowViewHolder which holds together all CardViewHolders present in a
 * single row.
 * 
 * @param <CVH>
 *            the CardViewHolder type which is going to be held in this class.
 */
class RowViewHolder<CVH> {

	/**
	 * The card view holders list, this list holds all the CardViewHolders
	 * present in a single row.
	 */
	private ArrayList<CVH> cardViewHolders = new ArrayList<CVH>();

	/**
	 * Gets the card view holders list
	 * 
	 * @return the card view holders
	 */
	public ArrayList<CVH> getCardViewHolders() {
		return cardViewHolders;
	}
}

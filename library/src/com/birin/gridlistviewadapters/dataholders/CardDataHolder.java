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
package com.birin.gridlistviewadapters.dataholders;

/**
 * This class holds single card's data & its positional properties (which is
 * held by {@link CardPositionInfo}), This class is passed to user to setup
 * views present in card.
 * 
 * 
 * @param <T>
 *            T signifies data type which this DataHolder class is going to
 *            hold.
 * 
 * @see CardPositionInfo
 */
public class CardDataHolder<T> {

	private T data;
	private CardPositionInfo cardPositionInfo;

	public CardDataHolder(T data, CardPositionInfo cardPositionInfo) {
		this.data = data;
		this.cardPositionInfo = cardPositionInfo;
	}

	public T getData() {
		return data;
	}

	public CardPositionInfo getCardInfo() {
		return cardPositionInfo;
	}

}

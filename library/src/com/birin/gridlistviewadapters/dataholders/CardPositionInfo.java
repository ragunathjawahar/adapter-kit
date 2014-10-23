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

import com.birin.gridlistviewadapters.utils.GridDataStructure;

/**
 * This class holds single card's positional properties. This class is bundled
 * within {@link CardDataHolder} & passed to user to setup views present in
 * card. This card doesn't hold any data info but it only holds card's
 * positional properties like card-types, card's absolute position in grid
 * etc.Also this class will be used when {@link GridDataStructure} creates Grid.
 * 
 * @see CardPositionInfo
 */
public final class CardPositionInfo {

	public static final int TYPE_CONTENT = 111;
	public static final int TYPE_EMPTY = 112;

	private int type = TYPE_EMPTY;
	private int absolutePositionValue = -1;

	public CardPositionInfo(int type, int absolutePosition) {
		this.type = type;
		this.absolutePositionValue = absolutePosition;
	}

	public int getType() {
		return type;
	}

	public int getAbsolutePositionValue() {
		return absolutePositionValue;
	}

	public boolean isEmptyCard() {
		return (this.type == TYPE_EMPTY);
	}

}

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

import java.util.ArrayList;

/**
 * This class holds list of card's positional properties. This class bundles
 * {@link CardDataHolder}. This RowDataHolder class corresponds to positional
 * properties of a single row, This List<RowDataHolder> will be directly fed to
 * GridAdapters to render UI.
 * 
 * @see CardPositionInfo
 */
public final class RowDataHolder {

	private ArrayList<CardPositionInfo> cardPositionInfos = new ArrayList<CardPositionInfo>();

	public ArrayList<CardPositionInfo> getCardPositionInfos() {
		return cardPositionInfos;
	}

}

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
package com.birin.gridlistviewadapters.utils;

import java.util.List;

import com.birin.gridlistviewadapters.dataholders.CardDataHolder;
import com.birin.gridlistviewadapters.dataholders.CardPositionInfo;
import com.birin.gridlistviewadapters.dataholders.RowDataHolder;

/**
 * This is a data structure class,here user's linear data will be mapped to Grid
 * data with complexity of O(n)
 * 
 * <p>
 * Note: This class only bind the position info of cards , actual card data is
 * not bound into structure rather retrieved at runtime as per need.
 * </p>
 * 
 * <p>
 * 
 * It basically creates List of {@link RowDataHolder} which in turn holds list
 * of {@link CardDataHolder}, this list of {@link RowDataHolder} is directly fed
 * to GridAdapters for rendering.
 * </p>
 */

public class GridDataStructure {

	private int totalCardsInRow = 0;

	public GridDataStructure(int totalCardsInRow) {
		this.totalCardsInRow = totalCardsInRow;
		if (totalCardsInRow <= 0) {
			throw new IllegalArgumentException(
					"Invalid number of cards in a row should be greater than 0");
		}
	}

	/**
	 * Update grid structure, this is main method that update/create the grid
	 * data structure from total number data size, it takes list of grid-rows
	 * which will be modified and cards will be added per row basis depending
	 * upon {@link #totalCardsInRow}.
	 * 
	 */
	public void updateStructure(List<RowDataHolder> gridStructuredDataList,
			int dataListSize) {
		if (null == gridStructuredDataList) {
			throw new IllegalArgumentException("Data lists shouldn't be empty.");
		}
		int size = gridStructuredDataList.size();
		int absoluteCardposition = 0;
		int emptyCardIndex = -1;
		boolean isFirstChunk = (size == 0);
		if (isFirstChunk == false) {
			// this means gridStructuredData already is present in grid list let
			// us now check for any empty card in last row, to be able to
			// replace with new data.
			RowDataHolder lastRow = gridStructuredDataList.get(size - 1);
			if (null != lastRow) {
				absoluteCardposition = lastRow.getCardPositionInfos()
						.get(totalCardsInRow - 1).getAbsolutePositionValue() + 1;
				// start scanning from last to first card in row if empty card
				// found then assign the value to empty-index variable.
				// Scanning is stopped when a non-empty card is found.
				for (int i = totalCardsInRow - 1; i >= 0; i--) {
					CardPositionInfo positionInfo = lastRow
							.getCardPositionInfos().get(i);
					if (positionInfo.isEmptyCard() == false) {
						break;
					}
					absoluteCardposition = positionInfo
							.getAbsolutePositionValue();
					emptyCardIndex = i;
					lastRow.getCardPositionInfos().remove(i);
				}
				if (emptyCardIndex != -1) {
					// this means that we have found few empty cards,lets add
					// new card at index starting from position of empty
					// card in last row.
					for (int cardPositionInaRow = emptyCardIndex; cardPositionInaRow < totalCardsInRow; cardPositionInaRow++) {
						absoluteCardposition = addNewCard(lastRow,
								absoluteCardposition, dataListSize);
					}
				}
			}
		}
		// now we have checked probability of finding empty blocks in last row &
		// have already filled new gridStructuredData in place of them , now
		// let's add new row
		while (absoluteCardposition < dataListSize) {
			absoluteCardposition = addNewRow(gridStructuredDataList,
					dataListSize, absoluteCardposition);
		}
	}

	/**
	 * Adds the new row from gridStructuredData list and add into grid list.
	 * 
	 */
	private int addNewRow(List<RowDataHolder> gridRowList, int dataListSize,
			int absoluteCardposition) {
		RowDataHolder newRow = new RowDataHolder();
		for (int i = 0; i < totalCardsInRow; i++) {
			absoluteCardposition = addNewCard(newRow, absoluteCardposition,
					dataListSize);
		}
		gridRowList.add(newRow);
		return absoluteCardposition;
	}

	/**
	 * Adds the new card into given row.
	 * 
	 */
	private int addNewCard(RowDataHolder gridRow, int absoluteCardposition,
			int dataListSize) {
		boolean isEmptyCard = false;
		if (absoluteCardposition >= dataListSize) {
			// Create new row and add new cards into it, but if we have
			// consumed till last card of the gridStructuredData-list but
			// still if row has cards to be filled then let's fill it with
			// empty block, which will be cleared out when next chunk of
			// gridStructuredData comes.
			isEmptyCard = true;
		}
		int type = -1;
		if (isEmptyCard == true) {
			type = CardPositionInfo.TYPE_EMPTY;
		} else {
			type = CardPositionInfo.TYPE_CONTENT;
		}
		// Lets tag our grid card with absoluteCardposition, which is its
		// position in the grid, this index helps us keep mapping between our
		// linear-data to grid structure.
		CardPositionInfo cardPositionInfo = new CardPositionInfo(type,
				absoluteCardposition);
		gridRow.getCardPositionInfos().add(cardPositionInfo);
		// return absoluteCardposition, is kept as to keep mapping for card of
		// grid structure to the data list of input.
		absoluteCardposition++;
		return absoluteCardposition;
	}

}

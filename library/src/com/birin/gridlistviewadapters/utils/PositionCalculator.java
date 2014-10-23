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

import android.content.res.Configuration;

/**
 * <p>
 * Often we want to support different number of cards in Portrait & Landscape
 * screen mode.For example 3 cards in Row for Portrait mode & 4 cards in
 * Landscape mode, The challenging part is since number of card is different the
 * first visible card's row will also vary. For Example for Portrait
 * mode(3-card-row) Card#25 is present in 9th Row, but same card#25 is present
 * in 7th row in Landscape mode (4-card-row).
 * </p>
 * 
 * <p>
 * This class enables you to calculate the new row position for a card(which was
 * visible at first in old orientation.)
 * </p>
 * 
 * @see library usage demos for usage of this class.
 * 
 */
public class PositionCalculator {

	private final MaxCardsInfo maxCardsInfo;

	public PositionCalculator(MaxCardsInfo info) {
		this.maxCardsInfo = info;
	}

	/**
	 * Calculate correct row position after orientation change.
	 * 
	 * @param oldRowPosition
	 *            row position The visible row position in old orientation
	 * @param oldOrientation
	 *            orientation value for the old orientation
	 *            {@link Configuration#ORIENTATION_LANDSCAPE} or
	 *            {@link Configuration#ORIENTATION_PORTRAIT}
	 * @param newOrientation
	 *            orientation value for the new orientation
	 *            {@link Configuration#ORIENTATION_PORTRAIT} or
	 *            {@link Configuration#ORIENTATION_LANDSCAPE}
	 * @return the new row position to be selected after orientation change.
	 * @see JavaDocs for this class & library usage samples.
	 */
	public int calculatePositionInNewOrientation(
			final int VISIBLE_POSITION_IN_OLD_ORIENTATION,
			final int OLD_ORIENTATION, final int NEW_ORIENTATION) {
		int positionInNewOrientation = VISIBLE_POSITION_IN_OLD_ORIENTATION;
		if (OLD_ORIENTATION != NEW_ORIENTATION) {
			validateOrientation(OLD_ORIENTATION);
			validateOrientation(NEW_ORIENTATION);
			int maxCardsInOldOrientation = getMaxCardsFor(OLD_ORIENTATION);
			int maxCardsInNewOrientation = getMaxCardsFor(NEW_ORIENTATION);
			int firstVisibleCardIndexInOldOrientation = (VISIBLE_POSITION_IN_OLD_ORIENTATION * maxCardsInOldOrientation);
			firstVisibleCardIndexInOldOrientation += 1; // 1 based index
			positionInNewOrientation = calculateBucket(
					firstVisibleCardIndexInOldOrientation,
					maxCardsInNewOrientation);

		}
		return positionInNewOrientation;
	}

	private int calculateBucket(int firstVisibleCardIndexInOldOrientation,
			int maxCardsInNewOrientation) {
		int quotient = (firstVisibleCardIndexInOldOrientation / maxCardsInNewOrientation);
		int remainder = (firstVisibleCardIndexInOldOrientation % maxCardsInNewOrientation);
		quotient -= 1; // 0 based rows
		if (remainder == 0) {
			// First visible card is already present in quotient row.
		} else {
			// First visible card is present in next row of quotient row.
			quotient += 1;
		}
		return quotient;
	}

	/**
	 * Determine the max supported cards in current orientation.
	 * 
	 * @param orientation
	 *            . the current orientation
	 * @param maxCardsInfo
	 *            the max cards info which holds max supported cards info in
	 *            both orientation.
	 * @return the max cards for given orientation mode.
	 * @see MaxCardsInfo
	 */
	public static int getMaxCardsFor(final int ORIENTATION,
			MaxCardsInfo maxCardsInfo) {
		int maxCards = 0;
		if (ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
			maxCards = maxCardsInfo.getPotraitMaxCards();
		} else if (ORIENTATION == Configuration.ORIENTATION_LANDSCAPE) {
			maxCards = maxCardsInfo.getLandscapeMaxCards();
		}
		return maxCards;
	}

	private int getMaxCardsFor(final int ORIENTATION) {
		return getMaxCardsFor(ORIENTATION, maxCardsInfo);
	}

	private void validateOrientation(final int ORIENTATION) {
		boolean isValid = (ORIENTATION == Configuration.ORIENTATION_LANDSCAPE || ORIENTATION == Configuration.ORIENTATION_PORTRAIT);
		if (isValid == false) {
			throw new UnsupportedOperationException(
					"Unsupported Orientation: "
							+ ORIENTATION
							+ ", Valid ones are Configuration.ORIENTATION_LANDSCAPE, Configuration.ORIENTATION_PORTRAIT");
		}
	}

}

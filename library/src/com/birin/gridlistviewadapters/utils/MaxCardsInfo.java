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

/**
 * This class holds number of cards that needs to be supported in portrait &
 * landscape modes.
 * 
 */
public class MaxCardsInfo {

	private final int potraitMaxCards;
	private final int landscapeMaxCards;

	public MaxCardsInfo(final int POTRAIT_MAX_CARDS,
			final int LANDSCAPE_MAX_CARD) {
		if (POTRAIT_MAX_CARDS <= 0 || LANDSCAPE_MAX_CARD <= 0) {
			throw new ExceptionInInitializerError(
					"Max cards should be greater than zero. Potrait Max"
							+ POTRAIT_MAX_CARDS + " Landscape Max "
							+ LANDSCAPE_MAX_CARD);
		}
		this.potraitMaxCards = POTRAIT_MAX_CARDS;
		this.landscapeMaxCards = LANDSCAPE_MAX_CARD;
	}

	public int getPotraitMaxCards() {
		return potraitMaxCards;
	}

	public int getLandscapeMaxCards() {
		return landscapeMaxCards;
	}

}

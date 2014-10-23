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

import android.view.View;
import android.view.View.OnClickListener;

import com.mobsandgeeks.adapters.R;

/**
 * The Class ChildViewsClickHandler, is a class which is responsible for setting
 * a single OnClickListener class instance to all views which want to receive
 * click events. This is achieved by tagging views with its click event-id & its
 * current card position, when click event occurs this card absolute position
 * data & event id is retrieved from view tag & passed to client in callback.
 * <p>
 * Whenever {@link getNewCard()} method is called ,a new CardViewHolder is
 * returned & user registers few children of the card for clicking, by calling
 * {@link #registerChildViewForClickEvent(View, int)} method, by using Java
 * Reflections all the views within ViewHolder are scanned in each getView
 * method call by using {@link ViewHolderPositionTagger} we will tag those child
 * views with card's absolute position if they were registered for click events
 * so that this position can be used to identify when click event happens.
 * </p>
 */
public final class ChildViewsClickHandler {

	private static int TAG_VIEW_POSITION = R.string.key_view_position;
	private static int TAG_CLICK_EVENT_ID = R.string.key_click_event_id;

	private OnClickListener onClickListener;

	public ChildViewsClickHandler(OnClickListener clickEventListener) {
		this.onClickListener = clickEventListener;
	}

	/**
	 * Register child view for click event,Its recommended that ONLY children
	 * view present inside a card should register for these events for parent
	 * card view click events use {@link onCardClicked} call-back provided by
	 * GridAdapters.
	 * 
	 * @param childView
	 *            the child view (present within CardViewHolder) which needs to
	 *            receive click event.
	 * @param clickEventId
	 *            the integer identifier for this childView's click event, this
	 *            Id should be a >= 0
	 */
	public void registerChildViewForClickEvent(View childView, int clickEventId) {
		if (clickEventId < 0) {
			throw new IllegalArgumentException(
					"Event-Id should be a positive integer");
		}
		tagEventIdValueToView(childView, clickEventId);
		childView.setOnClickListener(onClickListener);
	}

	public static int getPositionFromViewTag(View view) {
		return getIntValueFromViewTag(view, TAG_VIEW_POSITION);
	}

	public static int getEventIdFromViewTag(View view) {
		return getIntValueFromViewTag(view, TAG_CLICK_EVENT_ID);
	}

	public static void tagPositionValueToView(View view, int value) {
		tagIntValueToView(view, TAG_VIEW_POSITION, value);
	}

	public static void tagEventIdValueToView(View view, int value) {
		tagIntValueToView(view, TAG_CLICK_EVENT_ID, value);
	}

	private static int getIntValueFromViewTag(View view, int key) {
		Object taggedValue;
		if (null != view && null != (taggedValue = view.getTag(key))
				&& taggedValue instanceof Integer) {
			return (Integer) taggedValue;
		} else {
			return -1;
		}
	}

	private static void tagIntValueToView(View view, int key, int value) {
		if (null != view) {
			view.setTag(key, value);
		}
	}

}

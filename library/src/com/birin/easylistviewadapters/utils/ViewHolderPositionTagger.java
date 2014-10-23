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
package com.birin.easylistviewadapters.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import android.view.View;

/**
 * <p>
 * ViewHolderPositionTagger is used to tag position Value to View on each
 * getView call, this class is responsible for scanning Views presents in
 * ViewHolder class & tagging current position value to all children views which
 * were registered for "OnClickEvent" within passed ViewHolder class.
 * </p>
 * 
 * <p>
 * Scanning is performed by using Java Reflections currently only few scanning
 * are available
 * </p>
 * 
 * <p>
 * 1) Views present directly as member variables in ViewHolder.
 * </p>
 * 
 * <p>
 * 2) Array of View/View-wrapping-POJOs present as member variables in
 * ViewHolder.
 * </p>
 * 
 * <p>
 * 3) java.util.List of View/View-wrapping-POJOs present as member variables in
 * ViewHolder.
 * </p>
 * 
 * <p>
 * Note: combinations are also supported that means java.util.List of Array of
 * TextViews are also OK.
 * 
 * Note: Avoid holding any other objects other Views.
 * </p>
 * 
 * <p>
 * For custom scanning capability override {@link handleCustomDataObject} in a
 * subclass of this {@link ViewHolderPositionTagger} class & pass into
 * EasyAdapters by overriding its getPositionTagger() method.
 * </p>
 */
public class ViewHolderPositionTagger {

	/**
	 * This method scans given object for a {@link View} class reference & if
	 * the view is registered for OnClickEvent then position value will be
	 * tagged to that view, this position will be used to retrieve data present
	 * at row position when click event occurs.
	 * 
	 * @param holder
	 *            the Object in which we need to scan for {@link View} members
	 * @param position
	 *            the row position which needs to be tagged.
	 */
	@SuppressWarnings("unchecked")
	public void scanAndTagViewsWithPositionValue(Object holder, int position) {
		for (Field field : holder.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Class<?> thisClass = field.getType();
			if (thisClass.isPrimitive() == false) {
				Object child = null;
				try {
					child = field.get(holder);
				} catch (IllegalAccessException e) {
				} catch (IllegalArgumentException e) {
				}
				if (child != null) {
					if (isViewInstance(child) == true) {
						tagObjectIfViewInstanceOrScanObjectForEnclosedViews(
								child, position);
					} else if (child instanceof Object[]) {
						tagViewsWithinArray((Object[]) child, position);
					} else if (child instanceof List<?>) {
						tagViewsWithinList((List<Object>) child, position);
					} else {
						handleCustomDataObject(child, position);
					}
				}
			}
		}
	}

	/**
	 * Tag object if its view instance or scan object for enclosed views.
	 * 
	 * @param object
	 *            the candidate for View.
	 * @param position
	 *            the row position
	 */
	protected void tagObjectIfViewInstanceOrScanObjectForEnclosedViews(
			Object object, int position) {
		if (isViewInstance(object)) {
			View view = (View) object;
			addPositionTag(view, position);
		} else {
			scanAndTagViewsWithPositionValue(object, position);
		}
	}

	/**
	 * Iterate over an array & tag all enclosed within its objects.
	 * 
	 * @param childrenArray
	 *            the array of objects for which View objects are needed to be
	 *            scanned.
	 * @param position
	 *            the row position
	 */
	private void tagViewsWithinArray(Object[] childrenArray, int position) {
		for (Object child3 : childrenArray) {
			tagObjectIfViewInstanceOrScanObjectForEnclosedViews(child3,
					position);
		}
	}

	/**
	 * Iterate over an java.util.List & tag all enclosed within its objects.
	 * 
	 * @param childrenList
	 *            the list of objects for which View objects are needed to be
	 *            scanned.
	 * @param position
	 *            the row position
	 */
	private void tagViewsWithinList(List<Object> childrenList, int position) {
		for (Object childObject : childrenList) {
			tagObjectIfViewInstanceOrScanObjectForEnclosedViews(childObject,
					position);
		}
	}

	/**
	 * We are going to scan only few types of objects present in ViewHolder if
	 * user puts any data-structure which can not be scanned then this exception
	 * is thrown,for example if user puts {@link Map} of Views into ViewHolder
	 * object then this PositionTagger file doesn't know how to retrieve Views
	 * from it so it throws error.
	 * 
	 * By overriding this method you tell library that you will handle any
	 * custom Object present inside ViewHolder & you are responsible for tagging
	 * position to Views present inside your CustomObjects.
	 * 
	 * @param child
	 *            the child
	 */
	protected void handleCustomDataObject(Object child, int position) {
		throw new IllegalArgumentException(
				"Unable to tag position data to "
						+ child.getClass().getSimpleName()
						+ " in your view-holder"
						+ " object currently only View, java.util.List of Custom-View-Wrappers,"
						+ " Array of Custom-View-wrappers are supported within ViewHolder class"
						+ " if you have custom datastructure within your ViewHolder class consider"
						+ " passing subclass of ViewHolderPositionTagger with your handling in"
						+ " handleCustomDataObject(), and passing that subclass to adapter by"
						+ " overriding getPositionTagger() method in your adapter this class.");
	}

	protected boolean isViewInstance(Object object) {
		return object instanceof View;
	}

	/**
	 * Adds the position tag to a view if its registered for clicking.
	 * 
	 * @param view
	 *            the view instance to be tagged.
	 * @param position
	 *            the row position to be tagged.
	 */
	protected void addPositionTag(View view, int position) {
		if (ChildViewsClickHandler.getEventIdFromViewTag(view) >= 0) {
			ChildViewsClickHandler.tagPositionValueToView(view, position);
		}
	}

}

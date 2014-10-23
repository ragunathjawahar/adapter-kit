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

/**
 * The listener interface for receiving onLoadMoreRequest events. The class that
 * is interested in processing a onLoadMoreRequest event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addOnLoadMoreRequestListener<code> method. When
 * the onLoadMoreRequest event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see OnLoadMoreRequestEvent
 */
public interface OnLoadMoreRequestListener {

	/**
	 * On load more requested.
	 */
	public abstract void onLoadMoreRequested();

}

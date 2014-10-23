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
package com.birin.easylistviewadapters;

import android.database.Cursor;

/**
 * The Interface CursorRowViewSetter is used to bind android.database.Cursor to
 * views by using RowViewHolder passed for EasyCursorAdapter
 * 
 * @param <RVH>
 *            Pass the your RowViewHolder Class in place of RVH so that this
 *            implementation of this Class can be used generically with
 *            android.database.Cursor data & your RowViewHolder class, refer
 *            sample project for usage demo.
 */
public interface CursorRowViewSetter<RVH> extends
		BaseRowViewSetter<Cursor, RVH> {

}

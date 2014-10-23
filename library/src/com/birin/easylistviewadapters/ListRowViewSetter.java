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

/**
 * The Interface ListRowViewSetter to be implemented for registering your
 * RowViewSetter class to EasyListAdapter.
 * 
 * @param <E>
 *            The data type with which this RowViewSetter will operate.
 * @param <RVH>
 *            Pass the your RowViewHolder Class in place of RVH so that this
 *            implementation of this Class can be used generically with your
 *            POJO data & your RowViewHolder class, refer sample project for
 *            usage demo.
 */
public interface ListRowViewSetter<E, RVH> extends BaseRowViewSetter<E, RVH> {

}

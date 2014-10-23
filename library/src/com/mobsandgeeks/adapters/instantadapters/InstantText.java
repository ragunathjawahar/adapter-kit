/*
 * Copyright (c) 2013 Mobs & Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobsandgeeks.adapters.instantadapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates methods in your model class that has to be linked with a {@link View} in your
 * custom layout. It is imperative that the method should meet the following requirements:
 *
 * <ol>
 *   <li>Should be {@code public}.</li>
 *   <li>Should have no-args or a single {@link Context} parameter method signature.</li>
 *   <li>Must have a non-void return type.</li>
 * </ol>
 *
 * <p>
 * <b>Example</b>
 * <pre>
 * class Book {
 *     ...
 *
 *     &#064;InstantText(viewId = R.id.title)
 *     public String getTitle() {
 *          return title;
 *     }
 *
 *     &#064;InstantText(viewId = R.id.author)
 *     public String getAuthor() {
 *          return author;
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InstantText {
    public int viewId();
    public int datePatternResId()   default 0;
    public String datePattern()     default "";
    public int formatStringResId()  default 0;
    public String formatString()    default "";
    public boolean isHtml()         default false;
}

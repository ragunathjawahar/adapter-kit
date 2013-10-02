/* 
 * Copyright © 2013 Mobs & Geeks
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

package com.mobsandgeeks.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * {@link InstantAdapterCore} does all the heavy lifting behind the scenes for
 * {@link InstantAdapter} and {@link InstantCursorAdapter}. We use composition
 * instead of inheritance because {@link InstantAdapter} already extends the {@link ArrayAdapter}.
 * 
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 * 
 * @param <T> The model that is being backed by the {@link InstantAdapter} or
 *          {@link InstantCursorAdapter}.
 */
class InstantAdapterCore<T> {

    // Debug
    static final String LOG_TAG = InstantAdapterCore.class.getSimpleName();
    static final boolean DEBUG = false;

    // Constants
    private static final String EMPTY_STRING = "";

    // Attributes
    private Context mContext;
    private ListAdapter mAdapter;
    private int mLayoutResourceId;
    private LayoutInflater mLayoutInflater;
    private Class<?> mDataType;
    private Set<Integer> mAnnotatedViewIds;
    private SparseArray<ViewHandler<T>> mViewHandlers;

    // Caches
    private SparseArray<Meta> mViewIdsAndMetaCache;
    private SparseArray<SimpleDateFormat> mDateFormatCache;
    private SparseArray<String> mFormatStringCache;

    /**
     * Constructs a new {@link InstantAdapterCore} for your {@link InstantAdapter} and
     * {@link InstantCursorAdapter}.
     * 
     * @param context The {@link Context} to use.
     * @param adapter The adapter using this instance of {@link InstantAdapterCore}.
     * @param layoutResourceId The resource id of your XML layout.
     * @param dataType The data type backed by your adapter.
     * 
     * @throws IllegalArgumentException If {@code context} is null or {@code layoutResourceId} is
     *          invalid or {@code type} is {@code null}.
     */
    public InstantAdapterCore(final Context context, final ListAdapter adapter,
            final int layoutResourceId, final Class<?> dataType) {
        if (context == null) {
            throw new IllegalArgumentException("'context' cannot be null.");
        } else if (layoutResourceId == View.NO_ID || layoutResourceId == 0) {
            throw new IllegalArgumentException("Invalid 'layoutResourceId', please check again.");
        } else if (dataType == null) {
            throw new IllegalArgumentException("'dataType' cannot be null.");
        }

        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mLayoutInflater = LayoutInflater.from(context);
        mDataType = dataType;
        mViewHandlers = new SparseArray<ViewHandler<T>>();
        mAnnotatedViewIds = new HashSet<Integer>();
        mViewIdsAndMetaCache = new SparseArray<Meta>();
        mDateFormatCache = new SparseArray<SimpleDateFormat>();
        mFormatStringCache = new SparseArray<String>();

        // Setup
        findAnnotatedMethods();
    }

    /**
     * Method binds a POJO to the inflated View.
     * 
     * @param parent The {@link View}'s parent, usually an {@link AdapterView} such as a
     *          {@link ListView}.
     * @param view The associated view.
     * @param instance Instance backed by the adapter at the given position.
     * @param position The list item's position.
     */
    public final void bindToView(final ViewGroup parent, final View view,
            final T instance, final int position) {
        SparseArray<Holder> holders = (SparseArray<Holder>) view.getTag(mLayoutResourceId);
        updateAnnotatedViews(holders, view, instance, position);
        executeViewHandlers(holders, parent, view, instance, position);
    }

    /**
     * Create a new view by inflating the associated XML layout.
     * 
     * @param context The {@link Context} to use.
     * @param parent The inflated view's parent.
     * @return The {@link View} that was inflated from the layout.
     */
    public final View createNewView(final Context context, final ViewGroup parent) {
        View view = mLayoutInflater.inflate(mLayoutResourceId, parent, false);
        SparseArray<Holder> holders = new SparseArray<Holder>();

        int size = mViewIdsAndMetaCache.size();
        for (int i = 0; i < size; i++) {
            int viewId = mViewIdsAndMetaCache.keyAt(i);
            Meta meta = mViewIdsAndMetaCache.get(viewId);
            View viewFromLayout = view.findViewById(viewId);
            if (viewFromLayout == null) {
                String message = String.format("Cannot find View, check the 'viewId' " +
                        "attribute on method %s.%s()",
                            mDataType.getName(), meta.method.getName());
                throw new IllegalStateException(message);
            }
            holders.append(viewId, new Holder(viewFromLayout, meta));
            mAnnotatedViewIds.add(viewId);
        }
        view.setTag(mLayoutResourceId, holders);

        return view;
    }

    /**
     * Sets an {@link ViewHandler} for a given View id.
     * 
     * @param viewId Designated View id.
     * @param viewHandler A {@link ViewHandler} for the corresponding View.
     * 
     * @throws IllegalArgumentException If evaluator is {@code null}.
     */
    public void setViewHandler(final int viewId, final ViewHandler<T> viewHandler) {
        if (viewHandler == null) {
            throw new IllegalArgumentException("'viewHandler' cannot be null.");
        }
        mViewHandlers.put(viewId, viewHandler);
    }

    /**
     * Gets the {@link ViewHandler} associated with the View id.
     *
     * @param viewId The {@link View} id whose {@link ViewHandler} we are looking for.
     *
     * @return The {@link ViewHandler} or {@code null} if one does not exist.
     */
    public ViewHandler<T> getViewHandler(final int viewId) {
        return mViewHandlers.get(viewId);
    }

    /**
     * Gets all {@link ViewHandler}s associated with this {@link InstantAdapter}.
     * 
     * @return A {@link SparseArray} containing the all {@link ViewHandler}s.
     */
    public SparseArray<ViewHandler<T>> getViewHandlers() {
        return mViewHandlers;
    }

    /**
     * Remove a {@link ViewHandler} associated with the given view id.
     * 
     * @param viewId The View id whose {@link ViewHandler} has to be removed.
     */
    public void removeViewHandler(final int viewId) {
        mViewHandlers.remove(viewId);
    }

    /**
     * Removes all {@link ViewHandler}s that are associated with this {@link InstantAdapter}.
     */
    public void removeAllViewHandlers() {
        mViewHandlers.clear();
    }

    /**
     * You should have used this a zillion times if you were doing it right. In case you
     * didn't, check this 2009 Google IO video - http://www.youtube.com/watch?v=N6YdwzAvwOA
     */
    private static class Holder {
        View view;
        Meta meta;

        Holder(final View view, final Meta meta) {
            this.view = view;
            this.meta = meta;
        }
    }

    /**
     * Class holds reference to a View's annotation and it's annotated method.
     */
    private static class Meta {
        Annotation annotation;
        Method method;

        Meta(final Annotation annotation, final Method method) {
            this.annotation = annotation;
            this.method = method;
        }
    }

    private void findAnnotatedMethods() {
        Class<?> clazz = mDataType;
        do {
            findAnnotatedMethods(clazz);
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));

        if (DEBUG) {
            Log.d(LOG_TAG, String.format("Found %d method(s)", mViewIdsAndMetaCache.size()));
        }
    }

    private void findAnnotatedMethods(Class<?> clazz) {
        if (DEBUG) {
            Log.d(LOG_TAG, "Looking for methods in " + clazz.getName());
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (isInstantAnnotation(annotation)) {
                    // Assertions
                    assertMethodIsPublic(method);
                    assertNoParamsOrSingleContextParam(method);
                    assertNonVoidReturnType(method);

                    // TODO Check if view type is compatible with the annotation
                    Meta meta = new Meta(annotation, method);
                    if (annotation instanceof InstantText) {
                        mViewIdsAndMetaCache.append(((InstantText) annotation).viewId(), meta);
                    }
                }
            }
        }
    }

    private boolean isInstantAnnotation(final Annotation annotation) {
        return annotation.annotationType().equals(InstantText.class);
    }

    private void assertMethodIsPublic(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException(String.format("%s.%s() should be public",
                            mDataType.getSimpleName(), method.getName()));
        }
    }

    private void assertNoParamsOrSingleContextParam(final Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        final int nParameters = parameters.length;
        if (nParameters > 0) {
            String errorMessage = String.format("%s.%s() can have a single Context " +
                    "parameter or should have no parameters.",
                        mDataType.getSimpleName(), method.getName());
            if (parameters.length == 1) {
                Class<?> parameterType = parameters[0];
                if (!parameterType.isAssignableFrom(Context.class)) {
                    throw new IllegalStateException(errorMessage);
                }
            } else if (parameters.length > 1) {
                throw new IllegalStateException(errorMessage);
            }
        }
    }

    private void assertNonVoidReturnType(final Method method) {
        if (method.getReturnType().equals(Void.TYPE)) {
            throw new UnsupportedOperationException(
                    String.format("Methods with void return types cannot be annotated, " +
                            "check %s.%s()", mDataType.getSimpleName(), method.getName()));
        }
    }

    private void updateAnnotatedViews(final SparseArray<Holder> holders, final View parent,
            final T instance, final int position) {
        int nHolders = holders.size();
        for (int i = 0; i < nHolders; i++) {
            Holder holder = holders.valueAt(i);
            Meta meta = holder.meta;
            if (meta == null) continue; // ViewHandler-only views will have a null meta

            Object returnValue = invokeReflectedMethod(meta.method, instance);

            // Update view from data
            Class<? extends View> viewType = holder.view.getClass();
            if (TextView.class.isAssignableFrom(viewType)) {
                updateTextView(holder, returnValue);
            }

            // Evaluators for child views
            ViewHandler<T> viewHandler = mViewHandlers.get(holder.view.getId());
            if (viewHandler != null) {
                viewHandler.handleView(mAdapter, parent, holder.view, instance, position);
            }
        }
    }

    private Object invokeReflectedMethod(final Method method, final T instance) {
        Object returnValue = null;

        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            int nParameters = parameterTypes.length;
            if (nParameters == 0) {
                returnValue = method.invoke(instance);
            } else if (nParameters == 1) {
                returnValue = method.invoke(instance, mContext);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private void updateTextView(final Holder holder, final Object returnValue) {
        InstantText instantText = (InstantText) holder.meta.annotation;
        TextView textView = (TextView) holder.view;
        int viewId = textView.getId();

        String text = null;
        if (returnValue != null) {
            text = applyDatePattern(viewId, instantText, returnValue);
            text = applyFormatString(viewId, instantText, text, returnValue);
            if (text == null) {
                text = returnValue.toString();
            }
        }

        textView.setText(instantText.isHtml() ? Html.fromHtml(text) : text);
    }

    private String applyDatePattern(final int viewId, final InstantText instantText,
            final Object returnValue) {
        int index = mDateFormatCache.indexOfKey(viewId);
        SimpleDateFormat simpleDateFormat = null;
        String text = null;

        if (index > -1) {
            simpleDateFormat = mDateFormatCache.get(viewId);
        } else {
            int datePatternRes = instantText.datePatternResId();
            String datePattern = datePatternRes != 0 ?
                    mContext.getString(datePatternRes) : instantText.datePattern();

            if (datePattern != null && !EMPTY_STRING.equals(datePattern)) {
                simpleDateFormat = new SimpleDateFormat(datePattern, Locale.getDefault());
                mDateFormatCache.put(viewId, simpleDateFormat);
            }
        }

        if (simpleDateFormat != null) {
            text = simpleDateFormat.format(returnValue);
        }

        return text;
    }

    private String applyFormatString(final int viewId, final InstantText instantText,
            final String dateFormattedString, final Object returnValue) {
        int index = mFormatStringCache.indexOfKey(viewId);
        String formatString = null;
        String formatted = dateFormattedString;

        if (index > -1) {
            formatString = mFormatStringCache.get(viewId);
        } else {
            int formatStringRes = instantText.formatStringResId();
            formatString = formatStringRes != 0 ?
                    mContext.getString(formatStringRes) : instantText.formatString();
            mFormatStringCache.put(viewId, formatString);
        }

        if (formatString != null && !EMPTY_STRING.equals(formatString)) {
            formatted = String.format(formatString, dateFormattedString != null ?
                    dateFormattedString : returnValue);
        }

        return formatted;
    }

    private void executeViewHandlers(final SparseArray<Holder> holders,
            final View parent, final View view, final T instance, final int position) {
        int nViewHandlers = mViewHandlers.size();
        for (int i = 0; i < nViewHandlers; i++) {
            int viewId = mViewHandlers.keyAt(i);
            ViewHandler<T> viewHandler = mViewHandlers.get(viewId);

            if (viewHandler == null) continue;

            if (viewId == mLayoutResourceId) {
                viewHandler.handleView(mAdapter, parent, view, instance, position);
            } else {
                Holder holder = holders.get(viewId);
                View viewWithId = null;
                if (holder != null) {
                    viewWithId = holder.view;
                } else {
                    viewWithId = view.findViewById(viewId);
                    holders.append(viewId, new Holder(viewWithId, null));
                }
                viewHandler.handleView(mAdapter, view, viewWithId, instance, position);
            }
        }
    }

}

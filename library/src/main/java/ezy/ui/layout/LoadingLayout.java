/*
 * Copyright 2016 czy1121
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

package ezy.ui.layout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ezy.library.loadinglayout.R;


public class LoadingLayout extends FrameLayout {

    LayoutInflater mInflater;
    View.OnClickListener mRetryListener;
    static int mEmptyResId = NO_ID;
    static int mLoadingResId = NO_ID;
    static int mErrorResId = NO_ID;
    int mContentId = NO_ID;
    Map<Integer, View> mLayouts = new HashMap<>();

    /**全局设置占位布局(优先级最低)
     * @param mEmptyResId
     * @param mErrorResId
     * @param mLoadingResId
     */
    public static void setGlobalConfig(int mEmptyResId,int mErrorResId,int mLoadingResId)
    {
        LoadingLayout.mEmptyResId=mEmptyResId;
        LoadingLayout.mErrorResId=mErrorResId;
        LoadingLayout.mLoadingResId=mLoadingResId;
    }


    /**动态设置占位布局(优先级最高)
     * @param id
     * @return
     */
    public LoadingLayout setLoading(@LayoutRes int id) {
        if (mLoadingResId != id) {
            remove(mLoadingResId);
            mLoadingResId = id;
        }
        return this;
    }
    public LoadingLayout setEmpty(@LayoutRes int id) {
        if (mEmptyResId != id) {
            remove(mEmptyResId);
            mEmptyResId = id;
        }
        return this;
    }

    public LoadingLayout setError(@LayoutRes int id) {
        if (mErrorResId != id) {
            remove(mErrorResId);
            mErrorResId = id;
        }
        return this;
    }


    public static LoadingLayout wrap(Activity activity) {
        return wrap(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
    }
    public static LoadingLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }
    public static LoadingLayout wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (view == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        LoadingLayout layout = new LoadingLayout(view.getContext());
        parent.addView(layout, index, lp);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }


    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr,0);
        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_emptyResId, mEmptyResId);
        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_loadingResId, mLoadingResId);
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_errorResId, mErrorResId);
        a.recycle();

    }
    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }
    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(mContentId);
    }

    private void show(int layoutId) {
        for (View view : mLayouts.values()) {
            view.setVisibility(GONE);
        }
        layout(layoutId).setVisibility(VISIBLE);
    }

    private void remove(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            View vg = mLayouts.remove(layoutId);
            removeView(vg);
        }
    }

    private View layout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, null, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);
        return layout;
    }
}

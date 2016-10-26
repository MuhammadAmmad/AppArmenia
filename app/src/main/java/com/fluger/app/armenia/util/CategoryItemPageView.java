package com.fluger.app.armenia.util;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CategoryItemPageView extends ViewPager {

    public CategoryItemPageView(Context context) {
        super(context);
    }

    public CategoryItemPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getAdapter() != null) {
            int itemHeight = measureItem((View) getAdapter().instantiateItem(this, getCurrentItem()));
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureItem(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}
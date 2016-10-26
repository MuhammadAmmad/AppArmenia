package com.fluger.app.armenia.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fluger.app.armenia.data.AppCategoryItemData;

import java.util.ArrayList;
import java.util.List;

public class BaseItemPageAdapter extends PagerAdapter {

    protected Activity activity;
    protected int position;
    protected String type;

    protected List<List<AppCategoryItemData>> appCategoryGroupDataList;

    public BaseItemPageAdapter(Activity activity, ArrayList<AppCategoryItemData> appCategoryItemDataList, String type) {
        this.activity = activity;
        this.type = type;

        appCategoryGroupDataList = new ArrayList<List<AppCategoryItemData>>();
        ArrayList<AppCategoryItemData> appCategoryItemDataListTmp = new ArrayList<AppCategoryItemData>();
        for (int i = 0; i < appCategoryItemDataList.size(); i++) {
            if (i % 3 == 0) {
                appCategoryItemDataListTmp = new ArrayList<AppCategoryItemData>();
                appCategoryItemDataListTmp.add(appCategoryItemDataList.get(i));
                appCategoryGroupDataList.add(appCategoryItemDataListTmp);
            } else {
                appCategoryItemDataListTmp.add(appCategoryItemDataList.get(i));
            }
        }

        appCategoryGroupDataList.size();
    }


    @Override
    public int getCount() {
        return appCategoryGroupDataList.size();
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.position = position;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        Intent intent = new Intent("change_bullet");
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        activity.sendBroadcast(intent);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

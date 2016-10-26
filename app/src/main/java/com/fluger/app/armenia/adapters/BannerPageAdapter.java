package com.fluger.app.armenia.adapters;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fluger.app.armenia.data.BannerData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BannerPageAdapter extends PagerAdapter {

    protected Activity activity;
    protected int position;
    protected String type;

    protected List<BannerData> bannerDataList;

    public BannerPageAdapter(Activity activity, List<BannerData> bannerDataList, String type) {
        this.activity = activity;
        this.bannerDataList = bannerDataList;
        this.type = type;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        if (position < bannerDataList.size()) {
            final BannerData bannerData = bannerDataList.get(position);
            String imageURL = Constants.FILES_URL + bannerData.getImage();
            ImageLoader.getInstance().loadImage(imageURL, null);
            ImageLoader.getInstance().displayImage(imageURL, imageView, AppArmeniaManager.getInstance().options);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equalsIgnoreCase("f_banner")) {
                        bannerData.getTitle();
                        // do tag search
                    } else if (type.equalsIgnoreCase("b_banner")) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bannerData.getUrl())));
                    }
                }
            });

            collection.addView(imageView);
        }

        return imageView;
    }

    @Override
    public int getCount() {
        return bannerDataList.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.position = position;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
        Intent intent = new Intent("change_banner_bullet");
        intent.putExtra("position", position);
        intent.putExtra("type", type);
        activity.sendBroadcast(intent);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

}

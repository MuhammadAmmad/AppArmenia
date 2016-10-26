package com.fluger.app.armenia.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fluger.app.armenia.HomeActivity;
import com.fluger.app.armenia.R;
import com.fluger.app.armenia.activity.WallpapersActivity;
import com.fluger.app.armenia.activity.details.WallpaperDetailsActivity;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


public class WallpapersItemsPageAdapter extends BaseItemPageAdapter {

    public WallpapersItemsPageAdapter(Activity activity, ArrayList<AppCategoryItemData> appCategoryItemDataList, String type) {
        super(activity, appCategoryItemDataList, type);
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout itemGroup = (LinearLayout) inflater.inflate(R.layout.item_group_wallpapers, null, false);

        int index = 0;
        for(final AppCategoryItemData appCategoryItemData : appCategoryGroupDataList.get(position)) {
            ImageView image = null;

            if(index == 0) {
                image = (ImageView) itemGroup.findViewById(R.id.left_img);
            } else if(index == 1) {
                image = (ImageView) itemGroup.findViewById(R.id.right_img_top);
            } else if(index == 2) {
                image = (ImageView) itemGroup.findViewById(R.id.right_img_bottom);
            }

            index++;

            if(image != null) {
                String imageURL = Constants.FILES_URL + appCategoryItemData.imageThumbnailUrl;
                ImageLoader.getInstance().loadImage(imageURL, new ImageSize(70, 70), null);
                ImageLoader.getInstance().displayImage(imageURL, image, AppArmeniaManager.getInstance().options);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppArmeniaManager.getInstance().itemDataToBePassed = appCategoryItemData;
                        Intent intent = new Intent(activity, WallpaperDetailsActivity.class);
                        intent.putExtra(HomeActivity.POSITION, 2);
                        activity.startActivity(intent);
                    }
                });
            }
        }

        collection.addView(itemGroup);

        return itemGroup;
    }
}
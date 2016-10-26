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
import com.fluger.app.armenia.activity.details.ApplicationDetailsActivity;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class ApplicationItemsPageAdapter extends BaseItemPageAdapter {

    public ApplicationItemsPageAdapter(Activity activity, ArrayList<AppCategoryItemData> appCategoryItemDataList, String type) {
        super(activity, appCategoryItemDataList, type);
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout itemCategoryGroupPage =  (LinearLayout) inflater.inflate(R.layout.item_group_common, null, false);

        for (final AppCategoryItemData appCategoryItemData : appCategoryGroupDataList.get(position)) {
            RelativeLayout itemAppCategory = (RelativeLayout) inflater.inflate(R.layout.item_app_category, null, false);
            TextView title = (TextView) itemAppCategory.findViewById(R.id.app_category_item_name);
            TextView downloads = (TextView) itemAppCategory.findViewById(R.id.app_category_item_download);
            ImageView image = (ImageView) itemAppCategory.findViewById(R.id.app_category_img);
            ImageView imageSound = (ImageView) itemAppCategory.findViewById(R.id.app_sound_img);
            RatingBar ratingBar = (RatingBar) itemAppCategory.findViewById(R.id.app_category_item_rating);

            final ProgressBar loader = (ProgressBar) itemAppCategory.findViewById(R.id.app_category_img_loader);

            title.setText(appCategoryItemData.title);
            ratingBar.setRating(appCategoryItemData.rating);
            downloads.setText("Download: " + appCategoryItemData.downloadCount);
            imageSound.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);

            String imageURL = Constants.FILES_URL + appCategoryItemData.imageThumbnailUrl;
            ImageLoader.getInstance().loadImage(imageURL, new ImageSize(70, 70), new ImageLoadingListener() {
                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    loader.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
            ImageLoader.getInstance().displayImage(imageURL, image, AppArmeniaManager.getInstance().options);

            itemAppCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppArmeniaManager.getInstance().itemDataToBePassed = appCategoryItemData;
                    Intent intent = new Intent(activity, ApplicationDetailsActivity.class);
                    intent.putExtra(HomeActivity.POSITION, Constants.APPLICATIONS_MENU_POSITION);
                    activity.startActivity(intent);
                }
            });
            itemCategoryGroupPage.addView(itemAppCategory);
        }

        collection.addView(itemCategoryGroupPage);

        return itemCategoryGroupPage;
    }
}
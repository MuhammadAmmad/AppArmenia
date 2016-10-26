package com.fluger.app.armenia.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fluger.app.armenia.HomeActivity;
import com.fluger.app.armenia.R;
import com.fluger.app.armenia.activity.ApplicationsActivity;
import com.fluger.app.armenia.activity.NotificationsActivity;
import com.fluger.app.armenia.activity.RingtonesActivity;
import com.fluger.app.armenia.activity.WallpapersActivity;
import com.fluger.app.armenia.adapters.ApplicationItemsPageAdapter;
import com.fluger.app.armenia.adapters.BannerPageAdapter;
import com.fluger.app.armenia.adapters.SoundItemsPageAdapter;
import com.fluger.app.armenia.adapters.WallpapersItemsPageAdapter;
import com.fluger.app.armenia.data.AppCategory;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.data.BannerData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.CategoryItemPageView;
import com.fluger.app.armenia.util.Constants;
import com.fluger.app.armenia.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppCategoriesFragment extends Fragment {

    public static final String ARG_POSITION = "position";

    private LayoutInflater inflater;

    private Map<String,List<ImageView>> categoryBulletsListMap = new HashMap<String, List<ImageView>>();
    private List<ImageView> fBannersBulletsList = new ArrayList<ImageView>();
    private List<ImageView> bBannersBulletsList = new ArrayList<ImageView>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        // inflate root view (Scrollable layout), which will contain all category groups
        View rootView = inflater.inflate(R.layout.fragment_app_categories, container, false);

        // inflate container layout to dynamically add category groups
        LinearLayout innerContainer = (LinearLayout) rootView.findViewById(R.id.innerContainer);

        // render top banners
        renderBanner(innerContainer, "f_banner");

        // foreach application, wallpapers, ringtones, notifications
        for (int i = 0; i < AppArmeniaManager.getInstance().categoriesTrendingData.size(); i++) {
            AppCategory appCategory = AppArmeniaManager.getInstance().categoriesTrendingData.get(i);
            categoryBulletsListMap.put(appCategory.name,new ArrayList<ImageView>());
            LinearLayout groupAppCategory = (LinearLayout) inflater.inflate(R.layout.group_app_category, container, false);
            RelativeLayout groupAppCategoryHeader = (RelativeLayout) groupAppCategory.findViewById(R.id.app_category_group_header);
            TextView groupName = (TextView) groupAppCategory.findViewById(R.id.app_category_group_name);

            groupAppCategoryHeader.setBackgroundColor(Color.parseColor(appCategory.color));
            groupName.setText(appCategory.name);
            groupName.setCompoundDrawablesWithIntrinsicBounds(appCategory.iconDrawableId, 0, 0, 0);

            ArrayList<AppCategoryItemData> appCategoryItemDataList = AppArmeniaManager.getInstance().categoriesTrendingData.get(i).children;

            // calculate category group bullets count
            int bulletCount = (int) Math.ceil(appCategoryItemDataList.size() / 3.0);
            // render bullets for each category group
            for (int j = 0; j < bulletCount; j++) {
                LinearLayout appCategoryGroupBulletContainer = (LinearLayout) groupAppCategory.findViewById(R.id.app_category_group_bullet_container);
                ImageView itemBullet = (ImageView) inflater.inflate(R.layout.item_bullet, container, false);
                RelativeLayout itemBulletSpace = (RelativeLayout) inflater.inflate(R.layout.item_bullet_space, container, false);

                appCategoryGroupBulletContainer.addView(itemBulletSpace);
                appCategoryGroupBulletContainer.addView(itemBullet);

                categoryBulletsListMap.get(appCategory.name).add(itemBullet);
            }

            CategoryItemPageView viewPager = (CategoryItemPageView) groupAppCategory.findViewById(R.id.app_category_group_pager);

            switch (i) {
                case 0 :
                    // Application category horizontal slider
                    viewPager.setAdapter(new ApplicationItemsPageAdapter(getActivity(), appCategoryItemDataList, Constants.CATEGORIES[0]));
                    viewPager.setOffscreenPageLimit(bulletCount);
                    groupName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ApplicationsActivity.class);
                            intent.putExtra(HomeActivity.POSITION, 1);
                            startActivity(intent);
                        }
                    });
                    break;
                case 1 :
                    // Wallpaper category horizontal slider
                    viewPager.setAdapter(new WallpapersItemsPageAdapter(getActivity(), appCategoryItemDataList, Constants.CATEGORIES[1]));
                    viewPager.setOffscreenPageLimit(bulletCount);
                    groupName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), WallpapersActivity.class);
                            intent.putExtra(HomeActivity.POSITION, 2);
                            startActivity(intent);
                        }
                    });
                    break;
                case 2 :
                    //Ringtones category horizontal slider
                    viewPager.setAdapter(new SoundItemsPageAdapter(getActivity(), appCategoryItemDataList, Constants.CATEGORIES[2]));
                    viewPager.setOffscreenPageLimit(bulletCount);
                    groupName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), RingtonesActivity.class);
                            intent.putExtra(HomeActivity.POSITION, 3);
                            startActivity(intent);
                        }
                    });
                    break;
                case 3 :
                    // Notifications category horizontal slider
                    viewPager.setAdapter(new SoundItemsPageAdapter(getActivity(), appCategoryItemDataList, Constants.CATEGORIES[3]));
                    viewPager.setOffscreenPageLimit(bulletCount);
                    groupName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), NotificationsActivity.class);
                            intent.putExtra(HomeActivity.POSITION, 4);
                            startActivity(intent);
                        }
                    });
                    break;
            }

            innerContainer.addView(groupAppCategory);
        }

        // render bottom banner
        renderBanner(innerContainer, "b_banner");

        return rootView;
    }

    private void renderBanner(LinearLayout innerContainer, String type) {
        List<BannerData> bannerDataList = AppArmeniaManager.getInstance().fBannersData;
        if(type.equals("b_banner")) {
            bannerDataList = AppArmeniaManager.getInstance().bBannersData;
        }

        LinearLayout bannerLayout = (LinearLayout) inflater.inflate(R.layout.layout_banner, null);
        LinearLayout bannerBullets = (LinearLayout) bannerLayout.findViewById(R.id.banner_bullet_container);
        CategoryItemPageView bannerPager = (CategoryItemPageView) bannerLayout.findViewById(R.id.banner_pager);
        bannerPager.setAdapter(new BannerPageAdapter(getActivity(), bannerDataList, type));
        bannerPager.setOffscreenPageLimit(bannerDataList.size());

        for (int i = 0; i < bannerDataList.size(); i++) {
            ImageView bullet = (ImageView) inflater.inflate(R.layout.item_bullet, null);
            RelativeLayout itemBulletSpace = (RelativeLayout) inflater.inflate(R.layout.item_bullet_space, bannerBullets, false);
            bullet.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bullet_bg_dark));
            bannerBullets.addView(itemBulletSpace);
            bannerBullets.addView(bullet);

            if(type.equals("b_banner")) {
                bBannersBulletsList.add(bullet);
            } else if(type.equals("f_banner")) {
                fBannersBulletsList.add(bullet);
            }
        }
        innerContainer.addView(bannerLayout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getBaseContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra("position", 0);
                String type = intent.getStringExtra("type");

                List<ImageView> bullets = categoryBulletsListMap.get(type);

                for (int i = 0; i < bullets.size(); i++) {
                    ImageView bullet = bullets.get(i);
                    if (i == position) {
                        bullet.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bullet_bg));
                    } else {
                        bullet.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bullet_stroke));
                    }
                }
            }
        }, new IntentFilter("change_bullet"));

        getActivity().getBaseContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra("position", 0);
                String type = intent.getStringExtra("type");

                List<ImageView> bullets = fBannersBulletsList;
                if(type.equals("b_banner")) {
                    bullets = bBannersBulletsList;
                }

                for (int i = 0; i < bullets.size(); i++) {
                    ImageView bullet = bullets.get(i);
                    if (i == position) {
                        bullet.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bullet_bg_dark));
                    } else {
                        bullet.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.bullet_bg_light));
                    }
                }

            }
        }, new IntentFilter("change_banner_bullet"));
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getActivity().setTitle(getActivity().getResources().getString(R.string.app_name));
        getActivity().getActionBar().setIcon(Utils.getIconIdByMenuPosition(0));
        ((HomeActivity) getActivity()).getmDrawerToggle().setDrawerIndicatorEnabled(true);
    }
}

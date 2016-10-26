package com.fluger.app.armenia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;

import com.fluger.app.armenia.backend.API;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.data.BannerData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends Activity {

    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String SENDER_ID = "1000883071473";
    private GoogleCloudMessaging gcm;

    public interface OnDataLoadListener {
        public void onDataLoadComplete();
    }

    private OnDataLoadListener onDataLoadListener = new OnDataLoadListener() {
        @Override
        public void onDataLoadComplete() {
            if (isTrendingDataLoaded && isbBannerDataLoaded && isfBannerDataLoaded && isApplicationsDataLoaded && isWallpapersDataLoaded && isRingtonesDataLoaded && isNotificationsDataLoaded) {
                AppArmeniaManager.getInstance().isDataLoaded = true;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startAppCategoriesActivity();
                    }
                });
            }
        }
    };

    public boolean isRegistrationIdLoaded;
    public boolean isTrendingDataLoaded;
    public boolean isbBannerDataLoaded;
    public boolean isfBannerDataLoaded;
    public boolean isApplicationsDataLoaded;
    public boolean isWallpapersDataLoaded;
    public boolean isRingtonesDataLoaded;
    public boolean isNotificationsDataLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build());

        AppArmeniaManager.getInstance().initializeCategories();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            if (getRegistrationId(this).isEmpty()) {
                registerInBackground();
            } else {
                loadData();
            }
        }
    }

    private void loadData() {
        if (!AppArmeniaManager.getInstance().isDataLoaded) {
            API.getTrendingItems(0, 15, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject categoryJson = result.getJSONObject(i);
                            int category = categoryJson.optInt("category", 0);
                            JSONArray categoryItemsJson = categoryJson.getJSONArray("items");
                            for (int j = 0; j < categoryItemsJson.length(); j++) {
                                AppCategoryItemData categoryItemData = new AppCategoryItemData(categoryItemsJson.getJSONObject(j));
                                categoryItemData.category = category;
                                AppArmeniaManager.getInstance().categoriesTrendingData.get(category - 1).children.add(categoryItemData);
                            }
                        }

                        isTrendingDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            API.getFBanners(0, 15, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject itemJson = result.getJSONObject(i);
                            BannerData bannerData = new BannerData();
                            bannerData.setImage(itemJson.getString("image"));
                            bannerData.setTitle(itemJson.getString("title"));
                            bannerData.setTag(itemJson.getString("tag"));
                            AppArmeniaManager.getInstance().fBannersData.add(bannerData);
                        }

                        isfBannerDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            API.getBBanners(0, 15, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject itemJson = result.getJSONObject(i);
                            BannerData bannerData = new BannerData();
                            bannerData.setImage(itemJson.getString("image"));
                            bannerData.setUrl(itemJson.getString("url"));
                            bannerData.setTitle(itemJson.getString("title"));
                            AppArmeniaManager.getInstance().bBannersData.add(bannerData);
                        }

                        isbBannerDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            API.getAppsList(5, 0, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject categoryJson = result.getJSONObject(i);
                            String type = categoryJson.optString("type", "");
                            JSONArray categoryItemsJson = categoryJson.getJSONArray("items");
                            for (int j = 0; j < categoryItemsJson.length(); j++) {
                                AppCategoryItemData categoryItemData = new AppCategoryItemData(categoryItemsJson.getJSONObject(j));
                                categoryItemData.type = type;
                                categoryItemData.category = Constants.APPLICATIONS_CATEGORY_POSITION;
                                if (!categoryItemData.androidUrl.isEmpty()) {
                                    AppArmeniaManager.getInstance().applicationsData.get(type).add(categoryItemData);
                                }
                            }
                        }

                        isApplicationsDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            API.getWallpapersList(5, 0, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject categoryJson = result.getJSONObject(i);
                            String type = categoryJson.optString("type", "");
                            JSONArray categoryItemsJson = categoryJson.getJSONArray("items");
                            for (int j = 0; j < categoryItemsJson.length(); j++) {
                                AppCategoryItemData categoryItemData = new AppCategoryItemData(categoryItemsJson.getJSONObject(j));
                                categoryItemData.type = type;
                                categoryItemData.category = Constants.WALLPAPERS_CATEGORY_POSITION;
                                AppArmeniaManager.getInstance().wallpapersData.get(type).add(categoryItemData);
                            }
                        }
                        isWallpapersDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            API.getRingtonesList(5, 0, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject categoryJson = result.getJSONObject(i);
                            String type = categoryJson.optString("type", "");
                            JSONArray categoryItemsJson = categoryJson.getJSONArray("items");
                            for (int j = 0; j < categoryItemsJson.length(); j++) {
                                AppCategoryItemData categoryItemData = new AppCategoryItemData(categoryItemsJson.getJSONObject(j));
                                categoryItemData.type = type;
                                categoryItemData.category = Constants.RINGTONES_CATEGORY_POSITION;
                                AppArmeniaManager.getInstance().ringtonesData.get(type).add(categoryItemData);
                            }
                        }
                        isRingtonesDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            API.getNotificationsList(5, 0, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONArray result = response.getJSONArray("values");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject categoryJson = result.getJSONObject(i);
                            String type = categoryJson.optString("type", "");
                            JSONArray categoryItemsJson = categoryJson.getJSONArray("items");
                            for (int j = 0; j < categoryItemsJson.length(); j++) {
                                AppCategoryItemData categoryItemData = new AppCategoryItemData(categoryItemsJson.getJSONObject(j));
                                categoryItemData.type = type;
                                categoryItemData.category = Constants.NOTIFICATIONS_CATEGORY_POSITION;
                                AppArmeniaManager.getInstance().notificationsData.get(type).add(categoryItemData);
                            }
                        }
                        isNotificationsDataLoaded = true;
                        onDataLoadListener.onDataLoadComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            startAppCategoriesActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startAppCategoriesActivity() {
//		Intent appCategoriesActivity = new Intent(MainActivity.this, AppCategoriesFragment.class);
//		startActivity(appCategoriesActivity);
        Intent appCategoriesActivity = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(appCategoriesActivity);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, resultCode).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        API.deviceId = registrationId;
        return registrationId;
    }

    private void storeRegistrationId(String regId) {
        API.deviceId = regId;
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion(MainActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private SharedPreferences getGCMPreferences() {
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    storeRegistrationId(gcm.register(SENDER_ID));
                    loadData();
                } catch (Exception e) {

                }
            }
        }.start();
    }

}

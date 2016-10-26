package com.fluger.app.armenia.backend;

import android.util.Log;

import com.fluger.app.armenia.util.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class API {

    public static String deviceId = "some-device-device-Id";

    public static final String GET_TRENDING_ITEMS = "trending/";
    public static final String GET_F_BANNERS = "fbanners/";
    public static final String GET_B_BANNERS = "bbanners/";
    public static final String GET_APPS_LIST = "applist/";
    public static final String GET_WALLPAPERS_LIST = "wplist/";
    public static final String GET_RINGTONES_LIST = "rtlist/";
    public static final String GET_NOTIFICATIONS_LIST = "ntlist/";
    public static final String SEARCH = "search/";
    public static final String GET_TAGS_LIST = "tagslist/";
    public static final String GET_APPS_SEARCH_LIST = "atsearch/";
    public static final String GET_WALLPAPERS_SEARCH_LIST = "wtsearch/";
    public static final String GET_RINGTONES_SEARCH_LIST = "rtsearch/";
    public static final String GET_NOTIFICATIONS_SEARCH_LIST = "ntsearch/";
    public static final String GET_APPS_SCREENSHOTS = "screenshots/";
    public static final String POST_RATING = "rating/";

    public static final String TAG = "API";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getTrendingItems(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_TRENDING_ITEMS, limit + "/" + offset, observer);
    }

    public static void getFBanners(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_F_BANNERS, limit + "/" + offset, observer);
    }

    public static void getBBanners(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_B_BANNERS, limit + "/" + offset, observer);
    }

    public static void getAppsList(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_APPS_LIST, limit + "/" + offset, observer);
    }

    public static void getAppsSearchList(int limit, int offset, String tag, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_APPS_SEARCH_LIST, limit + "/" + offset + "/" + tag, observer);
    }

	public static void getAppsScreenshots(int appId, AsyncHttpResponseHandler observer) {
		sendAsyncRequestGet(GET_APPS_SCREENSHOTS, "" + appId, observer);
	}
    public static void getWallpapersList(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_WALLPAPERS_LIST, limit + "/" + offset, observer);
    }

    public static void getWallpapersSearchList(int limit, int offset, String tag, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_WALLPAPERS_SEARCH_LIST, limit + "/" + offset + "/" + tag, observer);
    }

    public static void getRingtonesList(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_RINGTONES_LIST, limit + "/" + offset, observer);
    }

    public static void getRingtonesSearchList(int limit, int offset, String tag, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_RINGTONES_SEARCH_LIST, limit + "/" + offset + "/" + tag, observer);
    }

    public static void getNotificationsList(int limit, int offset, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_NOTIFICATIONS_LIST, limit + "/" + offset, observer);
    }

    public static void getNotificationsSearchList(int limit, int offset, String tag, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_NOTIFICATIONS_SEARCH_LIST, limit + "/" + offset + "/" + tag, observer);
    }

    public static void search(int limit, int offset, String query, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(SEARCH, limit + "/" + offset + "/" + query, observer);
    }

    public static void getTagsList(int category, AsyncHttpResponseHandler observer) {
        sendAsyncRequestGet(GET_TAGS_LIST, "" + category, observer);
    }
    
    public static void postRating(int postId, int rating, AsyncHttpResponseHandler observer) {
    	sendAsyncRequestGet(POST_RATING, postId + "/" + rating, observer);
    }

    private static void sendAsyncRequestGet(String command, String requestStr, AsyncHttpResponseHandler responseHandler) {
        requestStr = Constants.SERVER_URL + command + deviceId + "/" + requestStr;

        RequestParams params = new RequestParams();

        debugLog(requestStr, params);
        client.post(requestStr, params, responseHandler);
    }

    private static void debugLog(String url, RequestParams params) {
        Log.i(TAG, url);
        Log.i(TAG, params.toString());
    }

}

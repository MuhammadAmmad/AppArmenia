package com.fluger.app.armenia;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.fluger.app.armenia.activity.SearchResultsActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            String message = intent.getStringExtra("message");
            String category = intent.getStringExtra("category");
            String tag = intent.getStringExtra("tag");

            if(category != null && tag != null) {
                Intent searchActivityIntent = new Intent(this, SearchResultsActivity.class);
                searchActivityIntent.putExtra("notif", true);
                searchActivityIntent.putExtra(SearchManager.QUERY, tag);
                searchActivityIntent.putExtra("category", Integer.parseInt(category));
                searchActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, searchActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentTitle("App Armenia")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setContentText(message);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
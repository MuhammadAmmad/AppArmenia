package com.fluger.app.armenia.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fluger.app.armenia.HomeActivity;
import com.fluger.app.armenia.R;
import com.fluger.app.armenia.activity.details.NotificationDetailsActivity;
import com.fluger.app.armenia.activity.details.RingtonesDetailsActivity;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.fluger.app.armenia.util.Constants;

import java.io.IOException;
import java.util.ArrayList;

public class SoundItemsPageAdapter extends BaseItemPageAdapter {

    private static ImageView prevSoundImg = null;
    private static ImageView prevSoundAnim = null;

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    public SoundItemsPageAdapter(Activity activity, ArrayList<AppCategoryItemData> appCategoryItemDataList, String type) {
        super(activity, appCategoryItemDataList, type);
    }

    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout itemCategoryGroupPage = (LinearLayout) inflater.inflate(R.layout.item_group_common, null, false);

        for (final AppCategoryItemData appCategoryItemData : appCategoryGroupDataList.get(position)) {
            RelativeLayout itemAppCategory = (RelativeLayout) inflater.inflate(R.layout.item_app_category, null, false);
            TextView title = (TextView) itemAppCategory.findViewById(R.id.app_category_item_name);
            TextView downloads = (TextView) itemAppCategory.findViewById(R.id.app_category_item_download);
            ImageView image = (ImageView) itemAppCategory.findViewById(R.id.app_category_img);
            RatingBar ratingBar = (RatingBar) itemAppCategory.findViewById(R.id.app_category_item_rating);
            final ProgressBar loader = (ProgressBar) itemAppCategory.findViewById(R.id.app_category_img_loader);
            final ImageView imageSound = (ImageView) itemAppCategory.findViewById(R.id.app_sound_img);
            final ImageView imageSoundAnim = (ImageView) itemAppCategory.findViewById(R.id.app_sound_anim);

            title.setText(appCategoryItemData.title);
            ratingBar.setRating(appCategoryItemData.rating);
            downloads.setText("Download: " + appCategoryItemData.downloadCount);
            image.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            itemCategoryGroupPage.addView(itemAppCategory);

            imageSoundAnim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageSound.setVisibility(View.VISIBLE);
                    imageSoundAnim.setVisibility(View.GONE);
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
            });

            imageSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(prevSoundAnim != null && prevSoundImg != null) {
                        prevSoundImg.setVisibility(View.VISIBLE);
                        prevSoundAnim.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        if(mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        }
                    }

                    prevSoundAnim = imageSoundAnim;
                    prevSoundImg = imageSound;

                    imageSound.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);

                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(activity, Uri.parse(Constants.FILES_URL + appCategoryItemData.audioUrl));
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                Glide.with(activity).load(R.drawable.equalizer_play_button).into(imageSoundAnim);
                                imageSoundAnim.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                                mediaPlayer.start();
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                imageSound.setVisibility(View.VISIBLE);
                                imageSoundAnim.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                            }
                        });
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {

                    }
                }
            });

            itemAppCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equalsIgnoreCase(Constants.CATEGORIES[2])) { // Ringtones
                        AppArmeniaManager.getInstance().itemDataToBePassed = appCategoryItemData;
                        Intent ringtonesDetailsActivity = new Intent(activity, RingtonesDetailsActivity.class);
                        ringtonesDetailsActivity.putExtra(HomeActivity.POSITION, Constants.RINGTONES_MENU_POSITION);
                        activity.startActivity(ringtonesDetailsActivity);
                    } else if (type.equalsIgnoreCase(Constants.CATEGORIES[3])) { //Notifications
                        AppArmeniaManager.getInstance().itemDataToBePassed = appCategoryItemData;
                        Intent ringtonesDetailsActivity = new Intent(activity, NotificationDetailsActivity.class);
                        ringtonesDetailsActivity.putExtra(HomeActivity.POSITION, Constants.NOTIFICATIONS_MENU_POSITION);
                        activity.startActivity(ringtonesDetailsActivity);
                    }
                }
            });
        }

        collection.addView(itemCategoryGroupPage);

        return itemCategoryGroupPage;
    }

    public static void stopMediaPlayer() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
            mediaPlayer.stop();
        }
    }
}
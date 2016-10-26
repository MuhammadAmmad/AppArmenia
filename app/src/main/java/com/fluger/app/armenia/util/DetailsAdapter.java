package com.fluger.app.armenia.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fluger.app.armenia.R;
import com.fluger.app.armenia.activity.SearchResultsActivity;
import com.fluger.app.armenia.backend.API;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DetailsAdapter extends ArrayAdapter<String> {
	
	private Context context;
	private int category;
	private Handler handler;

	public DetailsAdapter(Context context, int resource, ArrayList<String> objects, int category) {
		super(context, resource, objects);
		this.context = context;
		this.category = category;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_details_list, parent, false);
		}
		if (position == 0) {
			((TextView) convertView.findViewById(R.id.item_name)).setText(getItem(position));
		} else if (position == 1) {
			((TextView) convertView.findViewById(R.id.item_name)).setVisibility(View.GONE);
			final String[] tags = getItem(position).split(",");
			for (int i = 0; i < tags.length; i++) {
				TextView txtView = new TextView(context);
				txtView.setBackgroundResource(R.drawable.details_tag_background);
				txtView.setText("#" + tags[i]);
				txtView.setPadding(10, 10, 10, 10);
				txtView.setTextColor(context.getResources().getColor(R.color.grey_2));
				txtView.setGravity(Gravity.CENTER);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			    params.setMargins(5, 5, 5, 5);
			    params.gravity = Gravity.LEFT;
			    txtView.setLayoutParams(params);
			    ((LinearLayout) convertView.findViewById(R.id.details_container)).addView(txtView);
			    
			    final int index = i;
			    txtView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent searchActivity = new Intent(context, SearchResultsActivity.class);
						searchActivity.setAction(Intent.ACTION_SEARCH);
						searchActivity.putExtra("query", tags[index]);
						searchActivity.putExtra("category", category);
						context.startActivity(searchActivity);
					}
				});
			}
		} else if (position == 2) {
			((TextView) convertView.findViewById(R.id.item_name)).setText(getItem(position));
		} else if (position == 3) {
			((TextView) convertView.findViewById(R.id.item_name)).setText(getItem(position));
		} else if (position == 4) {
			((LinearLayout) convertView.findViewById(R.id.details_container)).setGravity(Gravity.CENTER);
			((TextView) convertView.findViewById(R.id.item_name)).setVisibility(View.GONE);
			((RatingBar) convertView.findViewById(R.id.item_rating_bar)).setVisibility(View.VISIBLE);
			
			((RatingBar) convertView.findViewById(R.id.item_rating_bar)).setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				
				@Override
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
					final int lastRating = (int)rating;
					if (handler == null) {
						handler = new Handler();
						handler.postDelayed(new Runnable() {
						  @Override
						  public void run() {
							  
							  API.postRating(AppArmeniaManager.getInstance().itemDataToBePassed.id, lastRating, new JsonHttpResponseHandler() {
								
								@Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
									((Activity) context).runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											Toast.makeText(context, "Rated!", Toast.LENGTH_LONG).show();
										}
									});
								}

							});
						  }
						}, 2000);
					}
				}
			});
		}

		return convertView;
	}

}

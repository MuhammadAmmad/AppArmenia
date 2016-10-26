package com.fluger.app.armenia.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fluger.app.armenia.R;
import com.fluger.app.armenia.data.AppCategoryItemData;
import com.fluger.app.armenia.manager.AppArmeniaManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

public class ItemsAdapter extends ArrayAdapter<AppCategoryItemData> {
	private Context context;
	
	public ItemsAdapter(Context context, int resource, List<AppCategoryItemData> objects) {
		super(context, resource, objects);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AppCategoryItemData child = (AppCategoryItemData) getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_applications_list, parent, false);
		}
		((TextView) convertView.findViewById(R.id.applications_item_name)).setText(child.title);
		((TextView) convertView.findViewById(R.id.applications_item_download)).setText("Downloads: " + child.downloadCount);
		((RatingBar) convertView.findViewById(R.id.applications_item_rating)).setRating(child.rating);
		
		if (child.category == Constants.APPLICATIONS_CATEGORY_POSITION) {
			String url = Constants.FILES_URL + child.imageUrl;
			ImageSize targetSize = new ImageSize(80, 80);
			ImageLoader.getInstance().loadImage(url, targetSize, null);
			ImageLoader.getInstance().displayImage(url, (ImageView) convertView.findViewById(R.id.applications_item_img), AppArmeniaManager.getInstance().options);
		}
		
		return convertView;
	}
	
}
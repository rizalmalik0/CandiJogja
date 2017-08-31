package com.candijogja.gis;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	Context mContext;
	List<String> images;

	public ImageAdapter(Context context, List<String> images) {
		mContext = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView img = new ImageView(mContext);
		img.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, 200));

		Picasso.with(mContext)
				.load("file:///android_asset/image/" + images.get(position))
				.fit().centerCrop().into(img);

		return img;
	}

}

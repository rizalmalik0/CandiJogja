package com.candijogja.gis;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private Context mContext;
	private List<Candi> candi;

	public CustomAdapter(Context context, List<Candi> candi) {
		this.mContext = context;
		this.candi = candi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return candi.size();
	}

	@Override
	public Object getItem(int position) {
		return candi.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Candi c = candi.get(position);

		// inflater
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.list_item, parent, false);
		}

		// init
		ImageView imgItem = (ImageView) row.findViewById(R.id.imgItem);
		TextView txtNama = (TextView) row.findViewById(R.id.txtItemNama);
		TextView txtAlamat = (TextView) row.findViewById(R.id.txtItemAlamat);
		TextView txtSejarah = (TextView) row.findViewById(R.id.txtItemSejarah);
		TextView txtJarak = (TextView) row.findViewById(R.id.txtItemJarak);

		// set tag to button

		// settext on text view
		Picasso.with(mContext)
				.load("file:///android_asset/image/" + c.getGambar())
				.fit().centerCrop().into(imgItem);
		txtNama.setText(c.getNama_candi());
		txtAlamat.setText(c.getAlamat());
		txtSejarah.setText(c.getSejarah());
		txtJarak.setText(c.getJarak()+"KM");

		return row;
	}

}

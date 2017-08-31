package com.candijogja.gis;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class DetailCandi extends Activity {
	TextView namaCandi, alamat, biaya, jamBuka, jamTutup, sejarahCandi;
	Gallery gambarCandi;
	DataAdapter data;
	List<String> gambar;
	Button btnNavigasi;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.detail_candi);

		// init
		data = new DataAdapter(this);
		gambarCandi = (Gallery) findViewById(R.id.galleryCandi);
		namaCandi = (TextView) findViewById(R.id.txtNamaCandi);
		alamat = (TextView) findViewById(R.id.txtAlamat);
		jamBuka = (TextView) findViewById(R.id.txtJamBuka);
		jamTutup = (TextView) findViewById(R.id.txtJamTutup);
		biaya = (TextView) findViewById(R.id.txtBiaya);
		sejarahCandi = (TextView) findViewById(R.id.txtSejarah);
		btnNavigasi = (Button) findViewById(R.id.btnNavigasi);

		// data
		int id = getIntent().getIntExtra("id", 0);
		data.open();
		Candi c = data.getDetailCandi(id);
		gambar = data.getGambarCandi(id);
		data.close();

		// adapter
		gambarCandi.setAdapter(new ImageAdapter(this, gambar));
		namaCandi.setText(c.getNama_candi());
		alamat.setText(c.getAlamat());
		sejarahCandi.setText(c.getSejarah());
		jamBuka.setText("Jam Buka : " + c.getJam_buka());
		jamTutup.setText("Jam Tutup : " + c.getJam_tutup());
		biaya.setText("Biaya : Rp. " + c.getBiaya());
		btnNavigasi.setTag(id);
	}

	public void navigasi(View v) {
		int id = (Integer) v.getTag();
		Intent i = new Intent(this, Rute.class);
		i.putExtra("id", id);
		startActivity(i);
	}
}

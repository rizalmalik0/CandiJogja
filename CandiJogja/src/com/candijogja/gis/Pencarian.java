package com.candijogja.gis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Pencarian extends Activity {
	Gallery galleryPencarian;
	RadioGroup rgPencarian;
	RadioButton lokasiTerdekat, biaya, namaCandi;
	EditText dari, sampai, nama;
	List<String> images;
	String selected = "Lokasi";

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.pencarian);

		// init
		images = new ArrayList<String>();
		dari = (EditText) findViewById(R.id.etDari);
		sampai = (EditText) findViewById(R.id.etSampai);
		nama = (EditText) findViewById(R.id.etNama);
		galleryPencarian = (Gallery) findViewById(R.id.galleryPencarian);
		rgPencarian = (RadioGroup) findViewById(R.id.rgPencarian);
		lokasiTerdekat = (RadioButton) findViewById(R.id.rbLokasi);
		biaya = (RadioButton) findViewById(R.id.rbBiaya);
		namaCandi = (RadioButton) findViewById(R.id.rbNama);

		// gallery adapter
		lokasiTerdekat.setChecked(true);
		dari.setEnabled(false);
		sampai.setEnabled(false);
		nama.setEnabled(false);
		images.add("candi_abang_3.jpg");
		images.add("candi_ijo_3.jpg");
		images.add("candi_barong_3.jpg");
		images.add("candi_kedulan_3.jpg");
		galleryPencarian.setAdapter(new ImageAdapter(this, images));

		// listener
		rgPencarian.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rbLokasi:
					selected = "Lokasi";
					dari.setEnabled(false);
					sampai.setEnabled(false);
					nama.setEnabled(false);
					break;
				case R.id.rbBiaya:
					selected = "Biaya";
					dari.setEnabled(true);
					sampai.setEnabled(true);
					nama.setEnabled(false);
					break;
				case R.id.rbNama:
					selected = "Nama";
					dari.setEnabled(false);
					sampai.setEnabled(false);
					nama.setEnabled(true);
					break;
				default:
					break;
				}
			}
		});
	}

	public void cari(View v) {
		Intent i = new Intent(this, ListCandi.class);
		i.putExtra("selected", selected);
		if (selected.equals("Biaya")) {
			if (dari.getText().toString().equals("")
					|| sampai.getText().toString().equals("")) {
				Toast.makeText(Pencarian.this, "Biaya Tidak Boleh Kosong",
						Toast.LENGTH_SHORT).show();
				dari.requestFocus();
			} else {
				int isiDari = Integer.valueOf(dari.getText().toString());
				int isiSampai = Integer.valueOf(sampai.getText().toString());
				if (isiDari > isiSampai) {

					Toast.makeText(Pencarian.this,
							"Biaya Sampai Tidak Boleh Lebih Kecil",
							Toast.LENGTH_SHORT).show();
					dari.requestFocus();
				} else {
					i.putExtra("dari", isiDari);
					i.putExtra("sampai", isiSampai);
					startActivity(i);
				}
			}

		} else if (selected.equals("Nama")) {
			String isiNama = nama.getText().toString();
			if (isiNama.trim().equals("")) {
				Toast.makeText(Pencarian.this, "Nama Tidak Boleh Kosong",
						Toast.LENGTH_SHORT).show();
				nama.requestFocus();
			} else {
				i.putExtra("nama", isiNama);
				startActivity(i);
			}
		} else {
			startActivity(i);
		}
	}
}

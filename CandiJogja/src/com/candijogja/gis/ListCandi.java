package com.candijogja.gis;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListCandi extends Activity implements OnItemClickListener{
	TextView txtNotFound;
	ListView listCandi;
	AutoCompleteTextView cari;
	LinearLayout layoutPencarian;
	List<Candi> candi;
	DataAdapter data;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.list_candi);

		// init
		data = new DataAdapter(this);
		txtNotFound = (TextView)findViewById(R.id.txtNotFound);
		listCandi = (ListView) findViewById(R.id.lvCandi);
		cari = (AutoCompleteTextView) findViewById(R.id.etCari);
		layoutPencarian = (LinearLayout) findViewById(R.id.layoutPencarian);

		// getData
		data.open();
		String pencarian = getIntent().getStringExtra("selected");
		if (pencarian != null) {
			layoutPencarian.setVisibility(View.GONE);
			if (pencarian.equals("Biaya")) {
				int dari = getIntent().getIntExtra("dari", 0);
				int sampai = getIntent().getIntExtra("sampai", 0);
				candi = data.getCandiBiaya(dari, sampai);
			} else if (pencarian.equals("Nama")) {
				String nama = getIntent().getStringExtra("nama");
				candi = data.getCandiNama(nama);
			} else {
				candi = data.getCandiTerdekat();
			}
		} else {
			candi = data.getSemuaCandi();
		}
		data.close();

		// adapter
		if (candi.isEmpty()) {
			txtNotFound.setVisibility(View.VISIBLE);
		}
		cari.setAdapter(new ArrayAdapter<Candi>(this,
				android.R.layout.simple_list_item_1, candi));
		listCandi.setAdapter(new CustomAdapter(this, candi));
		
		// listener
		cari.setOnItemClickListener(this);
		listCandi.setOnItemClickListener(this);
	}

	public void cari(View v) {
		Intent i = new Intent(this, ListCandi.class);
		i.putExtra("selected", "Nama");
		i.putExtra("nama", cari.getText().toString());
		startActivity(i);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		cari.setText("");
		
		Candi c = (Candi) parent.getItemAtPosition(position);
		Intent i = new Intent(ListCandi.this, DetailCandi.class);
		i.putExtra("id", c.getId_candi());
		startActivity(i);
	}
}

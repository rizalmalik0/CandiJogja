package com.candijogja.gis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

public class DataAdapter extends SQLiteAssetHelper{
	private static final String DATABASE_NAME = "candi.db";
	private static final int DATABASE_VERSION = 4;
	private SQLiteDatabase mDb;
	Context mContext;
	private Location origin;

	/*
	 * DATA ADAPTER OPEN CLOSE
	 */

	public DataAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
		setForcedUpgrade(DATABASE_VERSION);
		
		// get origin
		GPSTracker gps = new GPSTracker(context);
		if (gps.getLocation()==null) {
			gps.showSettingAlert();
		} else {
			origin = new Location(gps.getLocation());
		}
	}

	public DataAdapter open() throws SQLException {
		try {
			mDb = this.getReadableDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void close() {
		mDb.close();
	}

	/*
	 * DATA ADAPTER READ DATA
	 */

	public List<Candi> getSemuaCandi() {
		List<Candi> candi = new ArrayList<Candi>();
		String[] columns = new String[] { "candi.id_candi", "nama_candi",
				"alamat", "biaya", "substr(sejarah,0,100)||'...'", "jam_tutup",
				"jam_buka", "latitude", "longitude", "file" };

		Cursor cursor = mDb.query("candi JOIN gambar", columns,
				"candi.id_candi=gambar.id_candi", null, "candi.id_candi", null,
				null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Candi c = new Candi();

			int jarak = getDistance(cursor.getDouble(7), cursor.getDouble(8));
			c.setId_candi(cursor.getInt(0));
			c.setNama_candi(cursor.getString(1));
			c.setAlamat(cursor.getString(2));
			c.setBiaya(cursor.getInt(3));
			c.setSejarah(cursor.getString(4));
			c.setJam_tutup(cursor.getString(5));
			c.setJam_buka(cursor.getString(6));
			c.setGambar(cursor.getString(9));
			c.setJarak(jarak);

			candi.add(c);
		}

		return candi;
	}

	public Candi getDetailCandi(int id) {
		String[] column = new String[] { "nama_candi", "alamat", "biaya",
				"sejarah", "jam_tutup", "jam_buka", "latitude", "longitude" };
		Cursor cursor = mDb.query("candi", column, "id_candi=" + id, null,
				null, null, null);

		cursor.moveToFirst();

		Candi c = new Candi();
		c.setNama_candi(cursor.getString(0));
		c.setAlamat(cursor.getString(1));
		c.setBiaya(cursor.getInt(2));
		c.setSejarah(cursor.getString(3));
		c.setJam_tutup(cursor.getString(4));
		c.setJam_buka(cursor.getString(5));
		c.setLatitude(cursor.getDouble(6));
		c.setLongitude(cursor.getDouble(7));

		return c;
	}

	public List<String> getGambarCandi(int id) {
		List<String> gambar = new ArrayList<String>();
		Cursor cursor = mDb.query("gambar", new String[] { "file" },
				"id_candi=" + id, null, null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			gambar.add(cursor.getString(0));
		}

		return gambar;
	}

	public List<Candi> getCandiTerdekat() {
		List<Candi> candi = new ArrayList<Candi>();
		String[] columns = new String[] { "candi.id_candi", "nama_candi",
				"alamat", "biaya", "substr(sejarah,0,100)||'...'", "jam_tutup",
				"jam_buka", "latitude", "longitude", "file" };

		Cursor cursor = mDb.query("candi JOIN gambar", columns,
				"candi.id_candi=gambar.id_candi", null, "candi.id_candi", null,
				null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Candi c = new Candi();

			int jarak = getDistance(cursor.getDouble(7), cursor.getDouble(8));
			c.setId_candi(cursor.getInt(0));
			c.setNama_candi(cursor.getString(1));
			c.setAlamat(cursor.getString(2));
			c.setBiaya(cursor.getInt(3));
			c.setSejarah(cursor.getString(4));
			c.setJam_tutup(cursor.getString(5));
			c.setJam_buka(cursor.getString(6));
			c.setGambar(cursor.getString(9));
			c.setJarak(jarak);

			candi.add(c);
		}

		sortList(candi);
		return candi;
	}

	public List<Candi> getCandiNama(String nama) {
		List<Candi> candi = new ArrayList<Candi>();
		String[] columns = new String[] { "candi.id_candi", "nama_candi",
				"alamat", "biaya", "substr(sejarah,0,100)||'...'", "jam_tutup",
				"jam_buka", "latitude", "longitude", "file" };

		Cursor cursor = mDb.query("candi JOIN gambar", columns,
				"candi.id_candi=gambar.id_candi AND nama_candi LIKE '%" + nama
						+ "%'", null, "candi.id_candi", null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Candi c = new Candi();

			int jarak = getDistance(cursor.getDouble(7), cursor.getDouble(8));
			c.setId_candi(cursor.getInt(0));
			c.setNama_candi(cursor.getString(1));
			c.setAlamat(cursor.getString(2));
			c.setBiaya(cursor.getInt(3));
			c.setSejarah(cursor.getString(4));
			c.setJam_tutup(cursor.getString(5));
			c.setJam_buka(cursor.getString(6));
			c.setGambar(cursor.getString(9));
			c.setJarak(jarak);

			candi.add(c);
		}

		sortList(candi);
		return candi;
	}

	public List<Candi> getCandiBiaya(int dari, int sampai) {
		List<Candi> candi = new ArrayList<Candi>();
		String[] columns = new String[] { "candi.id_candi", "nama_candi",
				"alamat", "biaya", "substr(sejarah,0,100)||'...'", "jam_tutup",
				"jam_buka", "latitude", "longitude", "file" };

		Cursor cursor = mDb.query("candi JOIN gambar", columns,
				"candi.id_candi=gambar.id_candi AND biaya >= " + dari
						+ " AND biaya <= " + sampai, null, "candi.id_candi",
				null, null, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Candi c = new Candi();

			int jarak = getDistance(cursor.getDouble(7), cursor.getDouble(8));
			c.setId_candi(cursor.getInt(0));
			c.setNama_candi(cursor.getString(1));
			c.setAlamat(cursor.getString(2));
			c.setBiaya(cursor.getInt(3));
			c.setSejarah(cursor.getString(4));
			c.setJam_tutup(cursor.getString(5));
			c.setJam_buka(cursor.getString(6));
			c.setGambar(cursor.getString(9));
			c.setJarak(jarak);

			candi.add(c);
		}

		sortList(candi);
		return candi;
	}

	private int getDistance(double lat, double lng) {
		// set location tujuan
		Location tujuan = new Location("tujuan");
		tujuan.setLatitude(lat);
		tujuan.setLongitude(lng);

		// get jarak
		if (origin == null) {
			return 0;
		} else {
			int jarak = (int) origin.distanceTo(tujuan) / 1000;
			return jarak;
		}
	}

	private void sortList(List<Candi> candi) {
		Collections.sort(candi, new Comparator<Candi>() {
			@Override
			public int compare(Candi c1, Candi c2) {
				return c1.getJarak() - c2.getJarak();
				// for string
				// return
				// a1.getNama_apotek().compareToIgnoreCase(a2.getNama_apotek());
			}
		});
	}
}

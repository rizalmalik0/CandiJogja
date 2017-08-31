package com.candijogja.gis;

public class Candi {
	int id_candi, biaya, jarak;
	String nama_candi, alamat, sejarah, jam_tutup, jam_buka, gambar;
	Double latitude, longitude;

	public int getId_candi() {
		return id_candi;
	}

	public void setId_candi(int id_candi) {
		this.id_candi = id_candi;
	}

	public int getBiaya() {
		return biaya;
	}

	public void setBiaya(int biaya) {
		this.biaya = biaya;
	}

	public int getJarak() {
		return jarak;
	}

	public void setJarak(int jarak) {
		this.jarak = jarak;
	}

	public String getNama_candi() {
		return nama_candi;
	}

	public void setNama_candi(String nama_candi) {
		this.nama_candi = nama_candi;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getSejarah() {
		return sejarah;
	}

	public void setSejarah(String sejarah) {
		this.sejarah = sejarah;
	}

	public String getJam_tutup() {
		return jam_tutup;
	}

	public void setJam_tutup(String jam_tutup) {
		this.jam_tutup = jam_tutup;
	}

	public String getJam_buka() {
		return jam_buka;
	}

	public void setJam_buka(String jam_buka) {
		this.jam_buka = jam_buka;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getGambar() {
		return gambar;
	}

	public void setGambar(String gambar) {
		this.gambar = gambar;
	}

	@Override
	public String toString() {
		return getNama_candi();
	}
}

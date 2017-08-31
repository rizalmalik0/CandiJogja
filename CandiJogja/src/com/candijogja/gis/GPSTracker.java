package com.candijogja.gis;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
	private Context mContext;
	protected LocationManager locationManager;

	// GPS check
	boolean isGPSEnabled = false;
	boolean isNetworEnabled = false;
	boolean canGetLocation = false;

	// location
	Location location;
	double latitude;
	double longitude;

	// location listener
	private static final long MIN_DISTANCE_FOR_UPDATES = 10; // meter
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // milisecond

	public GPSTracker() {

	}

	public GPSTracker(Context mContext) {
		this.mContext = mContext;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// check network provider
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworEnabled) {
				// no network provider enabled
			} else {
				this.canGetLocation = true;
				// get from network provider
				if (isNetworEnabled) {
					locationManager
							.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS enabled get lat/long using GPS Service
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES, MIN_DISTANCE_FOR_UPDATES,
								this);
						Log.d("GPS Enabled", "Gps Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/*
	 * GET latitude longitude
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		return latitude;
	}

	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		return longitude;
	}

	/*
	 * check if best network provider
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettingAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("GPS Setting");
		alertDialog.setMessage("GPS is not enabled. Go to Setting");
		// alertDialog.setIcon(R.drawable.delete);

		alertDialog.setPositiveButton("Setting",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(i);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	/*
	 * Stop using GPS Listener
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		location.getLatitude();
		location.getLongitude();

		// Toast.makeText(mContext, location.getLatitude() + " " +
		// location.getLongitude(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
		getLocation();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}

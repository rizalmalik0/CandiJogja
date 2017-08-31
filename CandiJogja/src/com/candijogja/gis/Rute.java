package com.candijogja.gis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Rute extends Activity {
	GoogleMap map;
	LatLng origin, destination;
	List<LatLng> points;
	LatLngBounds bound;
	ProgressDialog dialog;

	private int GPS_ERRORDIALOG_REQUEST = 9001;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);

		GPSTracker gps = new GPSTracker(this);

		origin = new LatLng(gps.getLatitude(), gps.getLongitude());

		// destination
		int id = getIntent().getIntExtra("id", 0);

		DataAdapter data = new DataAdapter(this);
		data.open();
		Candi c = data.getDetailCandi(id);
		data.close();

		String judul = c.getNama_candi();
		String isi = c.getAlamat();
		destination = new LatLng(c.getLatitude(), c.getLongitude());

		// check service
		if (serviceOK()) {
			setContentView(R.layout.rute);

			MapFragment fm = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.layoutPeta);
			map = fm.getMap();

			// Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
			map.setMyLocationEnabled(true);

			if (origin.latitude == 0 && origin.longitude == 0) {
				map.moveCamera(CameraUpdateFactory.newLatLng(destination));
			} else {
				// origin
				dialog = ProgressDialog.show(this, "Loading Route",
						"Mendapatkan Route...");

				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				builder.include(origin);
				builder.include(destination);
				bound = builder.build();

				map.moveCamera(CameraUpdateFactory.newLatLng(origin));
				getRoute(origin, destination);
			}
			map.animateCamera(CameraUpdateFactory.zoomTo(12));
			map.addMarker(new MarkerOptions()
					.position(destination)
					.title(judul)
					.snippet(isi)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.candi)));
		}

		// listener
		map.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				if (bound != null) {
					CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
							bound, 100);
					map.moveCamera(cu);
					map.animateCamera(cu);
				}
			}
		});
	}

	public boolean serviceOK() {
		int isAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		if (isAvailable == ConnectionResult.SUCCESS) {
			return true;
		} else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
					this, GPS_ERRORDIALOG_REQUEST);
			dialog.show();
		} else {
			Toast.makeText(this, "Can't connect google play service",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	public void getRoute(LatLng orig, LatLng dest) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put("origin", orig.latitude + "," + orig.longitude);
		params.put("destination", dest.latitude + "," + dest.longitude);
		params.put("sensor", "false");
		client.get("http://maps.googleapis.com/maps/api/directions/json",
				params, new JsonHttpResponseHandler() {
					private List<List<HashMap<String, String>>> routes = null;

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						JSONObject jsonData = response;

						try {
							String jarak = jsonData.getJSONObject("routes")
									.getJSONObject("legs")
									.getJSONObject("distance")
									.getString("text");
							String waktu = jsonData.getJSONObject("routes")
									.getJSONObject("legs")
									.getJSONObject("duration")
									.getString("text");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						DirectionJSONParser parser = new DirectionJSONParser();
						routes = parser.parse(jsonData);

						drawmap(routes);
						dialog.dismiss();
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						dialog.dismiss();
						Toast.makeText(Rute.this, throwable.toString(),
								Toast.LENGTH_LONG).show();
					}
				});
	}

	public void drawmap(List<List<HashMap<String, String>>> result) {
		points = null;
		PolylineOptions lineOptions = null;

		// Traversing through all the routes
		for (int i = 0; i < result.size(); i++) {
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();

			// Fetching i-th route
			List<HashMap<String, String>> path = result.get(i);

			// Fetching all the points in i-th route
			for (int j = 0; j < path.size(); j++) {
				HashMap<String, String> point = path.get(j);

				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);

				points.add(position);
			}

			// Adding all the points in the route to LineOptions
			lineOptions.addAll(points);
			lineOptions.width(5);
			lineOptions.color(Color.BLUE);
		}

		if (result.size() < 1) {
			Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Drawing polyline in the Google Map for the i-th route
		map.addPolyline(lineOptions);
	}
}

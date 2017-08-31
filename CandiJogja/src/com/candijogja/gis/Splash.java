package com.candijogja.gis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Splash extends Activity {
	int waktuLoading;
	TextView txtJudul, txtLoading;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.splash);

		// init
		DataAdapter data = new DataAdapter(this);
		GPSTracker gps = new GPSTracker(this);
		gps.getLocation();
		
		txtJudul = (TextView) findViewById(R.id.txtJudul);
		//txtLoading = (TextView) findViewById(R.id.txtLoading);

		Thread timer = new Thread() {
			@Override
			public void run() {
				try {
					sleep(3000);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					Intent i = new Intent(Splash.this, MenuUtama.class);
					startActivity(i);
					finish();
				}
			}
		};
		timer.start();
	}
}

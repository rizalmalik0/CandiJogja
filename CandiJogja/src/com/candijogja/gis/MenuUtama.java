package com.candijogja.gis;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class MenuUtama extends TabActivity {
	TabHost th;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.menu_utama);
		
		// init
		th = (TabHost) findViewById(android.R.id.tabhost);

		TabSpec tab1 = th.newTabSpec("ListCandi");
		TabSpec tab2 = th.newTabSpec("Pencarian");
		TabSpec tab3 = th.newTabSpec("Bantuan");

		tab1.setIndicator("List Candi");
		tab1.setContent(new Intent(this, ListCandi.class));

		tab2.setIndicator("Pencarian");
		tab2.setContent(new Intent(this, Pencarian.class));

		tab3.setIndicator("Bantuan");
		tab3.setContent(new Intent(this, Bantuan.class));

		th.addTab(tab1);
		th.addTab(tab2);
		th.addTab(tab3);
		
		//Tab Widget
		TabWidget tw = (TabWidget)th.findViewById(android.R.id.tabs);
	    for (int i = 0; i < tw.getChildCount(); ++i)
	    {
	        View tabView = tw.getChildTabViewAt(i);
	        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
	        tv.setTextSize(10);
	    }
	}
}

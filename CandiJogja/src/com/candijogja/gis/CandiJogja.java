package com.candijogja.gis;

import android.app.Application;

public final class CandiJogja extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		
        FontsOverride.setDefaultFont(this, "MONOSPACE", "bankgthl.ttf");
	}
}

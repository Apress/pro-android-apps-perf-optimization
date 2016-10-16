package com.apress.proandroid;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

public class APressApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
/*
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectCustomSlowCalls() // use StrictMode.noteSlowCode
			//.detectDiskReads()
			//.detectDiskWrites()
			.detectNetwork()
			.penaltyLog()
			.penaltyDeath()
			.build());

			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects()
			.penaltyLog()
			.penaltyDeath()
			.build());
		}
		*/
	}
	

}

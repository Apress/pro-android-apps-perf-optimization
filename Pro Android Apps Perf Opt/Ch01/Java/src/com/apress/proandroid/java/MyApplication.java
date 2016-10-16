package com.apress.proandroid.java;

import java.lang.reflect.Method;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.detectCustomSlowCalls() // use StrictMode.noteSlowCode
			//.detectDiskReads()
			//.detectDiskWrites()
			.detectNetwork()
			.penaltyLog()
			.penaltyDeath()
			.build());

			try {
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.setClassInstanceLimit(Class.forName("com.apress.proandroid.java.MyClass"), 10)
				.penaltyLog()
				.penaltyDeath()
				.build());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		try {
			builder.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath();
			//Class c = Class.forName("android.os.StrictMode.VmPolicy.Builder");
			Class c = builder.getClass();
			Class[] types = new Class[2];
			types[0] = Class.class;
			types[1] = int.class;
			Method m = c.getMethod("setClassInstanceLimit", types);
			m.invoke(builder, Class.forName("com.apress.proandroid.java.MyClass"), 10);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StrictMode.setVmPolicy(builder.build());
		}
	}
	

}

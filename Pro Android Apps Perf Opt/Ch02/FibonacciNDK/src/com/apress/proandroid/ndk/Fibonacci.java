package com.apress.proandroid.ndk;

import android.util.Log;

public class Fibonacci {
	private static final boolean useNative;
	static {
		boolean success;
		try {
			long time = System.currentTimeMillis();
			System.loadLibrary("fibonacci");
			success = true;
			time = System.currentTimeMillis() - time;
			Log.i("TOTO", String.valueOf(time));
		} catch (Throwable e) {
			success = false;
		}
		useNative = success;
	}
	
	public static long recursive (int n) {
		if (useNative) return recursiveNative(n);
		return recursiveJava(n);
	}
	
	public static long recursiveJava (int n) {
		if (n > 1) return recursiveJava(n-2)+recursiveJava(n-1);
		return n;
	}
	
	// javah -classpath bin/classes -jni -d jni com.apress.proandroid.ndk.Fibonacci
	public static native long recursiveNative (int n);
}

package com.apress.proandroid.assembly;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AssemblyActivity extends Activity {
	private static final String TAG = "Assembly";
	
	private static final int LOOPS_FOR_VERIFICATION = 1000000;
	private static final int LOOPS_FOR_BENCHMARK    = 200000;
	
	{
		System.loadLibrary("chapter3");
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClickButton(View v) {    	
    	try {
    		verifyGCD(10, 100);
    		verifyColorConversion(0xffffffff);
    		verifyAverage8(10, 100);
    		verifyGCD();
    		verifyColorConversion();
    		verifyAverage8();
    	} catch (Exception e) {
    		System.out.println(e.toString());
    		Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    	}
    	
    	int loops = LOOPS_FOR_BENCHMARK;
    	long t1, t2;
    	
    	t1 = System.nanoTime();
        runNativeGCDTests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeGCDTests: " + (t2 - t1));
    	
    	t1 = System.nanoTime();
        runNativeGCDAsmTests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeGCDAsmTests: " + (t2 - t1));
    	
    	t1 = System.nanoTime();
        runNativeARGB8888Tests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeARGB8888Tests: " + (t2 - t1));
    	
    	t1 = System.nanoTime();
        runNativeARGB8888AsmTests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeARGB8888AsmTests: " + (t2 - t1));
    	
    	t1 = System.nanoTime();
        runNativeAverage8Tests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeAverage8Tests: " + (t2 - t1));
    	
    	t1 = System.nanoTime();
        runNativeAverage8AsmTests(loops);
    	t2 = System.nanoTime();
    	Log.i(TAG, "runNativeAverage8AsmTests: " + (t2 - t1));
        
        Toast.makeText(v.getContext(), "Done", Toast.LENGTH_LONG).show();
    }
    
    private static void verifyGCD(int a, int b) throws Exception {
    	int gcd1 = getGCD(a, b);
    	int gcd2 = getGCDAsm(a, b);
    	//Log.d(TAG, "GCD: " + gcd1 + " " + gcd2);
    	if (gcd1 != gcd2) {
    		throw new Exception(String.format("gcd(%d,%d) = %d vs %d", a, b, gcd1, gcd2));
    	}
    }
    
    private static void verifyGCD() throws Exception {
    	Random r = new Random();
    	for (int i = 0; i < LOOPS_FOR_VERIFICATION; i++) {
    		verifyGCD(r.nextInt() & 0x7FFFFFFF, r.nextInt() & 0x7FFFFFFF);
    	}
    }
    
    private static void verifyColorConversion(int color) throws Exception {
    	int c1 = convertARGB8888(color);
    	int c2 = convertARGB8888Asm(color);
    	//Log.d(TAG, "Color: " + c1 + " " + c2);
    	if (c1 != c2) {
    		throw new Exception(String.format("colorconv(%d) = %d vs %d", color, c1, c2));
    	}
    }
    
    private static void verifyColorConversion() throws Exception {
    	Random r = new Random();
    	for (int i = 0; i < LOOPS_FOR_VERIFICATION; i++) {
    		verifyColorConversion(r.nextInt());
    	}
    }
    
    private static void verifyAverage8(int a, int b) throws Exception {
    	int avg1 = computeAverage8(a, b);
    	int avg2 = computeAverage8Faster(a, b);
    	int avg3 = computeAverage8Fastest(a, b);
    	int avg4 = computeAverage8Asm(a, b);
    	//Log.d(TAG, "Average8: " + avg1 + " " + avg2 + " " + avg3 + " " + avg4);
    	if (! (avg1 == avg2 && avg2 == avg3 && avg3 == avg4)) {
    		throw new Exception(String.format("avg8(%d) = %d vs %d", a, b, avg1, avg2, avg3, avg4));
    	}
    }
    
    private static void verifyAverage8() throws Exception {
    	Random r = new Random();
    	for (int i = 0; i < LOOPS_FOR_VERIFICATION; i++) {
    		verifyAverage8(r.nextInt() & 0x7FFFFFFF, r.nextInt() & 0x7FFFFFFF);
    	}
    }
    
    /* GCD */
    private static native int getGCD(int a, int b);
    private static native int getGCDAsm(int a, int b);
    
    /* ARGB8888 to RGB565 conversion */
    private static native int convertARGB8888(int color);
    private static native int convertARGB8888Asm(int color);
    
    /* Average8 */
    private static native int computeAverage8(int a, int b);
    private static native int computeAverage8Faster(int a, int b);
    private static native int computeAverage8Fastest(int a, int b);
    private static native int computeAverage8Asm(int a, int b);

    /* Tests */
    private static native void runNativeGCDTests(int loops);
    private static native void runNativeGCDAsmTests(int loops);
    private static native void runNativeARGB8888Tests(int loops);
    private static native void runNativeARGB8888AsmTests(int loops);
    private static native void runNativeAverage8Tests(int loops);
    private static native void runNativeAverage8AsmTests(int loops);
}
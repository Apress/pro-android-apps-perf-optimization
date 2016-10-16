package com.apress.proandroid.ndk;

import com.apress.proandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class FibonacciNDKActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClick (View v) {
    	long time = System.currentTimeMillis();
    	long f = Fibonacci.recursive(30);
    	time = System.currentTimeMillis() - time;
    	Log.i("Fibonacci", String.valueOf(f)+" - time: " + time);
    }
    
    public void onClick2 (View v) {
    	long time = System.currentTimeMillis();
    	long f = Fibonacci2.recursive(30);
    	f += Fibonacci2.iterativeFaster(30);
    	time = System.currentTimeMillis() - time;
    	Log.i("Fibonacci2", String.valueOf(f)+" - time: " + time);
    }
    
    public void onClick3 (View v) {
    	for (int n = 0; n <= 92; n++) {
    		int x = 0;
    		long time = System.currentTimeMillis();
    		for (int i = 0; i < 100000; i++) {
    			Fibonacci2.iterativeFaster(n);
    			//iterativeFaster(n);
    			x += i;
    		}
    		time = System.currentTimeMillis() - time;
    		Log.i("Benchmarks", String.valueOf(n) + " >> Total time: " + time + " milliseconds");
    	}
    }
    
	private long iterativeFaster (int n) {
		if (n > 1) {
			long a, b = 1;
			n--;
			a = n & 1;
			n /= 2;
			while (n-- > 0) {
				a += b;
				b += a;
			}
			return b;
		}
		return n;
	}
}
package com.apress.proandroid.ndk;

public final class FibonacciNative implements FibonacciInterface {
	static {
		// change name of library here to simulate link error and use Java implementation
		System.loadLibrary("fibonacci");
	}
	
    public native long recursive (int n);
    
    public native long iterativeFaster (int n);
}

package com.apress.proandroid.ndk;


public class Fibonacci2 {
	private static final FibonacciInterface fibStrategy;
	static {
		FibonacciInterface fib;
		try {
			fib = new FibonacciNative();
		} catch (Throwable e) {
			fib = new FibonacciJava();
		}
		fibStrategy = fib;
	}
	
	public static long recursive (int n) {
		return fibStrategy.recursive(n);
	}
	
	public static long iterativeFaster (int n) {
		return fibStrategy.iterativeFaster(n);
	}
}

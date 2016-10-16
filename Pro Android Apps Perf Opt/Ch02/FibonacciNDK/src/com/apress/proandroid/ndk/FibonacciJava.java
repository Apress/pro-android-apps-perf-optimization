package com.apress.proandroid.ndk;

public final class FibonacciJava implements FibonacciInterface {

	public long recursive(int n) {
		if (n > 1) return recursive(n-2)+recursive(n-1);
		return n;
	}

	public long iterativeFaster (int n) {
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

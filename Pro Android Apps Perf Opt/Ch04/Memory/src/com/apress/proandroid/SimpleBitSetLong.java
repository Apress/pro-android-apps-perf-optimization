package com.apress.proandroid;

public class SimpleBitSetLong {
	private long[] array;
	
	SimpleBitSetLong(int nBits) {
		array = new long[(nBits + 63) / 64];
	}
	
	void set(int index, boolean value) {
		int i = index / 64;
		int o = index % 64;
		if (value) {
			array[i] |= 1L << o;
		} else {
			array[i] &= ~(1L << o);
		}
	}
	
	boolean get(int index) {
		int i = index / 64;
		int o = index % 64;
		return 0 != (array[i] & (1L << o));
	}}

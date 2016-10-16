package com.apress.proandroid;

public class SimpleBitSet {
	private static final int SIZEOF_INT = 32;
	private static final int OFFSET_MASK = SIZEOF_INT - 1;
	
	private int[] bits;
	
	SimpleBitSet(int nBits) {
		bits = new int[(nBits + SIZEOF_INT - 1) / SIZEOF_INT];
	}
	
	void set(int index, boolean value) {
		int i = index / SIZEOF_INT;
		int o = index & OFFSET_MASK;
		if (value) {
			bits[i] |= 1 << o;
		} else {
			bits[i] &= ~(1 << o);
		}
	}
	
	boolean get(int index) {
		int i = index / SIZEOF_INT;
		int o = index & OFFSET_MASK;
		return 0 != (bits[i] & (1 << o));
	}
}

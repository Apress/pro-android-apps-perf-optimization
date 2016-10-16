package com.apress.proandroid;

import java.util.Arrays;
import java.util.BitSet;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;

public class MemoryActivity extends Activity {
	private static final String TAG = "MemoryActivity";
	
	private static final int ARRAY_SIZE = 1000000;
	
	boolean[] booleanArray;
	byte[] byteArray;
	char[] charArray;
	short[] shortArray;
	int[] intArray;
	long[] longArray;
	float[] floatArray;
	double[] doubleArray;
	
	private void showMemoryInfo() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memInfo);
		Log.i(TAG, String.format("Available:%d, Low memory threshold: %d%s", memInfo.availMem, memInfo.threshold, memInfo.lowMemory?" (low memory)":""));
		
		Debug.MemoryInfo debugMemInfo = new Debug.MemoryInfo();
	    Debug.getMemoryInfo(debugMemInfo);
	    // use information from debugMemInfo here
	}
	
	private void allocate() {
		showMemoryInfo();
		booleanArray = new boolean[100];
        byteArray = new byte[100];
        charArray = new char[100];
        shortArray = new short[100];
        intArray = new int[100];
        longArray = new long[100];
        floatArray = new float[100];
        doubleArray = new double[100];
		showMemoryInfo();
	}
	
	private static short[] randomize (short array[]) {
		for (int i = 0; i < array.length; i++) {
			array[i] = (short) (1000000 * Math.random());
		}
		return array;
	}
	
	private static int[] randomize (int array[]) {
		for (int i = 0; i < array.length; i++) {
			array[i] = (int) (1000000 * Math.random());
		}
		return array;
	}
	
	private static long[] randomize (long array[]) {
		for (int i = 0; i < array.length; i++) {
			array[i] = (long) (1000000 * Math.random());
		}
		return array;
	}
	
	private static float[] randomize (float array[]) {
		for (int i = 0; i < array.length; i++) {
			array[i] = (float) (1000000 * Math.random());
		}
		return array;
	}
	
	private static double[] randomize (double array[]) {
		for (int i = 0; i < array.length; i++) {
			array[i] = 1000000 * Math.random();
		}
		return array;
	}
	
	private static void sort (short array[]) {
		Arrays.sort(array);
	}
	
	private static void sort (int array[]) {
		Arrays.sort(array);
	}
	
	private static void sort (long array[]) {
		Arrays.sort(array);
	}
	
	private static void sort (float array[]) {
		Arrays.sort(array);
	}
	
	private static void sort (double array[]) {
		Arrays.sort(array);
	}
	
	private static short findMin (short array[]) {
		short min = Short.MAX_VALUE;
		for (short e: array) {
			if (e < min) min = e;
		}
		return min;
	}
	
	private static int findMin (int array[]) {
		int min = Integer.MAX_VALUE;
		for (int e: array) {
			if (e < min) min = e;
		}
		return min;
	}
	
	private static long findMin (long array[]) {
		long min = Long.MAX_VALUE;
		for (long e :array) {
			if (e < min) min = e;
		}
		return min;
	}
	
	private static float findMin (float array[]) {
		float min = Float.MAX_VALUE;
		for (float e :array) {
			if (e < min) min = e;
		}
		return min;
	}
	
	private static double findMin (double array[]) {
		double min = Double.MAX_VALUE;
		for (double e :array) {
			if (e < min) min = e;
		}
		return min;
	}
	
	private static short addAll (short array[]) {
		short sum = 0;
		for (int e :array) {
			sum += e;
		}
		return sum;
	}
	
	private static int addAll (int array[]) {
		int sum = 0;
		for (int e :array) {
			sum += e;
		}
		return sum;
	}
	
	private static long addAll (long array[]) {
		long sum = 0;
		for (long e :array) {
			sum += e;
		}
		return sum;
	}
	
	private static float addAll (float array[]) {
		float sum = 0;
		for (float e :array) {
			sum += e;
		}
		return sum;
	}
	
	private static double addAll (double array[]) {
		double sum = 0;
		for (double e :array) {
			sum += e;
		}
		return sum;
	}
	
	private static void testSort() {
		{
			short[] a = randomize(new short[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			sort(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sorting (short): " + time);
			a = null;
		}
    	System.gc();
		{
			int[] a = randomize(new int[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			sort(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sorting (int): " + time);
			a = null;
		}
    	System.gc();
		{
			long[] a = randomize(new long[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			sort(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sorting (long): " + time);
			a = null;
		}
    	System.gc();
		{
			float[] a = randomize(new float[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			sort(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sorting (float): " + time);
			a = null;
		}
    	System.gc();
		{
			double[] a = randomize(new double[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			sort(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sorting (double): " + time);
			a = null;
		}
	}
	
	private static void testFindMin() {
		{
			short[] a = randomize(new short[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			short min = findMin(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Min of all short: " + min + " in " + time);
			a = null;
		}
    	System.gc();
		{
			int[] a = randomize(new int[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			int min = findMin(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Min of all int: " + min + " in " + time);
			a = null;
		}
    	System.gc();
		{
			long[] a = randomize(new long[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			long min = findMin(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Min of all long: " + min + " in " + time);
			a = null;
		}
    	System.gc();
		{
			float[] a = randomize(new float[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			float min = findMin(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Min of all float: " + min + " in " + time);
			a = null;
		}
    	System.gc();
		{
			double[] a = randomize(new double[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			double min = findMin(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Min of all double: " + min + " in " + time);
			a = null;
		}
	}
	
	private static void testSum() {
		{
			short[] a = randomize(new short[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			short sum = addAll(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sum of all short: " + sum + " in " + time);
			a = null;
		}
    	System.gc();
		{
			int[] a = randomize(new int[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			int sum = addAll(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sum of all int: " + sum + " in " + time);
			a = null;
		}
    	System.gc();
		{
			long[] a = randomize(new long[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			long sum = addAll(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sum of all long: " + sum + " in " + time);
			a = null;
		}
    	System.gc();
		{
			float[] a = randomize(new float[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			float sum = addAll(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sum of all float: " + sum + " in " + time);
			a = null;
		}
    	System.gc();
		{
			double[] a = randomize(new double[ARRAY_SIZE]);
			long time = System.currentTimeMillis();
			double sum = addAll(a);
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "Sum of all double: " + sum + " in " + time);
			a = null;
		}
	}
	
	private static void compareBitSet() {
		{
			BitSet bs = new BitSet(ARRAY_SIZE);
			for (int i = 0; i < ARRAY_SIZE; i++) {
				bs.set(i, (Integer.numberOfTrailingZeros(i) % 2) == 0);
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				sum += bs.get(i) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "BitSet: " + time);
			bs = null;
		}
		System.gc();
		{
			MyBitSet bs = new MyBitSet(ARRAY_SIZE);
			for (int i = 0; i < ARRAY_SIZE; i++) {
				bs.set(i, (Integer.numberOfTrailingZeros(i) % 2) == 0);
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				sum += bs.get(i) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "MyBitSet: " + time);
			bs = null;
		}
		System.gc();
		{
			SimpleBitSet bs = new SimpleBitSet(ARRAY_SIZE);
			for (int i = 0; i < ARRAY_SIZE; i++) {
				bs.set(i, (Integer.numberOfTrailingZeros(i) % 2) == 0);
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				sum += bs.get(i) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "SimpleBitSet: " + time);
			bs = null;
		}
		System.gc();
		{
			SimpleBitSetLong bs = new SimpleBitSetLong(ARRAY_SIZE);
			for (int i = 0; i < ARRAY_SIZE; i++) {
				bs.set(i, (Integer.numberOfTrailingZeros(i) % 2) == 0);
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				sum += bs.get(i) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "SimpleBitSetLong: " + time);
			bs = null;
		}
		System.gc();
		{
			int[] bs = new int[(ARRAY_SIZE + 31) / 32];
			for (int i = 0; i < ARRAY_SIZE; i++) {
				int index = i / 32;
				int offset = i % 32;
				bs[index] |= (Integer.numberOfTrailingZeros(i) % 2) << offset;
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				int index = i / 32;
				int offset = i % 32;
				sum += ((bs[index] & (1 << offset)) != 0) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "MyBitSet as a simple array: " + time);
			bs = null;
		}
		System.gc();
		{
			long[] bs = new long[(ARRAY_SIZE + 63) / 64];
			for (int i = 0; i < ARRAY_SIZE; i++) {
				int index = i / 64;
				int offset = i % 64;
				bs[index] |= (Integer.numberOfTrailingZeros(i) % 2) << offset;
			}
			int sum = 0;
			long time = System.currentTimeMillis();
			for (int i = 0; i < ARRAY_SIZE; i++) {
				int index = i / 64;
				int offset = i % 64;
				sum += ((bs[index] & (1 << offset)) != 0) ? 1 : 0;
			}
			time = System.currentTimeMillis() - time;
			Log.i(TAG, "MyBitSet as a simple array of long: " + time);
			bs = null;
		}
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClickAllocate (View v) {
    	allocate();
    }
    
    public void onClickTest (View v) {
    	compareBitSet();
    	System.gc();
    	testSort();
    	System.gc();
    	testFindMin();
    	System.gc();
    	testSum();
    	System.gc();
    }
}
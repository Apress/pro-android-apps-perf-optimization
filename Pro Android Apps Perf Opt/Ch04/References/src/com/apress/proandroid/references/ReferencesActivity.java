package com.apress.proandroid.references;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ReferencesActivity extends Activity {
	private static final String TAG = "ReferenceActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    private Integer strongRef;
    private SoftReference<Integer> softRef;
    private WeakReference<Integer> weakRef;
    private PhantomReference<Integer> phantomRef;
    private ReferenceQueue<Integer> softRefQueue = new ReferenceQueue<Integer>();
    private ReferenceQueue<Integer> weakRefQueue = new ReferenceQueue<Integer>();
    private ReferenceQueue<Integer> phantomRefQueue = new ReferenceQueue<Integer>();
    
    public void onClickReset (View v) {
    	Log.i(TAG, "Reset");
    	strongRef = new Integer(1);
    	softRef = new SoftReference<Integer>(strongRef, softRefQueue);
    	weakRef = new WeakReference<Integer>(strongRef, weakRefQueue);
    	phantomRef = new PhantomReference<Integer>(strongRef, phantomRefQueue);
    }
    
    public void onClickClearStrong (View v) {
    	Log.i(TAG, "Clear strong");
    	strongRef = null;
    }
    
    public void onClickClearSoft (View v) {
    	Log.i(TAG, "Clear soft");
    	softRef = null;
    }
    
    public void onClickClearWeak (View v) {
    	Log.i(TAG, "Clear weak");
    	weakRef = null;
    }
    
    public void onClickClearPhantom (View v) {
    	Log.i(TAG, "Nothing happening");
    	//phantomRef = null;
    }
    
    public void onClickPoll (View v) {
    	Reference<? extends Integer> r;
    	Log.i(TAG, "" + softRef + " " + weakRef + " " + phantomRef);
    	if ((r = softRefQueue.poll()) != null) {
    		do {
    			Log.i(TAG, "Soft reference: " + r);
    		} while ((r = softRefQueue.poll()) != null);
    	} else {
    		Log.i(TAG, "Soft reference queue empty");
    	}
    	if ((r = weakRefQueue.poll()) != null) {
    		do {
    			Log.i(TAG, "Weak reference: " + r);
    		} while ((r = weakRefQueue.poll()) != null);
    	} else {
    		Log.i(TAG, "Weak reference queue empty");
    	}
    	if ((r = phantomRefQueue.poll()) != null) {
    		do {
    			Log.i(TAG, "Phantom reference: " + r);
    		} while ((r = phantomRefQueue.poll()) != null);
    	} else {
    		Log.i(TAG, "Phantom reference queue empty");
    	}
    }
    
    public void onClickGarbageCollection (View v) {
    	Log.i(TAG, "Garbage collection");
    	System.gc();
    }
}
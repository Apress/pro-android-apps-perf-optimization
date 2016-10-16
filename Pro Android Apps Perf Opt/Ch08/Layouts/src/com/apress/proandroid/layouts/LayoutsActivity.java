package com.apress.proandroid.layouts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

public class LayoutsActivity extends Activity {
	private static final String TAG = "LayoutsActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        int layoutResID = R.layout.frame;
        
        // we just verify inflating all the layouts works fine
        setContentView(R.layout.all);
        setContentView(R.layout.frame);
        setContentView(R.layout.linear);
        setContentView(R.layout.main);
        setContentView(R.layout.merge);
        setContentView(R.layout.mylayout);
        setContentView(R.layout.nested);
        setContentView(R.layout.relative);
        
        // final layout (the one you will actually see)
        setContentView(layoutResID);
        
        inflateStub(R.id.myid, true);
    }
    
    private View inflateStub(int id, boolean useSetVisibility) {
    	View view = null;
    	if (useSetVisibility) {
    		// to test ViewStub (note: not all layouts include the stub, so check if v is null)
            View v = findViewById(id);
            if (v != null) {
            	Log.d(TAG, "Parent of v? " + v.getParent().toString());
            	v.setVisibility(View.VISIBLE);
            	Log.d(TAG, "Parent of v? " + v.getParent());
            	view = findViewById(id);
            }
    	} else {
    		ViewStub stub = (ViewStub) findViewById(id);
    		if (stub != null) {
    			view = stub.inflate();
    		}
    	}
    	return view;
    }
}
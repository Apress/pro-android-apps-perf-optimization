package com.apress.proandroid.ch9;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;

public class Chapter9Activity extends Activity {
	static final String TAG = "Chapter9Activity";
	
	private HelloRenderingView mHelloRenderingView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        HelloWorldRenderScript();
        
        mHelloRenderingView = new HelloRenderingView(this);
        setContentView(mHelloRenderingView);
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		mHelloRenderingView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHelloRenderingView.resume();
	}
	
    private void HelloWorldRenderScript() {
    	RenderScript rs = RenderScript.create(this); // needs a Context as parameter
    	
    	// script created using the helloworld bitcode in res/raw/helloworld.bc
        ScriptC_helloworld helloworldScript = new ScriptC_helloworld(rs, getResources(), R.raw.helloworld);
        
        // optional: set a message handler
        rs.setMessageHandler(new RenderScript.RSMessageHandler() {

			@Override
			public void run() {
				super.run();
				Log.d(TAG, String.valueOf(this.mID) + " " + mData + ", length:" + mLength);
				if (mData != null) {
					for (int i = 0;  i < mLength/4; i++) {
						Log.d(TAG, String.valueOf(i) + ": " + mData[i]);
					}
				}
			}
        });
        
        helloworldScript.invoke_hello_world();
    }
}

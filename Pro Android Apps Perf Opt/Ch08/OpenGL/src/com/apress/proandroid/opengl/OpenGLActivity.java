package com.apress.proandroid.opengl;

import android.app.Activity;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;

public class OpenGLActivity extends Activity {
	private static final String TAG = "OpenGLActivity";
	
	private MyGLES20View mGLView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
		String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d(TAG, "Extensions: " + extensions);

		mGLView = new MyGLES20View(this);
		setContentView(mGLView);
		
		// won't show anything (no OpenGL ES 2.0 context yet - see logcat)
		extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d(TAG, "Extensions: " + extensions);
    }

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}
}
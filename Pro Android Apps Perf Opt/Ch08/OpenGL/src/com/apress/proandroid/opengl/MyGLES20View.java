package com.apress.proandroid.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class MyGLES20View extends GLSurfaceView {
	private static final String TAG = "MyGLES20View";
	
	private MyGLES20Renderer mRenderer;
	
	public MyGLES20View(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		
		// won't show anything
		String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d(TAG, "Extensions: " + extensions);

		mRenderer = new MyGLES20Renderer();
		setRenderer(mRenderer);

		// won't show anything either
		extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d(TAG, "Extensions: " + extensions);
	}

}

package com.apress.proandroid.opengl;

import java.nio.IntBuffer;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MyGLES20Renderer implements Renderer {
	private static final String TAG = "MyGLES20Renderer";
	
	@Override
	public void onDrawFrame(GL10 arg0) {

	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {

	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
		Log.d(TAG, "Extensions: " + extensions);

		IntBuffer ib = IntBuffer.allocate(1);
		GLES20.glGetIntegerv(GLES20.GL_NUM_COMPRESSED_TEXTURE_FORMATS, ib);
		int[] formats = new int[ib.get(0)];
		GLES20.glGetIntegerv(GLES20.GL_COMPRESSED_TEXTURE_FORMATS, formats, 0);
		Log.d(TAG, "Formats: " + Arrays.toString(formats));
	}

}

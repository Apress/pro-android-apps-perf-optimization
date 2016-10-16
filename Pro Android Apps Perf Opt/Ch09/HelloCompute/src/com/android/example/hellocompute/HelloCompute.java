/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.hellocompute;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.ImageView;

public class HelloCompute extends Activity {
	private static final String TAG = "HelloCompute";
	
    private Bitmap mBitmapIn;
    private Bitmap mBitmapOutRS;
    private Bitmap mBitmapOutJava;

    private RenderScript mRS;
    private Allocation mInAllocation;
    private Allocation mOutAllocation;
    private ScriptC_mono mScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBitmapIn = loadBitmap(R.drawable.data);
        int w = mBitmapIn.getWidth();
        int h = mBitmapIn.getHeight();
        mBitmapOutRS = Bitmap.createBitmap(w, h, mBitmapIn.getConfig());
        mBitmapOutJava = Bitmap.createBitmap(w, h, mBitmapIn.getConfig());

        ImageView in = (ImageView) findViewById(R.id.displayin);
        in.setImageBitmap(mBitmapIn);

        ImageView outRS = (ImageView) findViewById(R.id.displayoutrs);
        outRS.setImageBitmap(mBitmapOutRS);

        ImageView outJava = (ImageView) findViewById(R.id.displayoutjava);
        outJava.setImageBitmap(mBitmapOutJava);
        
        // RenderScript
        long t1 = System.nanoTime();
        
        createScript();
        
        long t2 = System.nanoTime();
        Log.d(TAG, "RenderScript: " + (t2 - t1));
        
        // Java
        t1 = System.nanoTime();
        
        int size = w * h;
        int[] pixels = new int[size];
        mBitmapIn.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < size; i++) {
        	int c = pixels[i]; // 0xAARRGGBB
        	int r = (c >> 16) & 0xFF; // 0xRR
        	int g = (c >> 8)  & 0xFF; // 0xGG
        	int b =  c        & 0xFF; // 0xBB
        	r *= 76;
        	g *= 151;
        	b *= 29;
        	int y = (r + g + b) >> 8; // luminance
        	pixels[i] = y | (y << 8) | (y << 16) | ( c & 0xFF000000);
        }
        mBitmapOutJava.setPixels(pixels, 0, w, 0, 0, w, h);
        
        t2 = System.nanoTime();
        Log.d(TAG, "Java: " + (t2 - t1));
        
    }


    private void createScript() {
        mRS = RenderScript.create(this);

        mInAllocation = Allocation.createFromBitmap(mRS, mBitmapIn,
                                                    Allocation.MipmapControl.MIPMAP_NONE,
                                                    Allocation.USAGE_SCRIPT);
        mOutAllocation = Allocation.createTyped(mRS, mInAllocation.getType());

        mScript = new ScriptC_mono(mRS, getResources(), R.raw.mono);

        mScript.set_gIn(mInAllocation);
        mScript.set_gOut(mOutAllocation);
        mScript.set_gScript(mScript);
        mScript.invoke_filter();
        mOutAllocation.copyTo(mBitmapOutRS);
    }

    private Bitmap loadBitmap(int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(getResources(), resource, options);
    }
}

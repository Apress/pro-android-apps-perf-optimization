package com.apress.proandroid.ch9;

import android.content.Context;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.view.SurfaceHolder;

public class HelloRenderingView extends RSSurfaceView {

    public HelloRenderingView(Context context) {
        super(context);
    }

    private RenderScriptGL mRS;
    private HelloRenderingRS mRender;

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
        if (mRS == null) {
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRS = createRenderScriptGL(sc);
            mRender = new HelloRenderingRS();
            mRender.init(mRS, getResources());
        }
        mRS.setSurface(holder, w, h);
    }

    @Override
    protected void onDetachedFromWindow() {
        if(mRS != null) {
            mRS = null;
            destroyRenderScriptGL();
        }
    }
}

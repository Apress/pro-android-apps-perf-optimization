package com.apress.proandroid.ch9;

import android.content.res.Resources;
import android.renderscript.RenderScriptGL;

public class HelloRenderingRS {

    public HelloRenderingRS() {
    }

    private Resources mRes;
    private RenderScriptGL mRS;
    private ScriptC_hellorendering mScript;

    public void init(RenderScriptGL rs, Resources res) {
        mRS = rs;
        mRes = res;

        mScript = new ScriptC_hellorendering(mRS, mRes, R.raw.hellorendering);

        mRS.bindRootScript(mScript);
    }
}

package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;

/**
 * Created by android on 12/19/17.
 */

public class ActivityTriangle extends ActivitySurfaceViewBase {


    RendererTriangle mRenderer;

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererTriangle(this.getApplicationContext());
        }
        return mRenderer;
    }
}

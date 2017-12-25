package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;

/**
 * Created by android on 12/19/17.
 */

public class ActivityRectangle extends ActivitySurfaceViewBase {


    RendererRectangle mRenderer;

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererRectangle(this.getApplicationContext());
        }
        return mRenderer;
    }
}

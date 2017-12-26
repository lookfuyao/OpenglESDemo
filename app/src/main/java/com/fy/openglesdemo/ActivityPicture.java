package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by android on 12/19/17.
 */

public class ActivityPicture extends ActivitySurfaceViewBase {


    private RendererPicture mRenderer;

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererPicture(this.getApplicationContext());
        }
        return mRenderer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            mRenderer.revertFixAspect();
        }
        return super.onTouchEvent(event);
    }
}

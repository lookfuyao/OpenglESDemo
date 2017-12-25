package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by android on 12/19/17.
 */

public class ActivityPictureGesture extends ActivitySurfaceViewBase {
    private static final String TAG = "ActivityPictureGesture";

    private RendererPictureMVP mRenderer;

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mRenderer.transferX(distanceX * 0.01f);
            mRenderer.transferY(distanceY * 0.01f);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetector(this, mGestureListener);
    }

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererPictureMVP(this.getApplicationContext());
        }
        return mRenderer;
    }

}

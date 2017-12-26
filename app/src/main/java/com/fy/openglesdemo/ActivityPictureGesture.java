package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by android on 12/19/17.
 */

public class ActivityPictureGesture extends ActivitySurfaceViewBase {
    private static final String TAG = "ActivityPictureGesture";

    private RendererPictureMVP mRenderer;


    private static final int MODE_TRANSFER = 0;
    private static final int MODE_ROTATE = 1;

    private int mMode = MODE_TRANSFER;


    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(mMode == MODE_TRANSFER) {
                mRenderer.transferX(distanceX * 0.01f);
                mRenderer.transferY(distanceY * 0.01f);
            } else if (mMode == MODE_ROTATE){
                mRenderer.rotateX(distanceX);
                mRenderer.rotateY(distanceY);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if(mMode == MODE_TRANSFER){
                mMode = MODE_ROTATE;
                Toast.makeText(ActivityPictureGesture.this, "Rotate Mode", Toast.LENGTH_SHORT).show();
            } else if (mMode == MODE_ROTATE){
                mMode = MODE_TRANSFER;
                Toast.makeText(ActivityPictureGesture.this, "Transfer Mode", Toast.LENGTH_SHORT).show();
            }
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

        Toast.makeText(this, "Transfer Mode", Toast.LENGTH_SHORT).show();
    }

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererPictureMVP(this.getApplicationContext());
        }
        return mRenderer;
    }

}

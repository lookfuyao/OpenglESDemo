package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Scroller;

/**
 * Created by android on 12/19/17.
 */

public class ActivityCubeGesture extends ActivitySurfaceViewBase {
    private static final String TAG = "ActivityCubeGesture";

    private RendererCubeMVP mRenderer;
    private Handler mHandler = new Handler();

    private FlingRunnable mFlingRunnable;

    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mRenderer.rotateX(distanceX);
            mRenderer.rotateY(distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "onFling " + velocityX + velocityY);
            if(mFlingRunnable == null){
                mFlingRunnable = new FlingRunnable();
            }
            mFlingRunnable.startUsingVelocity((int)velocityX, (int)velocityY);
            return true;//super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    };

    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            Log.d(TAG, "onScale" + scale);
            mRenderer.scale(scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetector(this, mGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(this, mOnScaleGestureListener);
    }

    @Override
    GLSurfaceView.Renderer initRenderer() {
        if(null == mRenderer){
            mRenderer = new RendererCubeMVP(this.getApplicationContext());
        }
        return mRenderer;
    }


    /**
     * Responsible for fling behavior. Each frame of the fling is handled in {@link #run()}.
     * A FlingRunnable will keep re-posting itself until the fling is done.
     */
    private class FlingRunnable implements Runnable {
        /**
         * Tracks the decay of a fling scroll
         */
        private Scroller mScroller;

        /**
         * X value reported by mScroller on the previous fling
         */
        private int mLastFlingX;
        private int mLastFlingY;

        public FlingRunnable() {
            mScroller = new Scroller(ActivityCubeGesture.this);
        }

        private void startCommon() {
            // Remove any pending flings
            mHandler.removeCallbacks(this);
        }

        public void startUsingVelocity(int initialVelocityX, int initialVelocityY) {
            if (initialVelocityX == 0 && initialVelocityY == 0) return;

            startCommon();

            int initialX = initialVelocityX < 0 ? Integer.MAX_VALUE : 0;
            int initialY =  initialVelocityY < 0 ? Integer.MAX_VALUE : 0;
            mLastFlingX = initialX;
            mLastFlingY = initialY;
            mScroller.fling(initialX, initialY, initialVelocityX, initialVelocityY,
                    0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            mHandler.post(this);
        }

        public void stop() {
            mHandler.removeCallbacks(this);
        }

        private void endFling() {
            /*
             * Force the scroller's status to finished (without setting its
             * position to the end)
             */
            mScroller.forceFinished(true);
        }

        @Override
        public void run() {

            final Scroller scroller = mScroller;
            boolean more = scroller.computeScrollOffset();
            final int x = scroller.getCurrX();

            final int y = scroller.getCurrY();

            // Flip sign to convert finger direction to list items direction
            // (e.g. finger moving down means list is moving towards the top)
            int deltaX = mLastFlingX - x;
            int deltaY = mLastFlingY - y;

            mRenderer.rotateX(deltaX);
            mRenderer.rotateY(deltaY);

            if (more) {
                mLastFlingX = x;
                mHandler.post(this);
            } else {
                endFling();
            }
        }

    }
}

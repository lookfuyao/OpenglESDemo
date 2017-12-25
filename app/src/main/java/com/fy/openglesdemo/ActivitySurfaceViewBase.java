package com.fy.openglesdemo;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

abstract public class ActivitySurfaceViewBase extends AppCompatActivity {

    protected GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surfaceview);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.surface_view);
        mGLSurfaceView.setEGLContextClientVersion(2);

        mGLSurfaceView.setRenderer(initRenderer());
    }


    abstract GLSurfaceView.Renderer initRenderer();

}

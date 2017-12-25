package com.fy.openglesdemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by android on 12/19/17.
 */

public class RendererRectangle extends RendererBase {


    private int mProgramId;
    private int mPositionHandle;
    private int mMatrixHandle;
    private int mColorHandle;

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;

    private static final float[] VERTEX_DATA = {
            0.5f, 0.5f, 0f,
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
    };

    private static final short[] INDEX_DATA = {
            0, 1, 2,
            0, 2, 3
    };

    private static final float[] COLOR = {1f, 0f, 1f, 1f};

    private float[] mProjectionMatrix = new float[16];

    public RendererRectangle(Context context) {
        super(context);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ShaderUtils.readRawTextFile(mContext, R.raw.vertex_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(mContext, R.raw.fragment_shader);
        mProgramId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "uMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgramId, "uColor");

        mVertexBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX_DATA);
        mVertexBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(INDEX_DATA.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(INDEX_DATA);
        mIndexBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        float ratio = width > height ? (float) width / height : (float) height / width;
//        if (width > height) {
//            Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
//        }
        Matrix.setIdentityM(mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgramId);

        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mProjectionMatrix, 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        GLES20.glUniform4fv(mColorHandle, 1, COLOR, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_DATA.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
    }
}

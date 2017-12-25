package com.fy.openglesdemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by android on 12/19/17.
 */

public class RendererTriangle extends RendererBase {


    private int mProgramId;
    private int mPositionHandle;
    private int mMatrixHandle;
    private int mColorHandle;

    private FloatBuffer mVertexBuffer;


    //三角形顶点坐标X,Y,Z
    private static final float[] VERTEX_DATA = {
            0f, 0f, 0f,
            1f, -1f, 0f,
            1f, 1f, 0f
    };

    //归一化的颜色值RGBA（0-1）
    private static final float[] COLOR = {1f, 0f, 1f, 1f};

    //投影矩阵
    private float[] mProjectionMatrix = new float[16];

    public RendererTriangle(Context context) {
        super(context);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //加载顶点着色器和象素着色器，并进行编译、连接
        String vertexShader = ShaderUtils.readRawTextFile(mContext, R.raw.vertex_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(mContext, R.raw.fragment_shader);
        mProgramId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        //获取着色器里面的Position句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        //获取着色器里面的Matrix句柄
        mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "uMatrix");
        //获取着色器里面的Color句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgramId, "uColor");

        //准备顶点坐标参数
        mVertexBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX_DATA);
        mVertexBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //计算正交投影的区域
//        float ratio = width > height ? (float) width / height : (float) height / width;
//        if (width > height) {
//            Matrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
//        }

        //初始矩阵
        Matrix.setIdentityM(mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除深度缓存和颜色缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        //指定着色器
        GLES20.glUseProgram(mProgramId);

        //传递Matrix
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mProjectionMatrix, 0);

        //enable 顶点句柄，并传递顶点坐标参数
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        //传递颜色参数
        GLES20.glUniform4fv(mColorHandle, 1, COLOR, 0);

        //绘图
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}

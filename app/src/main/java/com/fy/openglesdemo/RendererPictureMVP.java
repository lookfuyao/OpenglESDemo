package com.fy.openglesdemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fy.openglesdemo.MatrixUtils.Camera;
import com.fy.openglesdemo.MatrixUtils.Model;
import com.fy.openglesdemo.MatrixUtils.Projection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.fy.openglesdemo.MatrixUtils.Projection.TYPE_frustumM;
import static com.fy.openglesdemo.MatrixUtils.Projection.TYPE_orthoM;

/**
 * Created by android on 12/19/17.
 */

public class RendererPictureMVP extends RendererBase {

    private int mProgramId;

    private int mTextureId;

    private int mPositionHandle;
    private int mMatrixHandle;
    private int mTextureCoordHandle;
    private int mSampleHandle;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureBuffer;
    private ShortBuffer mIndexBuffer;

    private static final float[] VERTEX_DATA = {
            1f, 1f, 0f,
            -1f, 1f, 0f,
            -1f, -1f, 0f,
            1f, -1f, 0f,
    };

    private static final float[] TEXTURE_DATA = {
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f
    };

    private static final short[] INDEX_DATA = {
            0, 1, 2,
            0, 2, 3
    };

    private Camera mCamera = new Camera();
    private Model mModel = new Model();
    private Projection mProjection = new Projection(TYPE_orthoM);

    private float[] mMVMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public RendererPictureMVP(Context context) {
        super(context);
    }

    public void rotateX(float angle) {
        mModel.setAngleX(mModel.getAngleX() + angle);
    }

    public void rotateY(float angle) {
        mModel.setAngleY(mModel.getAngleY() + angle);
    }

    public void rotateZ(float angle) {
        mModel.setAngleZ(mModel.getAngleZ() + angle);
    }

    public void transferX(float value) {
        float x = mModel.getX() - value;
        if (x < -2f) return;
        if (x > 2f) return;
        mModel.setX(x);
    }

    public void transferY(float value) {
        float x = mModel.getY() - value;
        if (x < -2f) return;
        if (x > 2f) return;
        mModel.setY(x);
    }

    private void applyMVP() {
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVMatrix, 0, mCamera.getMatrix(), 0, mModel.getMatrix(), 0);

        // This multiplies the model view matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjection.getMartix(), 0, mMVMatrix, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ShaderUtils.readRawTextFile(mContext, R.raw.vertex_shader_render_picture);
        String fragmentShader = ShaderUtils.readRawTextFile(mContext, R.raw.fragment_shader_render_picture);
        mProgramId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        mTextureId = TextureHelper.loadTexture(mContext, R.raw.warm_killer);

        mPositionHandle = GLES20.glGetAttribLocation(mProgramId, "aPosition");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgramId, "uMatrix");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgramId, "aTexCoord");
        mSampleHandle = GLES20.glGetUniformLocation(mProgramId, "uTexture");

        mVertexBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTEX_DATA);
        mVertexBuffer.position(0);

        mTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEXTURE_DATA);
        mTextureBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(INDEX_DATA.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(INDEX_DATA);
        mIndexBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = width > height ? (float) width / height : (float) height / width;
        if (width > height) {
            mProjection.setLeft(-ratio);
            mProjection.setRight(ratio);
            mProjection.setBottom(-1f);
            mProjection.setTop(1f);
            mProjection.setNear(-1000f);
            mProjection.setFar(1000f);
        } else {
            mProjection.setLeft(-1);
            mProjection.setRight(1);
            mProjection.setBottom(-ratio);
            mProjection.setTop(ratio);
            mProjection.setNear(-1000f);
            mProjection.setFar(1000f);
        }

        mCamera.setLookAtM(0, 0, -1, 0, 0, 1, 0, 1, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgramId);

        applyMVP();
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);

        GLES20.glUniform1i(mSampleHandle, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_DATA.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
    }
}

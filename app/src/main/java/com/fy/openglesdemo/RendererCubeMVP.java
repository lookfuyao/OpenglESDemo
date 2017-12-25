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

import static com.fy.openglesdemo.MatrixUtils.Projection.TYPE_perspectiveM;

/**
 * Created by android on 12/19/17.
 */

public class RendererCubeMVP extends RendererBase {

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
            // Vertex data for face 0
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,

            //data for face 1
            1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,

            //data for face 2
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            //data for face 3
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,

            //data for face 4
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,

            //data for face 5
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
    };

    private static final float[] TEXTURE_DATA = {
            0.0f, 0.0f,
            0.33f, 0.0f,
            0.0f, 0.5f,
            0.33f, 0.5f,


            0.0f, 0.5f,
            0.33f, 0.5f,
            0.0f, 1.0f,
            0.33f, 1.0f,


            0.66f, 0.5f,
            1.0f, 0.5f,
            0.66f, 1.0f,
            1.0f, 1.0f,


            0.66f, 0.0f,
            1.0f, 0.0f,
            0.66f, 0.5f,
            1.0f, 0.5f,


            0.33f, 0.0f,
            0.66f, 0.0f,
            0.33f, 0.5f,
            0.66f, 0.5f,


            0.33f, 0.5f,
            0.66f, 0.5f,
            0.33f, 1.0f,
            0.66f, 1.0f
    };

    private static final short[] INDEX_DATA = {
            0,  1,  2,  3,  3,     // Face 0 - triangle strip ( v0,  v1,  v2,  v3)
            4,  4,  5,  6,  7,  7, // Face 1 - triangle strip ( v4,  v5,  v6,  v7)
            8,  8,  9, 10, 11, 11, // Face 2 - triangle strip ( v8,  v9, v10, v11)
            12, 12, 13, 14, 15, 15, // Face 3 - triangle strip (v12, v13, v14, v15)
            16, 16, 17, 18, 19, 19, // Face 4 - triangle strip (v16, v17, v18, v19)
            20, 20, 21, 22, 23      // Face 5 - triangle strip (v20, v21, v22, v23)
    };

    private Camera mCamera = new Camera();
    private Model mModel = new Model();
    private Projection mProjection = new Projection(TYPE_perspectiveM);

    private float[] mMVMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    public RendererCubeMVP(Context context) {
        super(context);
    }


    public void scale(float scale) {
        float fov = mProjection.getFov();
        float val =  fov * scale;
        mProjection.setFov(val);
    }

    public void rotateX(float angle) {
        float val = mModel.getPitch() + angle % 360.0f;
        if(val >= 360) val = 0;
        mModel.setPitch(val);
    }

    public void rotateY(float angle) {
        float val = mModel.getYaw() + angle % 360.0f;
        if(val >= 360) val = 0;
        mModel.setYaw(val);
    }

    public void rotateZ(float angle) {
        float val = mModel.getAngleZ() + angle;
        if(val >= 360) val = 0;
        mModel.setAngleZ(val);
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
        Matrix.multiplyMM(mMVPMatrix, 0, mProjection.getMatrix(), 0, mMVMatrix, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexShader = ShaderUtils.readRawTextFile(mContext, R.raw.vertex_shader_render_picture);
        String fragmentShader = ShaderUtils.readRawTextFile(mContext, R.raw.fragment_shader_render_picture);
        mProgramId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        mTextureId = TextureHelper.loadTexture(mContext, R.raw.cube);

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
        mProjection.setFov(90);
        mProjection.setAspect((float)width/(float) height);
        mProjection.setNear(1f);
        mProjection.setFar(1000f);
        mCamera.setLookAtM(0, 0, 0,  0, 0, -1,  0, 1, 0);
        mModel.setZ(-10.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

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

        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, INDEX_DATA.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);

        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }
}

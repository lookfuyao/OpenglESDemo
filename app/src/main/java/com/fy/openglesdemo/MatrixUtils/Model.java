package com.fy.openglesdemo.MatrixUtils;

import android.opengl.Matrix;

/**
 * Created by android on 8/19/17.
 */

public class Model {

    private float[] mModelMatrix = null;

    private float mX;
    private float mY;
    private float mZ;
    private float mAngleX;
    private float mAngleY;
    private float mAngleZ;
    private float mPitch; // x-axis
    private float mYaw; // y-axis
    private float mRoll; // z-axis
    private boolean changed;

    public Model() {
        mX = mY = mZ = 0;
        mAngleX = mAngleY = mAngleZ = 0;
        mPitch = mYaw = mRoll = 0;
        changed = true;
    }

    public Model(float mX, float mY, float mZ, float mAngleX, float mAngleY, float mAngleZ, float mPitch, float mYaw, float mRoll) {
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;
        this.mAngleX = mAngleX;
        this.mAngleY = mAngleY;
        this.mAngleZ = mAngleZ;
        this.mPitch = mPitch;
        this.mYaw = mYaw;
        this.mRoll = mRoll;
        changed = true;
    }

    public float getPitch() {
        return mPitch;
    }

    public Model setPitch(float pitch) {
        changed |= this.mPitch != pitch;
        this.mPitch = pitch;
        return this;
    }

    public float getYaw() {
        return mYaw;
    }

    public Model setYaw(float yaw) {
        changed |= this.mYaw != yaw;
        this.mYaw = yaw;
        return this;
    }

    public float getRoll() {
        return mRoll;
    }

    public Model setRoll(float roll) {
        changed |= this.mRoll != roll;
        this.mRoll = roll;
        return this;
    }

    public float getX() {
        return mX;
    }

    public Model setX(float x) {
        changed |= this.mX != x;
        this.mX = x;
        return this;
    }

    public float getY() {
        return mY;
    }

    public Model setY(float y) {
        changed |= this.mY != y;
        this.mY = y;
        return this;
    }

    public float getZ() {
        return mZ;
    }

    public Model setZ(float z) {
        changed |= this.mZ != z;
        this.mZ = z;
        return this;
    }

    public float getAngleX() {
        return mAngleX;
    }

    /**
     * setAngleX
     * @param angleX in degree
     * @return self
     */
    public Model setAngleX(float angleX) {
        changed |= this.mAngleX != angleX;
        this.mAngleX = angleX;
        return this;
    }

    public float getAngleY() {
        return mAngleY;
    }

    /**
     * setAngleY
     * @param angleY in degree
     * @return self
     */
    public Model setAngleY(float angleY) {
        changed |= this.mAngleY != angleY;
        this.mAngleY = angleY;
        return this;
    }

    public float getAngleZ() {
        return mAngleZ;
    }

    /**
     * setAngleZ
     * @param angleZ in degree
     * @return self
     */
    public Model setAngleZ(float angleZ) {
        changed |= this.mAngleX != angleZ;
        this.mAngleZ = angleZ;
        return this;
    }

    public static Model newInstance(){
        return new Model();
    }

    @Override
    public String toString() {
        return "Model{" +
                "mX=" + mX +
                ", mY=" + mY +
                ", mZ=" + mZ +
                ", mAngleX=" + mAngleX +
                ", mAngleY=" + mAngleY +
                ", mAngleZ=" + mAngleZ +
                ", mPitch=" + mPitch +
                ", mYaw=" + mYaw +
                ", mRoll=" + mRoll +
                '}';
    }







    private void ensure(){
        // model
        if (mModelMatrix == null){
            mModelMatrix = new float[16];
            Matrix.setIdentityM(mModelMatrix, 0);
        }

        if (!changed){
            return;
        }

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.rotateM(mModelMatrix, 0, getAngleX(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getAngleY(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getAngleZ(), 0.0f, 0.0f, 1.0f);

        Matrix.translateM(mModelMatrix, 0, getX(),getY(),getZ());

        Matrix.rotateM(mModelMatrix, 0, getYaw(), 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getPitch(), 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getRoll(), 0.0f, 0.0f, 1.0f);
        changed = false;
    }

    public float[] getMatrix(){
        ensure();
        return mModelMatrix;
    }

}









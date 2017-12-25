package com.fy.openglesdemo.MatrixUtils;

import android.opengl.Matrix;

/**
 * Created by android on 8/19/17.
 */

public class Camera {

    private float eyeX;
    private float eyeY;
    private float eyeZ;
    private float lookX;
    private float lookY;
    private float lookZ = -1f;
    private float upX;
    private float upY = 1f;
    private float upZ;
    private boolean change = true;

    private float[] mViewMatrix = null;

    public Camera() {
    }

    public Camera(float eyeX, float eyeY, float eyeZ, float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.lookX = lookX;
        this.lookY = lookY;
        this.lookZ = lookZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
    }

    public void setLookAtM(float eyeX, float eyeY, float eyeZ, float lookX, float lookY, float lookZ, float upX, float upY, float upZ) {
        this.eyeX = eyeX;
        this.eyeY = eyeY;
        this.eyeZ = eyeZ;
        this.lookX = lookX;
        this.lookY = lookY;
        this.lookZ = lookZ;
        this.upX = upX;
        this.upY = upY;
        this.upZ = upZ;
        change = true;
    }

    public float getEyeX() {
        return eyeX;
    }

    public void setEyeX(float eyeX) {
        change |= this.eyeX != eyeX;
        this.eyeX = eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public void setEyeY(float eyeY) {
        change |= this.eyeY != eyeY;
        this.eyeY = eyeY;
    }

    public float getEyeZ() {
        return eyeZ;
    }

    public void setEyeZ(float eyeZ) {
        change |= this.eyeZ != eyeZ;
        this.eyeZ = eyeZ;
    }

    public float getLookX() {
        return lookX;
    }

    public void setLookX(float lookX) {
        change |= this.lookX != lookX;
        this.lookX = lookX;
    }

    public float getLookY() {
        return lookY;
    }

    public void setLookY(float lookY) {
        change |= this.lookY != lookY;
        this.lookY = lookY;
    }

    public float getLookZ() {
        return lookZ;
    }

    public void setLookZ(float lookZ) {
        change |= this.lookZ != lookZ;
        this.lookZ = lookZ;
    }

    public float getUpX() {
        return upX;
    }

    public void setUpX(float upX) {
        change |= this.upX != upX;
        this.upX = upX;
    }

    public float getUpY() {
        return upY;
    }

    public void setUpY(float upY) {
        change |= this.upY != upY;
        this.upY = upY;
    }

    public float getUpZ() {
        return upZ;
    }

    public void setUpZ(float upZ) {
        change |= this.upZ != upZ;
        this.upZ = upZ;
    }

    public float[] getMatrix() {
        if (mViewMatrix == null) {
            mViewMatrix = new float[16];
            Matrix.setIdentityM(mViewMatrix, 0);
        }

        if (change) {
            change = false;
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        }
        return mViewMatrix;
    }

    @Override
    public String toString() {
        return "Camera{" +
                "eyeX=" + eyeX +
                ", eyeY=" + eyeY +
                ", eyeZ=" + eyeZ +
                ", lookX=" + lookX +
                ", lookY=" + lookY +
                ", lookZ=" + lookZ +
                ", upX=" + upX +
                ", upY=" + upY +
                ", upZ=" + upZ +
                '}';
    }
}

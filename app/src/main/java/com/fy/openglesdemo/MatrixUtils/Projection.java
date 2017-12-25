package com.fy.openglesdemo.MatrixUtils;

import android.opengl.Matrix;

/**
 * Created by android on 8/19/17.
 */

public class Projection {

    private float[] mMatrix = null;

    public static final int TYPE_perspectiveM = 0;
    public static final int TYPE_orthoM = 1;

    private float left;
    private float right;
    private float bottom;
    private float top;
    private float near;
    private float far;

    private float fov;
    private float aspect;

    private int mType = TYPE_perspectiveM;
    private boolean change = true;

    public Projection(int mType) {
        this.mType = mType;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        change |= this.left != left;
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        change |= this.right != right;
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        change |= this.bottom != bottom;
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        change |= this.top != top;
        this.top = top;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        change |= this.near != near;
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        change |= this.far != far;
        this.far = far;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        change |= this.fov != fov;
        this.fov = fov;
    }

    public float getAspect() {
        return aspect;
    }

    public void setAspect(float aspect) {
        change |= this.aspect != aspect;
        this.aspect = aspect;
    }

    @Override
    public String toString() {
        return "Projection{" +
                "left=" + left +
                ", right=" + right +
                ", bottom=" + bottom +
                ", top=" + top +
                ", near=" + near +
                ", far=" + far +
                ", fov=" + fov +
                ", aspect=" + aspect +
                ", mType=" + mType +
                '}';
    }

    public float[] getMatrix() {
        if (null == mMatrix) {
            mMatrix = new float[16];
        }

        if (!change) {
            return mMatrix;
        }

        change = false;
        switch (mType) {
            case TYPE_perspectiveM:
                Matrix.perspectiveM(mMatrix, 0, fov, aspect, near, far);
                break;
            case TYPE_orthoM:
                Matrix.orthoM(mMatrix, 0, left, right, bottom, top, near, far);
                break;
        }
        return mMatrix;
    }
}









package engine3d.transforms;

import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.vectors.Vec4df;

/**
 * This class represents a transformation of a mesh
 *
 * date: 08/02/2022
 */
public class Transform {

    /**
     * The matrix of the transformation
     */
    protected Mat4x4 mat;

    /**
     * vector x, y, z, t represents delta
     * Transformation vector in x y z axis
     */
    protected Vec4df d;

    /**
     * Void constructor
     */
    public Transform() {
        mat = MatrixMath.matrixMakeIdentity();
        d = new Vec4df();
    }

    /**
     * Parametrized constructor
     * @param delta vector of transformation in x, y, z axis
     */
    public Transform(Vec4df delta) {
        mat = MatrixMath.matrixMakeIdentity();
        d = delta;
    }

    /**
     * Full parametrized constructor
     * @param mat matrix
     * @param d vector
     */
    public Transform(Mat4x4 mat, Vec4df d) {
        this.mat = mat;
        this.d = d;
    }

    /**
     * Copy constructor
     * @param t instance of other transform
     */
    public Transform(Transform t) {
        this.mat = new Mat4x4(t.getMat());
        this.d = new Vec4df(t.getDelta());
    }

    /**
     * Generic method to update the transform matrix
     * by the values of delta
     * @return must returns its self
     */
    public Transform update() {
        return this;
    }

    // Combination methods

    public Mat4x4 combine(Mat4x4 mat) {
        return MatrixMath.matrixMultiplyMatrix(this.mat, mat);
    }

    public Mat4x4 combine(Transform t) {
        return MatrixMath.matrixMultiplyMatrix(this.mat, t.getMat());
    }

    // Getters and Setters

    public Mat4x4 getMat() {
        return mat;
    }

    public void setMat(Mat4x4 mat) {
        this.mat = mat;
    }

    public Vec4df getDelta() {
        return d;
    }

    public void setDelta(Vec4df d) {
        this.d = d;
    }

}

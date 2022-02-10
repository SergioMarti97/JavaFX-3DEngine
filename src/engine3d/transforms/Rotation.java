package engine3d.transforms;

import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.vectors.Vec4df;

/**
 * This class represents a rotation transformation
 * in 3 dimensions
 */
public class Rotation extends Transform {

    public Rotation() {
    }

    public Rotation(float x, float y, float z) {
        super(x, y, z);
    }

    public Rotation(Vec4df delta) {
        super(delta);
    }

    public Rotation(Mat4x4 mat, Vec4df d) {
        super(mat, d);
    }

    public Rotation(Transform t) {
        super(t);
    }

    @Override
    public Transform update() {
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(d.getX());
        matRotX = MatrixMath.matrixMultiplyMatrix(MatrixMath.matrixMakeIdentity(), matRotX);
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(d.getY());
        Mat4x4 matRotXY = MatrixMath.matrixMultiplyMatrix(matRotY, matRotX);
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(d.getZ());
        mat = MatrixMath.matrixMultiplyMatrix(matRotXY, matRotZ);
        return this;
    }

}

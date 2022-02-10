package engine3d.transforms;

import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.vectors.Vec4df;

/**
 * This class represents the a scale transformation
 */
public class Scale extends Transform {

    public Scale() {
    }

    public Scale(float x, float y, float z) {
        super(x, y, z);
    }

    public Scale(Vec4df delta) {
        super(delta);
    }

    public Scale(Mat4x4 mat, Vec4df d) {
        super(mat, d);
    }

    public Scale(Transform t) {
        super(t);
    }

    @Override
    public Transform update() {
        mat = MatrixMath.matrixMakeScale(d.getX(), d.getY(), d.getZ());
        return this;
    }

}

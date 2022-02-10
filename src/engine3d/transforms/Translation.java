package engine3d.transforms;

import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.vectors.Vec4df;

public class Translation extends Transform {

    public Translation() {
    }

    public Translation(float x, float y, float z) {
        super(x, y, z);
    }

    public Translation(Vec4df delta) {
        super(delta);
    }

    public Translation(Mat4x4 mat, Vec4df d) {
        super(mat, d);
    }

    public Translation(Transform t) {
        super(t);
    }

    @Override
    public Transform update() {
        mat = MatrixMath.matrixMakeTranslation(d.getX(), d.getY(), d.getZ());
        return this;
    }

}

package engine3d.transforms;

import engine3d.matrix.MatrixMath;

public class Translation extends Transform {

    @Override
    public Transform update() {
        mat = MatrixMath.matrixMakeTranslation(d.getX(), d.getY(), d.getZ());
        return this;
    }

}

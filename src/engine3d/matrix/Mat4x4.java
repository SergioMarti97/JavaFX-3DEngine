package engine3d.matrix;

/**
 * This class is used for transformations
 * required for the 3D engine
 *
 * @class Mat4x4
 * @author Sergio Martí Torregrosa
 * @date 2020-08-06
 */
public class Mat4x4 {

    private float[][] m;

    public Mat4x4() {
        m = new float[4][4];
    }

    public Mat4x4(float[][] m) {
        this.m = m;
    }

    public Mat4x4(Mat4x4 mat4x4) {
        this.m = mat4x4.getM();
    }

    public float[][] getM() {
        return m;
    }

    public void setM(float[][] m) {
        this.m = m;
    }

}

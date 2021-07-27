package engine3d.vectors;

/**
 * In fact I have seen another 3D engines an this object is
 * called "Quaternion".
 *
 * @class Vec3df.
 * @autor Sergio Martí Torregrosa. sMartiTo
 * @date 2020-08-05
 */
public class Vec4df {

    /**
     * The x coordinate
     */
    private float x;

    /**
     * The x coordinate
     */
    private float y;

    /**
     * The x coordinate
     */
    private float z;

    /**
     * The x coordinate
     */
    private float w;


    /**
     * The void constructor
     */
    public Vec4df() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        w = 1.0f;
    }

    /**
     * The constructor
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public Vec4df(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
    }

    /**
     * Constructor with all parameters
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param w w coordinate
     */
    public Vec4df(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Copy constructor
     * @param vec4df the instance to copy of the same object
     */
    public Vec4df(Vec4df vec4df) {
        this.x = vec4df.getX();
        this.y = vec4df.getY();
        this.z = vec4df.getZ();
        this.w = vec4df.getW();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public void set(Vec4df vec4Df) {
        this.x = vec4Df.getX();
        this.y = vec4Df.getY();
        this.z = vec4Df.getZ();
        this.w = vec4Df.getW();
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0f;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void addToX(double amount) {
        this.x += amount;
    }

    public void addToY(double amount) {
        this.y += amount;
    }

    public void addToZ(double amount) {
        this.z += amount;
    }

    public void addToW(double amount) {
        this.w += amount;
    }

    public void multiplyXBy(double amount) {
        this.x *= amount;
    }

    public void multiplyYBy(double amount) {
        this.y *= amount;
    }

    public void multiplyZBy(double amount) {
        this.z *= amount;
    }

    public void multiplyWBy(double amount) {
        this.w *= amount;
    }

    public float mag() {
        return (float)(Math.sqrt( x * x + y * y + z * z ));
    }

    public void normalize() {
        float l = mag();
        x /= l;
        y /= l;
        z /= l;
    }

    public float dotProduct(Vec4df v) {
        return this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Vec4df) {
            Vec4df vec4Df = (Vec4df) obj;
            return vec4Df.getX() == this.x && vec4Df.getY() == this.y && vec4Df.getZ() == this.z;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z + " W: " + w;
    }

}

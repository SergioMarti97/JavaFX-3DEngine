package engine3d.mesh;

import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;

/**
 * This class is a representation of a triangle in a 3d space
 * This is needed for the 3d engine.
 *
 * @class Triangle
 * @author Sergio Martí Torregrosa
 * @date 18/08/2020
 */
public class Triangle {

    /**
     * The Default color when none color is specified
     */
    private final int DEFAULT_COLOR = 0xffffffff;

    /**
     * The three points which forms the triangle.
     * They are four dimensions points (quaternions)
     */
    private Vec4df[] p = new Vec4df[3];

    /**
     * The three points which forms the texture triangle in 2d
     * They are three dimensions, the third value is needed for
     * the texture correction when the texture is projected
     */
    private Vec3df[] t = new Vec3df[3];

    private Vec4df[] n = new Vec4df[3];

    /**
     * The triangle color
     */
    private int color = DEFAULT_COLOR;

    /**
     * The brightness of the triangle
     */
    private float brightness = 1.0f;

    /**
     * The null constructor, all fields are initialise to 0
     */
    public Triangle() {
        p[0] = new Vec4df();
        p[1] = new Vec4df();
        p[2] = new Vec4df();
        t[0] = new Vec3df();
        t[1] = new Vec3df();
        t[2] = new Vec3df();
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
    }

    /**
     * Constructor
     * @param p the points of the triangle
     */
    public Triangle(Vec4df[] p) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        t[0] = new Vec3df();
        t[1] = new Vec3df();
        t[2] = new Vec3df();
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
    }

    /**
     * Constructor
     * @param p the points of the triangle
     * @param color the color of the triangle
     */
    public Triangle(Vec4df[] p, int color) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        t[0] = new Vec3df();
        t[1] = new Vec3df();
        t[2] = new Vec3df();
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
        this.color = color;
    }

    /**
     * Constructor
     * @param p the points of the triangle
     * @param t the texture points of the triangle
     */
    public Triangle(Vec4df[] p, Vec3df[] t) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        this.t[0] = new Vec3df(t[0]);
        this.t[1] = new Vec3df(t[1]);
        this.t[2] = new Vec3df(t[2]);
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
    }

    /**
     * Constructor
     * @param p the points of the triangle
     * @param t the texture points of the triangle
     * @param color the color of the triangle
     */
    public Triangle(Vec4df[] p, Vec3df[] t, int color) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        this.t[0] = new Vec3df(t[0]);
        this.t[1] = new Vec3df(t[1]);
        this.t[2] = new Vec3df(t[2]);
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
        this.color = color;
    }

    /**
     * Constructor
     * @param p the points of the triangle
     * @param t the texture points of the triangle
     * @param color the color of the triangle
     * @param brightness the brightness of the triangle
     */
    public Triangle(Vec4df[] p, Vec3df[] t, int color, float brightness) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        this.t[0] = new Vec3df(t[0]);
        this.t[1] = new Vec3df(t[1]);
        this.t[2] = new Vec3df(t[2]);
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
        this.color = color;
        this.brightness = brightness;
    }

    public Triangle(Vec4df[] p, Vec3df[] t, Vec4df[] n) {
        this.p[0] = new Vec4df(p[0]);
        this.p[1] = new Vec4df(p[1]);
        this.p[2] = new Vec4df(p[2]);
        this.t[0] = new Vec3df(t[0]);
        this.t[1] = new Vec3df(t[1]);
        this.t[2] = new Vec3df(t[2]);

        this.n[0] = new Vec4df(n[0]);
        this.n[1] = new Vec4df(n[1]);
        this.n[2] = new Vec4df(n[2]);
    }

    /**
     * The copy constructor
     * @param triangle the triangle to copy
     */
    public Triangle(Triangle triangle) {
        this.p[0] = new Vec4df(triangle.getP()[0]);
        this.p[1] = new Vec4df(triangle.getP()[1]);
        this.p[2] = new Vec4df(triangle.getP()[2]);
        this.t[0] = new Vec3df(triangle.getT()[0]);
        this.t[1] = new Vec3df(triangle.getT()[1]);
        this.t[2] = new Vec3df(triangle.getT()[2]);
        n[0] = new Vec4df();
        n[1] = new Vec4df();
        n[2] = new Vec4df();
        this.color = triangle.getColor();
        this.brightness = triangle.getBrightness();
    }

    /**
     * Getter for the points
     * @return the points of the triangle
     */
    public Vec4df[] getP() {
        return p;
    }

    /**
     * Getter for the texture points
     * @return the texture points of the triangle
     */
    public Vec3df[] getT() {
        return t;
    }

    public Vec4df[] getN() {
        return n;
    }

    /**
     * Getter for the color
     * @return the color of the triangle
     */
    public int getColor() {
        return color;
    }

    /**
     * Getter for the brightness
     * @return the brightness of the triangle
     */
    public float getBrightness() {
        return brightness;
    }

    /**
     * The setter for the points
     * @param p the new points values
     */
    public void setP(Vec4df[] p) {
        this.p[0].set(p[0]);
        this.p[1].set(p[1]);
        this.p[2].set(p[2]);
    }

    /**
     * The setter for the texture points
     * @param t the new texture points
     */
    public void setT(Vec3df[] t) {
        this.t[0].set(t[0]);
        this.t[1].set(t[1]);
        this.t[2].set(t[2]);
    }

    public void setN(Vec4df[] n) {
        this.n[0].set(n[0]);
        this.n[1].set(n[1]);
        this.n[2].set(n[2]);
    }

    /**
     * The setter for the color
     * @param color the new color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * The setter for the brightness
     * @param brightness the new brightness
     */
    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "p0 " + p[0] + " p1 " + p[1] + " p2 " + p[2] +
                " t0: " + t[0] + " t1: " + t[1] + " t2: " + t[2] +
                " color: " + color + " brightness: " + brightness;
    }

}

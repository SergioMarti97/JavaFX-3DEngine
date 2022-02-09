package engine3d.matrix;

import engine3d.mesh.Mesh;
import engine3d.mesh.Triangle;
import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;

import java.util.ArrayList;

/**
 * This class contains all the static methods
 * related with matrix operations
 *
 * @class MatrixMath
 * @author Sergio Martí Torregrosa
 * @date 18/08/2020
 */
public class MatrixMath {

    /**
     * This method transforms the triangles of a mesh
     * with the matrix passed on the parameters
     * @param m the transform
     * @param mesh the mesh
     */
    public static void transformMesh(Mat4x4 m, Mesh mesh) {
        for ( Triangle triangle : mesh.getTris() ) {
            triangle.setP(matrixMultiplyVectors(m, triangle.getP()));
        }
    }

    public static Vec4df matrixMultiplyVector(Mat4x4 m, Vec4df i) {
        Vec4df v = new Vec4df();
        v.setX(i.getX() * m.getM()[0][0] + i.getY() * m.getM()[1][0] + i.getZ() * m.getM()[2][0] + i.getW() * m.getM()[3][0]);
        v.setY(i.getX() * m.getM()[0][1] + i.getY() * m.getM()[1][1] + i.getZ() * m.getM()[2][1] + i.getW() * m.getM()[3][1]);
        v.setZ(i.getX() * m.getM()[0][2] + i.getY() * m.getM()[1][2] + i.getZ() * m.getM()[2][2] + i.getW() * m.getM()[3][2]);
        v.setW(i.getX() * m.getM()[0][3] + i.getY() * m.getM()[1][3] + i.getZ() * m.getM()[2][3] + i.getW() * m.getM()[3][3]);
        return v;
    }

    /**
     * Este método es el método general para transformar una array de puntos en 3D con
     * cualquier matriz. Por ejemplo, la matriz de visión o la matriz de proyección.
     * También se podría utilizar para transformar los triangulos con una matriz de
     * rotación o translación.
     * @param m es la matriz con la que se van a transformar los puntos.
     * @param vec4dfs son los puntos 3D que se van a transformar
     * @return devuelve un nuevo array con los puntos pasados por parámetro transformados.
     */
    public static Vec4df[] matrixMultiplyVectors(Mat4x4 m, Vec4df[] vec4dfs) {
        Vec4df[] vec4dfTransformed = new Vec4df[vec4dfs.length];
        for (int i = 0; i < vec4dfs.length; i++ ) {
            vec4dfTransformed[i] = MatrixMath.matrixMultiplyVector(m, vec4dfs[i]);
        }
        return vec4dfTransformed;
    }

    public static Mat4x4 matrixMultiplyMatrix(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 matrix = new Mat4x4();
        for ( int c = 0; c < 4; c++ ) {
            for ( int r = 0; r < 4; r++ ) {
                matrix.getM()[r][c] = m1.getM()[r][0] * m2.getM()[0][c] +
                                      m1.getM()[r][1] * m2.getM()[1][c] +
                                      m1.getM()[r][2] * m2.getM()[2][c] +
                                      m1.getM()[r][3] * m2.getM()[3][c];
            }
        }
        return matrix;
    }

    public static Mat4x4 matrixMakeIdentity() {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationX(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = (float)(Math.cos(angleRad));
        matrix.getM()[1][2] = (float)(Math.sin(angleRad));
        matrix.getM()[2][1] = - (float)(Math.sin(angleRad));
        matrix.getM()[2][2] = (float)(Math.cos(angleRad));
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationY(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = (float)(Math.cos(angleRad));
        matrix.getM()[0][2] = (float)(Math.sin(angleRad));
        matrix.getM()[2][0] = - (float)(Math.sin(angleRad));
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = (float)(Math.cos(angleRad));
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotationZ(float angleRad) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = (float)(Math.cos(angleRad));
        matrix.getM()[0][1] = (float)(Math.sin(angleRad));
        matrix.getM()[1][0] = - (float)(Math.sin(angleRad));
        matrix.getM()[1][1] = (float)(Math.cos(angleRad));
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeRotation(Vec4df rotation) {
        Mat4x4 matRotX = MatrixMath.matrixMakeRotationX(rotation.getX());
        Mat4x4 matRotY = MatrixMath.matrixMakeRotationY(rotation.getY());
        Mat4x4 matRotZ = MatrixMath.matrixMakeRotationZ(rotation.getZ());
        Mat4x4 matRotXY = MatrixMath.matrixMultiplyMatrix(matRotX, matRotY);
        return MatrixMath.matrixMultiplyMatrix(matRotZ, matRotXY);
    }

    public static Mat4x4 matrixMakeScale(float x, float y, float z) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = x;
        matrix.getM()[1][1] = y;
        matrix.getM()[2][2] = z;
        matrix.getM()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeTranslation(float x, float y, float z) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        matrix.getM()[3][0] = x;
        matrix.getM()[3][1] = y;
        matrix.getM()[3][2] = z;
        return matrix;
    }

    public static Mat4x4 matrixMakeTranslation(Vec4df vec4Df) {
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = 1.0f;
        matrix.getM()[1][1] = 1.0f;
        matrix.getM()[2][2] = 1.0f;
        matrix.getM()[3][3] = 1.0f;
        matrix.getM()[3][0] = vec4Df.getX();
        matrix.getM()[3][1] = vec4Df.getY();
        matrix.getM()[3][2] = vec4Df.getZ();
        return matrix;
    }

    public static Mat4x4 matrixMakeProjection(float fovDegrees, float aspectRatio, float near, float far) {
        float fovRad = (float) (1.0f / Math.tan(fovDegrees * 0.5f / 180.0f * 3.14159f));
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = aspectRatio * fovRad;
        matrix.getM()[1][1] = fovRad;
        matrix.getM()[2][2] = far / (far - near);
        matrix.getM()[3][2] = (-far * near) / (far - near);
        matrix.getM()[2][3] = 1.0f;
        matrix.getM()[3][3] = 0.0f;
        return matrix;
    }

    public static Mat4x4 matrixMakeOrthogonalProjection() {
        // Project Cube Orthographically
        float left = -1 * 0.5f;
        float right = 1 * 0.5f;
        float top = 1 * 0.5f;
        float bottom = -1 * 0.5f;
        float near = 0.1f;
        float far = 100.0f;
        Mat4x4 matrix = new Mat4x4();
        matrix.getM()[0][0] = (2.0f / (right - left));
        matrix.getM()[0][3] = - ((right + left) / (right - left));
        matrix.getM()[1][1] = (2.0f / (top - bottom));
        matrix.getM()[1][3] = ((top + bottom) / (top - bottom));
        matrix.getM()[2][2] = (2.0f / (far - near));
        matrix.getM()[2][3] = ((far + near) / (far - near));
        matrix.getM()[3][3] = 0.0f;
        return matrix;
    }

    public static Mat4x4 matrixPointAt(Vec4df pos, Vec4df target, Vec4df up) {
        Vec4df newForward = vectorSub(target, pos);
        newForward = vectorNormalise(newForward);

        // Calculate new Up direction
        Vec4df a = vectorMul(newForward, vectorDotProduct(up, newForward));
        Vec4df newUp = vectorSub(up, a);
        newUp = vectorNormalise(newUp);

        // New Right direction is easy, its just cross product
        Vec4df newRight = vectorCrossProduct(newUp, newForward);

        // Construct Dimensioning and Translation Matrix
        Mat4x4 matrix = new Mat4x4();

        matrix.getM()[0][0] = newRight.getX();
        matrix.getM()[0][1] = newRight.getY();
        matrix.getM()[0][2] = newRight.getZ();
        matrix.getM()[0][3] = 0.0f;
        matrix.getM()[1][0] = newUp.getX();
        matrix.getM()[1][1] = newUp.getY();
        matrix.getM()[1][2] = newUp.getZ();
        matrix.getM()[1][3] = 0.0f;
        matrix.getM()[2][0] = newForward.getX();
        matrix.getM()[2][1] = newForward.getY();
        matrix.getM()[2][2] = newForward.getZ();
        matrix.getM()[2][3] = 0.0f;
        matrix.getM()[3][0] = pos.getX();
        matrix.getM()[3][1] = pos.getY();
        matrix.getM()[3][2] = pos.getZ();
        matrix.getM()[3][3] = 1.0f;

        return matrix;
    }

    public static Mat4x4 matrixQuickInverse(Mat4x4 m) {
        Mat4x4 matrix = new Mat4x4();

        matrix.getM()[0][0] = m.getM()[0][0];
        matrix.getM()[0][1] = m.getM()[1][0];
        matrix.getM()[0][2] = m.getM()[2][0];
        matrix.getM()[0][3] = 0.0f;
        matrix.getM()[1][0] = m.getM()[0][1];
        matrix.getM()[1][1] = m.getM()[1][1];
        matrix.getM()[1][2] = m.getM()[2][1];
        matrix.getM()[1][3] = 0.0f;
        matrix.getM()[2][0] = m.getM()[0][2];
        matrix.getM()[2][1] = m.getM()[1][2];
        matrix.getM()[2][2] = m.getM()[2][2];
        matrix.getM()[2][3] = 0.0f;
        matrix.getM()[3][0] = -(m.getM()[3][0] * matrix.getM()[0][0] + m.getM()[3][1] * matrix.getM()[1][0] + m.getM()[3][2] * matrix.getM()[2][0]);
        matrix.getM()[3][1] = -(m.getM()[3][0] * matrix.getM()[0][1] + m.getM()[3][1] * matrix.getM()[1][1] + m.getM()[3][2] * matrix.getM()[2][1]);
        matrix.getM()[3][2] = -(m.getM()[3][0] * matrix.getM()[0][2] + m.getM()[3][1] * matrix.getM()[1][2] + m.getM()[3][2] * matrix.getM()[2][2]);
        matrix.getM()[3][3] = 1.0f;

        return matrix;
    } // Only for Rotation/Translation Matrices

    public static Mat4x4 matrixInverse(Mat4x4 m) {
        double  det;

        Mat4x4 matInv = new Mat4x4();

        matInv.getM()[0][0] =  m.getM()[1][1] * m.getM()[2][2] * m.getM()[3][3] - m.getM()[1][1] * m.getM()[2][3] * m.getM()[3][2] - m.getM()[2][1] * m.getM()[1][2] * m.getM()[3][3] + m.getM()[2][1] * m.getM()[1][3] * m.getM()[3][2] + m.getM()[3][1] * m.getM()[1][2] * m.getM()[2][3] - m.getM()[3][1] * m.getM()[1][3] * m.getM()[2][2];
        matInv.getM()[1][0] = -m.getM()[1][0] * m.getM()[2][2] * m.getM()[3][3] + m.getM()[1][0] * m.getM()[2][3] * m.getM()[3][2] + m.getM()[2][0] * m.getM()[1][2] * m.getM()[3][3] - m.getM()[2][0] * m.getM()[1][3] * m.getM()[3][2] - m.getM()[3][0] * m.getM()[1][2] * m.getM()[2][3] + m.getM()[3][0] * m.getM()[1][3] * m.getM()[2][2];
        matInv.getM()[2][0] =  m.getM()[1][0] * m.getM()[2][1] * m.getM()[3][3] - m.getM()[1][0] * m.getM()[2][3] * m.getM()[3][1] - m.getM()[2][0] * m.getM()[1][1] * m.getM()[3][3] + m.getM()[2][0] * m.getM()[1][3] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[1][1] * m.getM()[2][3] - m.getM()[3][0] * m.getM()[1][3] * m.getM()[2][1];
        matInv.getM()[3][0] = -m.getM()[1][0] * m.getM()[2][1] * m.getM()[3][2] + m.getM()[1][0] * m.getM()[2][2] * m.getM()[3][1] + m.getM()[2][0] * m.getM()[1][1] * m.getM()[3][2] - m.getM()[2][0] * m.getM()[1][2] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[1][1] * m.getM()[2][2] + m.getM()[3][0] * m.getM()[1][2] * m.getM()[2][1];
        matInv.getM()[0][1] = -m.getM()[0][1] * m.getM()[2][2] * m.getM()[3][3] + m.getM()[0][1] * m.getM()[2][3] * m.getM()[3][2] + m.getM()[2][1] * m.getM()[0][2] * m.getM()[3][3] - m.getM()[2][1] * m.getM()[0][3] * m.getM()[3][2] - m.getM()[3][1] * m.getM()[0][2] * m.getM()[2][3] + m.getM()[3][1] * m.getM()[0][3] * m.getM()[2][2];
        matInv.getM()[1][1] =  m.getM()[0][0] * m.getM()[2][2] * m.getM()[3][3] - m.getM()[0][0] * m.getM()[2][3] * m.getM()[3][2] - m.getM()[2][0] * m.getM()[0][2] * m.getM()[3][3] + m.getM()[2][0] * m.getM()[0][3] * m.getM()[3][2] + m.getM()[3][0] * m.getM()[0][2] * m.getM()[2][3] - m.getM()[3][0] * m.getM()[0][3] * m.getM()[2][2];
        matInv.getM()[2][1] = -m.getM()[0][0] * m.getM()[2][1] * m.getM()[3][3] + m.getM()[0][0] * m.getM()[2][3] * m.getM()[3][1] + m.getM()[2][0] * m.getM()[0][1] * m.getM()[3][3] - m.getM()[2][0] * m.getM()[0][3] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[0][1] * m.getM()[2][3] + m.getM()[3][0] * m.getM()[0][3] * m.getM()[2][1];
        matInv.getM()[3][1] =  m.getM()[0][0] * m.getM()[2][1] * m.getM()[3][2] - m.getM()[0][0] * m.getM()[2][2] * m.getM()[3][1] - m.getM()[2][0] * m.getM()[0][1] * m.getM()[3][2] + m.getM()[2][0] * m.getM()[0][2] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[0][1] * m.getM()[2][2] - m.getM()[3][0] * m.getM()[0][2] * m.getM()[2][1];
        matInv.getM()[0][2] =  m.getM()[0][1] * m.getM()[1][2] * m.getM()[3][3] - m.getM()[0][1] * m.getM()[1][3] * m.getM()[3][2] - m.getM()[1][1] * m.getM()[0][2] * m.getM()[3][3] + m.getM()[1][1] * m.getM()[0][3] * m.getM()[3][2] + m.getM()[3][1] * m.getM()[0][2] * m.getM()[1][3] - m.getM()[3][1] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[1][2] = -m.getM()[0][0] * m.getM()[1][2] * m.getM()[3][3] + m.getM()[0][0] * m.getM()[1][3] * m.getM()[3][2] + m.getM()[1][0] * m.getM()[0][2] * m.getM()[3][3] - m.getM()[1][0] * m.getM()[0][3] * m.getM()[3][2] - m.getM()[3][0] * m.getM()[0][2] * m.getM()[1][3] + m.getM()[3][0] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[2][2] =  m.getM()[0][0] * m.getM()[1][1] * m.getM()[3][3] - m.getM()[0][0] * m.getM()[1][3] * m.getM()[3][1] - m.getM()[1][0] * m.getM()[0][1] * m.getM()[3][3] + m.getM()[1][0] * m.getM()[0][3] * m.getM()[3][1] + m.getM()[3][0] * m.getM()[0][1] * m.getM()[1][3] - m.getM()[3][0] * m.getM()[0][3] * m.getM()[1][1];
        matInv.getM()[3][2] = -m.getM()[0][0] * m.getM()[1][1] * m.getM()[3][2] + m.getM()[0][0] * m.getM()[1][2] * m.getM()[3][1] + m.getM()[1][0] * m.getM()[0][1] * m.getM()[3][2] - m.getM()[1][0] * m.getM()[0][2] * m.getM()[3][1] - m.getM()[3][0] * m.getM()[0][1] * m.getM()[1][2] + m.getM()[3][0] * m.getM()[0][2] * m.getM()[1][1];
        matInv.getM()[0][3] = -m.getM()[0][1] * m.getM()[1][2] * m.getM()[2][3] + m.getM()[0][1] * m.getM()[1][3] * m.getM()[2][2] + m.getM()[1][1] * m.getM()[0][2] * m.getM()[2][3] - m.getM()[1][1] * m.getM()[0][3] * m.getM()[2][2] - m.getM()[2][1] * m.getM()[0][2] * m.getM()[1][3] + m.getM()[2][1] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[1][3] =  m.getM()[0][0] * m.getM()[1][2] * m.getM()[2][3] - m.getM()[0][0] * m.getM()[1][3] * m.getM()[2][2] - m.getM()[1][0] * m.getM()[0][2] * m.getM()[2][3] + m.getM()[1][0] * m.getM()[0][3] * m.getM()[2][2] + m.getM()[2][0] * m.getM()[0][2] * m.getM()[1][3] - m.getM()[2][0] * m.getM()[0][3] * m.getM()[1][2];
        matInv.getM()[2][3] = -m.getM()[0][0] * m.getM()[1][1] * m.getM()[2][3] + m.getM()[0][0] * m.getM()[1][3] * m.getM()[2][1] + m.getM()[1][0] * m.getM()[0][1] * m.getM()[2][3] - m.getM()[1][0] * m.getM()[0][3] * m.getM()[2][1] - m.getM()[2][0] * m.getM()[0][1] * m.getM()[1][3] + m.getM()[2][0] * m.getM()[0][3] * m.getM()[1][1];
        matInv.getM()[3][3] =  m.getM()[0][0] * m.getM()[1][1] * m.getM()[2][2] - m.getM()[0][0] * m.getM()[1][2] * m.getM()[2][1] - m.getM()[1][0] * m.getM()[0][1] * m.getM()[2][2] + m.getM()[1][0] * m.getM()[0][2] * m.getM()[2][1] + m.getM()[2][0] * m.getM()[0][1] * m.getM()[1][2] - m.getM()[2][0] * m.getM()[0][2] * m.getM()[1][1];

        det = m.getM()[0][0] * matInv.getM()[0][0] + m.getM()[0][1] * matInv.getM()[1][0] + m.getM()[0][2] * matInv.getM()[2][0] + m.getM()[0][3] * matInv.getM()[3][0];
        //	if (det == 0) return false;

        det = 1.0 / det;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matInv.getM()[i][j] *= (float) det;
            }
        }

        return matInv;
    }

    public static Vec3df vectorAdd(Vec3df v1, Vec3df v2) {
        return new Vec3df(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vec4df vectorAdd(Vec4df v1, Vec4df v2) {
        return new Vec4df(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vec4df vectorSub(Vec4df v1, Vec4df v2) {
        return new Vec4df(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vec3df vectorMul(Vec3df v1, float k) {
        return new Vec3df(v1.getX() * k, v1.getY() * k, v1.getZ() * k);
    }

    public static Vec4df vectorMul(Vec4df v1, float k) {
        return new Vec4df(v1.getX() * k, v1.getY() * k, v1.getZ() * k);
    }

    public static Vec4df vectorDiv(Vec4df v1, float k) {
        return new Vec4df(v1.getX() / k, v1.getY() / k, v1.getZ() / k);
    }

    public static float vectorDotProduct(Vec4df v1, Vec4df v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    private static float vectorLength(Vec4df v) {
        return (float) Math.sqrt(vectorDotProduct(v, v));
    }

    public static Vec4df vectorNormalise(Vec4df v) {
        float l = vectorLength(v);
        return new Vec4df(v.getX() / l, v.getY() / l, v.getZ() / l );
    }

    public static Vec4df vectorCrossProduct(Vec4df v1, Vec4df v2) {
        Vec4df v = new Vec4df();
        v.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        v.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        v.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());
        return v;
    }

    /**
     * This enum contextualises more the code. It is used in the
     * triangleClipAgainstPlane.
     * There are four possible scenarios to clip a triangle against a plane:
     * <ul>
     *     <li>
     *         ALL_POINTS_INSIDE: all points lie on the inside of plane,
     *         so do nothing and allow the triangle to simply pass through
     *     </li>
     *     <li>
     *         ALL_PINTS_OUTSIDE: all points lie on the outside of plane,
     *         so clip whole triangle. It ceases to exist
     *     </li>
     *     <li>
     *         ONE_POINT_OUTSIDE: the triangle should be clipped.
     *         As two points lie inside the plane, the clipped triangle becomes a "quad".
     *         Fortunately, we can represent a quad with two new triangles
     *     </li>
     *     <li>
     *         TWO_POINTS_OUTSIDE: the triangle should be clipped.
     *         As two points lie outside the plane, the triangle simply becomes a smaller triangle
     *     </li>
     * </ul>
     */
    private enum ClipTriangleCase {
        ALL_POINTS_INSIDE,
        ALL_POINTS_OUTSIDE,
        ONE_POINT_OUTSIDE,
        TWO_POINTS_OUTSIDE
    }

    /**
     * What I have do here is split in half the original method of vectorIntersectPlane
     *
     * @param plane_p point in the plane, it is used to define the plane equation
     * @param plane_n normal to the plane, it is used to define the plane equation
     * @param lineStart starting point of the line
     * @param lineEnd ending point of the line
     * @return return the t value, what is the normalise point if the triangle
     * intersects with the plane
     */
    private static float calculateT(Vec4df plane_p, Vec4df plane_n, Vec4df lineStart, Vec4df lineEnd) {
        plane_n = vectorNormalise(plane_n);
        float plane_d = -vectorDotProduct(plane_n, plane_p);
        float ad = vectorDotProduct(lineStart, plane_n);
        float bd = vectorDotProduct(lineEnd, plane_n);
        return (-plane_d - ad) / (bd - ad);
    }

    /**
     * This method is the second part of the original method @vectorIntersectPlane
     * @param lineStart the starting point of the line
     * @param lineEnd the ending point of the line
     * @param t the t value from the @calculateT method
     * @return return a point if the line intersects with the plane
     */
    private static Vec4df calculateIntersectionPoint(Vec4df lineStart, Vec4df lineEnd, float t) {
        Vec4df lineStartToEnd = vectorSub(lineEnd, lineStart);
        Vec4df lineToIntersect = vectorMul(lineStartToEnd, t);
        return vectorAdd(lineStart, lineToIntersect);
    }

    /**
     * Will return a vector if the line crosses the plane. How do lines intersect planes.
     * @param plane_p point in the plane, it is used to define the plane equation
     * @param plane_n normal to the plane, it is used to define the plane equation
     * @param lineStart starting point of the line
     * @param lineEnd ending point of the line
     * @return return a point if the line intersects with the plane
     */
    public static Vec4df vectorIntersectPlane(Vec4df plane_p, Vec4df plane_n, Vec4df lineStart, Vec4df lineEnd) {
        float t = calculateT(plane_p, plane_n, lineStart, lineEnd);
        return  calculateIntersectionPoint(lineStart, lineEnd, t);
    }

    /**
     * To classify whether a point is on the inside or the outside of the triangle the easiest way to
     * calculate this is to calculate the distance between the point and the nearest point on the
     * surface of the plane. We can look at the sign of this distance to indicate whether we're
     * on the inside or outside of the plane.
     * @param point the point
     * @param planePoint plane point
     * @param planeNormal plane normal
     * @return signed shortest distance from point to plane, plane normal must be normalised
     */
    private static float distancePointToPlane(Vec4df point, Vec4df planePoint, Vec4df planeNormal) {
        return planeNormal.getX() * point.getX() +
                planeNormal.getY() * point.getY() +
                planeNormal.getZ() * point.getZ()
                - vectorDotProduct(planeNormal, planePoint);
    }

    /**
     * This method classify the triangle points, and break the input triangle into
     * smaller output triangles if required. There are four possible outcomes which
     * are recollected inside the enum object @ClipTriangleCase
     * @param numPointsInside the number of points inside
     * @param numPointsOutside the number of points outside
     * @return return the four possibles cases of clipping
     */
    private static ClipTriangleCase getClipCase(int numPointsInside, int numPointsOutside) {
        if ( numPointsInside == 0 ) {
            return ClipTriangleCase.ALL_POINTS_OUTSIDE;
        } else if ( numPointsInside == 3 ) {
            return ClipTriangleCase.ALL_POINTS_INSIDE;
        } else if ( numPointsInside == 1 && numPointsOutside == 2 ) {
            return ClipTriangleCase.TWO_POINTS_OUTSIDE;
        } else if ( numPointsInside == 2 && numPointsOutside == 1 ) {
            return  ClipTriangleCase.ONE_POINT_OUTSIDE;
        } else {
            return null;
        }
    }

    /**
     * This method is called when the triangle had to be clipped against the plane and
     * there are only one point inside.
     * Firstly, this method copies the appearance of the input triangle to the output triangle.
     * Secondly, it calculates the new points for the output triangle.
     * The inside point is valid, that point is copied into the output triangle.
     * But the two new points are at the locations where the original sides of
     * the triangle (lines) intersect with the plane. So they two have to be calculated.
     *
     * @param planePoint point in the plane, it is used to define the plane equation
     * @param planeNormal normal to the plane, it is used to define the plane equation
     * @param inside_points the points inside the screen from the original triangle
     * @param outside_points the points outside the screen from the original triangle
     * @param inside_tex the texture points inside the screen from the original triangle
     * @param outside_tex the texture points outside the screen from the original triangle
     * @param inputTriangle the input triangle
     * @return only one triangle reduced from the input triangle
     */
    private static Triangle triangleClipReduceCase(Vec4df planePoint, Vec4df planeNormal,
                                                   Vec4df[] inside_points, Vec4df[] outside_points,
                                                   Vec3df[] inside_tex, Vec3df[] outside_tex,
                                                   Triangle inputTriangle) {
        Triangle outputTriangle1 = new Triangle();

        outputTriangle1.setColor(inputTriangle.getColor());
        outputTriangle1.setBrightness(inputTriangle.getBrightness());

        outputTriangle1.getP()[0].set(inside_points[0]);
        outputTriangle1.getT()[0].set(inside_tex[0]);

        float t;

        // todo hay algo mal aquí

        t = calculateT(planePoint, planeNormal, inside_points[0], outside_points[0]);
        outputTriangle1.getP()[1] = calculateIntersectionPoint(inside_points[0], outside_points[0], t);
        outputTriangle1.getT()[1].setX(t * (outside_tex[0].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
        outputTriangle1.getT()[1].setY(t * (outside_tex[0].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
        outputTriangle1.getT()[1].setZ(t * (outside_tex[0].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

        t = calculateT(planePoint, planeNormal, inside_points[0], outside_points[1]);
        outputTriangle1.getP()[2] = calculateIntersectionPoint(inside_points[0], outside_points[1], t);
        outputTriangle1.getT()[2].setX(t * (outside_tex[1].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
        outputTriangle1.getT()[2].setY(t * (outside_tex[1].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
        outputTriangle1.getT()[2].setZ(t * (outside_tex[1].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

        return outputTriangle1;
    }

    /**
     * Firstly, this method copies the appearance of the input triangle to the two outputs triangles.
     * Secondly, it builds two new triangles. The first triangle consists of the two inside points and a new
     * point determined by the location where one side of the triangle intersects with the plane.
     * The second triangle is composed of one of he inside points, a new point determined by the
     * intersection of the other side of the triangle and the plane, and the newly created point above.
     *
     * @param planePoint point in the plane, it is used to define the plane equation
     * @param planeNormal normal to the plane, it is used to define the plane equation
     * @param inside_points the points inside the screen from the original triangle
     * @param outside_points the points outside the screen from the original triangle
     * @param inside_tex the texture points inside the screen from the original triangle
     * @param outside_tex the texture points outside the screen from the original triangle
     * @param inputTriangle the input triangle
     * @return returns two new triangles, result of clip the quad case in two triangles
     */
    private static Triangle[] triangleClipQuadCase(Vec4df planePoint, Vec4df planeNormal,
                                                   Vec4df[] inside_points, Vec4df[] outside_points,
                                                   Vec3df[] inside_tex, Vec3df[] outside_tex,
                                                   Triangle inputTriangle) {
        Triangle outputTriangle1 = new Triangle();
        Triangle outputTriangle2 = new Triangle();

        outputTriangle1.setColor(inputTriangle.getColor());
        outputTriangle2.setColor(inputTriangle.getColor());
        outputTriangle1.setBrightness(inputTriangle.getBrightness());
        outputTriangle2.setBrightness(inputTriangle.getBrightness());

        outputTriangle1.getP()[0].set(inside_points[0]);
        outputTriangle1.getP()[1].set(inside_points[1]);
        outputTriangle1.getT()[0].set(inside_tex[0]);
        outputTriangle1.getT()[1].set(inside_tex[1]);

        float t;
        t = calculateT(planePoint, planeNormal, inside_points[0], outside_points[0]);
        outputTriangle1.getP()[2] = calculateIntersectionPoint(inside_points[0], outside_points[0], t);
        outputTriangle1.getT()[2].setX(t * (outside_tex[0].getX() - inside_tex[0].getX()) + inside_tex[0].getX());
        outputTriangle1.getT()[2].setY(t * (outside_tex[0].getY() - inside_tex[0].getY()) + inside_tex[0].getY());
        outputTriangle1.getT()[2].setZ(t * (outside_tex[0].getZ() - inside_tex[0].getZ()) + inside_tex[0].getZ());

        outputTriangle2.getP()[0].set(inside_points[1]);
        outputTriangle2.getT()[0].set(inside_tex[1]);
        outputTriangle2.getP()[1].set(outputTriangle1.getP()[2]);
        outputTriangle2.getT()[1].set(outputTriangle1.getT()[2]);

        t = calculateT(planePoint, planeNormal, inside_points[1], outside_points[0]);
        outputTriangle2.getP()[2] = calculateIntersectionPoint(inside_points[1], outside_points[0], t);
        outputTriangle2.getT()[2].setX(t * (outside_tex[0].getX() - inside_tex[1].getX()) + inside_tex[1].getX());
        outputTriangle2.getT()[2].setY(t * (outside_tex[0].getY() - inside_tex[1].getY()) + inside_tex[1].getY());
        outputTriangle2.getT()[2].setZ(t * (outside_tex[0].getZ() - inside_tex[1].getZ()) + inside_tex[1].getZ());

        return new Triangle[] { outputTriangle1, outputTriangle2 };
    }

    /**
     * This method encapsulates all the clip methods.
     *
     * @param triangleCase the case of clipping
     * @param inputTriangle the input triangle
     * @param planePoint point in the plane, it is used to define the plane equation
     * @param planeNormal normal to the plane, it is used to define the plane equation
     * @param insidePoints the points inside the screen from the original triangle
     * @param outsidePoints the points outside the screen from the original triangle
     * @param insideTex the texture points inside the screen from the original triangle
     * @param outsideTex the texture points outside the screen from the original triangle
     * @return an ArrayList which contains the result triangles
     */
    private static ArrayList<Triangle> clipTriangle(ClipTriangleCase triangleCase, Triangle inputTriangle,
                                                    Vec4df planePoint, Vec4df planeNormal,
                                                    Vec4df[] insidePoints, Vec4df[] outsidePoints,
                                                    Vec3df[] insideTex, Vec3df[] outsideTex) {
        ArrayList<Triangle> trianglesClipped = new ArrayList<>();
        switch ( triangleCase ) {
            default: case ALL_POINTS_OUTSIDE:
                return trianglesClipped;
            case ALL_POINTS_INSIDE:
                trianglesClipped.add(inputTriangle);
                return trianglesClipped;
            case TWO_POINTS_OUTSIDE:
                trianglesClipped.add(triangleClipReduceCase(
                        planePoint, planeNormal,
                        insidePoints, outsidePoints,
                        insideTex, outsideTex,
                        inputTriangle));
                return trianglesClipped;
            case ONE_POINT_OUTSIDE:
                Triangle[] newTriangles = triangleClipQuadCase(
                        planePoint, planeNormal,
                        insidePoints, outsidePoints,
                        insideTex, outsideTex,
                        inputTriangle);
                trianglesClipped.add(newTriangles[0]);
                trianglesClipped.add(newTriangles[1]);
                return trianglesClipped;
        }
    }

    /**
     * This method copies the input triangle texture information to the
     * output triangle texture.
     * @param inputTriangle the input triangle
     * @param outputTriangle the output triangle
     */
    private static void copyTriangleTexture(Triangle inputTriangle, Triangle outputTriangle) {
        outputTriangle.getT()[0] = inputTriangle.getT()[0];
        outputTriangle.getT()[1] = inputTriangle.getT()[1];
        outputTriangle.getT()[2] = inputTriangle.getT()[2];
    }

    /**
     * Triangle clipping method. There are three possible scenarios:
     *   1) It returns 0 triangles: the triangle is outside of the plane.
     *   2) It returns 1 triangle: all the triangle is inside or there are only one
     *      point of the triangle inside, returning a new more tiny triangle.
     *   3) It returns 2 triangles: there are two points inside, so there left a quad
     *      inside. Then, this quad is going to be cut into two triangles.
     *
     * By this function we provide the plane equation parameters: the position and the normal.
     * It knows if a point is inside or outside getting the distance between the point and the plane.
     * If the distance is negative, the point is outside, if the distance is positive, the point
     * is inside. We increase our counter fot that, and assign the point to the correspond array.
     *
     * @param planePoint point in the plane, it is used to define the plane equation
     * @param planeNormal normal to the plane, it is used to define the plane equation
     * @param triangle the triangle to clip.
     * @return return an @ArrayList which contains the clipped triangles
     */
    public static ArrayList<Triangle> triangleClipAgainstPlane(Vec4df planePoint, Vec4df planeNormal, Triangle triangle) {
        planeNormal = vectorNormalise(planeNormal);

        Vec4df[] inside_points = new Vec4df[3];
        int nInsidePointCount = 0;
        Vec4df[] outside_points = new Vec4df[3];
        int nOutsidePointCount = 0;

        Vec3df[] inside_tex = new Vec3df[3];
        int nInsideTexCount = 0;
        Vec3df[] outside_tex = new Vec3df[3];
        int nOutsideTexCount = 0;

        float d0 = distancePointToPlane(triangle.getP()[0], planePoint, planeNormal);
        float d1 = distancePointToPlane(triangle.getP()[1], planePoint, planeNormal);
        float d2 = distancePointToPlane(triangle.getP()[2], planePoint, planeNormal);

        if ( d0 >= 0 ) {
            inside_points[nInsidePointCount] = new Vec4df(triangle.getP()[0]);
            inside_tex[nInsideTexCount] = new Vec3df(triangle.getT()[0]);
            nInsidePointCount++;
            nInsideTexCount++;
        } else {
            outside_points[nOutsidePointCount] = new Vec4df(triangle.getP()[0]);
            outside_tex[nOutsideTexCount] = new Vec3df(triangle.getT()[0]);
            nOutsidePointCount++;
            nOutsideTexCount++;
        }

        if ( d1 >= 0 ) {
            inside_points[nInsidePointCount] = new Vec4df(triangle.getP()[1]);
            inside_tex[nInsideTexCount] = new Vec3df(triangle.getT()[1]);
            nInsidePointCount++;
            nInsideTexCount++;
        } else {
            outside_points[nOutsidePointCount] = new Vec4df(triangle.getP()[1]);
            outside_tex[nOutsideTexCount] = new Vec3df(triangle.getT()[1]);
            nOutsidePointCount++;
            nOutsideTexCount++;
        }

        if ( d2 >= 0 ) {
            inside_points[nInsidePointCount] = new Vec4df(triangle.getP()[2]);
            inside_tex[nInsideTexCount] = new Vec3df(triangle.getT()[2]);
            nInsidePointCount++;
            nInsideTexCount++;
        } else {
            outside_points[nOutsidePointCount] = new Vec4df(triangle.getP()[2]);
            outside_tex[nOutsideTexCount] = new Vec3df(triangle.getT()[2]);
            nOutsidePointCount++;
            nOutsideTexCount++;
        }

        ClipTriangleCase triangleCase = getClipCase(nInsidePointCount, nOutsidePointCount);
        if ( triangleCase != null ) {
            return clipTriangle(
                    triangleCase, triangle,
                    planePoint, planeNormal,
                    inside_points, outside_points,
                    inside_tex, outside_tex);
        } else {
            return null;
        }

    }

}

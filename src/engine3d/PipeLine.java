package engine3d;

import engine3d.matrix.Mat4x4;
import engine3d.matrix.MatrixMath;
import engine3d.mesh.Mesh;
import engine3d.mesh.Model;
import engine3d.mesh.Triangle;
import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;
import olcPGEApproach.gfx.images.Image;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class encapsulate all the methods to
 * render 3D graphics.
 *
 * @class PipeLine
 * @author Sergio Martí Torregrosa
 * @date 18/08/2020
 */
public class PipeLine {

    /**
     * We need this for the offsetScaleTriangle method.
     * How is a susceptible point to increase the computational
     * cost, we define this constant here
     */
    private final Vec4df OFFSET_VIEW = new Vec4df(1.0f, 1.0f, 0.0f, 0.0f);

    /**
     * We need this for the triangleClipAgainstPlane
     */
    private final Vec4df PLANE_POINT = new Vec4df(0.0f, 0.0f, 0.1f);

    /**
     * Same as the planePoint
     */
    private final Vec4df PLANE_NORMAL = new Vec4df(0.0f, 0.0f, 1.0f);

    /**
     * The 3D renderer with all needed methods for drawing
     */
    private Renderer3D renderer3D;

    /**
     * The camera object
     */
    private Camera cameraObj;

    /**
     * The view matrix
     */
    private Mat4x4 matView;

    /**
     * The projection matrix
     */
    private Mat4x4 matProjection;

    /**
     * The world matrix transformation
     */
    private Mat4x4 worldMatrix;

    /**
     * The light source
     */
    private Vec4df lightDirection;

    /**
     * The kind of perspective
     */
    private Perspective perspective;

    /**
     * The screen width
     */
    private int screenWidth;

    /**
     * The screen height
     */
    private int screenHeight;

    /**
     * This are the num of triangles drawn
     */
    private int numTrianglesDrawn = 0;

    /**
     * The constructor
     */
    public PipeLine(int[] p, int pW, int pH) {
        screenWidth = pW;
        screenHeight = pH;

        perspective = Perspective.NORMAL;

        renderer3D = new Renderer3D(p, pW, pH);

        cameraObj = new Camera();
        cameraObj.setOrigin(new Vec4df(0.0f, 0.0f, -1.0f));
        matView = cameraObj.getMatView();

        matProjection = buildProjectionMatrix(screenWidth, screenHeight);

        worldMatrix = MatrixMath.matrixMakeIdentity();

        lightDirection = new Vec4df(0.0f, 0.0f, -1.0f);
        lightDirection.normalize();
    }

    /**
     * This method builds the normal projection matrix.
     * @param width the screen width
     * @param height the screen height
     * @return the normal projection matrix
     */
    private Mat4x4 buildNormalProjectionMatrix(int width, int height) {
        float near = 0.1f;
        float far = 1000.0f;
        float fovDegrees = 90.0f;
        float aspectRatio = (float)height / (float)width;
        return MatrixMath.matrixMakeProjection(fovDegrees, aspectRatio, near, far);
    }

    /**
     * This method builds the projection matrix.
     * @param width the screen width
     * @param height the screen height
     * @return returns the projection matrix
     */
    private Mat4x4 buildProjectionMatrix(int width, int height) {
        switch ( perspective ) {
            case NORMAL: default:
                return buildNormalProjectionMatrix(width, height);
            case ORTHOGONAL:
                return MatrixMath.matrixMakeOrthogonalProjection();
        }
    }

    /**
     * This method copy the @ArrayList of @Triangles from the
     * pipeline. It isn't the same instances what are inside the
     * mesh object. It has to be different, to not modify the
     * original mesh.
     * @param mesh the mesh to copy the triangles
     * @return return a copied triangles from the mesh triangles
     */
    private ArrayList<Triangle> copyMeshTriangles(Mesh mesh) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        Triangle copiedTriangle;
        Vec4df[] trianglePoints;
        Vec3df[] triangleTexture;
        for ( Triangle triangle : mesh.getTris() ) {
            copiedTriangle = new Triangle();
            trianglePoints = new Vec4df[3];
            triangleTexture = new Vec3df[3];
            trianglePoints[0] = triangle.getP()[0];
            trianglePoints[1] = triangle.getP()[1];
            trianglePoints[2] = triangle.getP()[2];
            triangleTexture[0] = triangle.getT()[0];
            triangleTexture[1] = triangle.getT()[1];
            triangleTexture[2] = triangle.getT()[2];
            copiedTriangle.setP(trianglePoints);
            copiedTriangle.setT(triangleTexture);
            triangles.add(copiedTriangle);
        }
        return triangles;
    }

    /**
     * This method transform the mesh by the worldMatrix
     * @param mesh the mesh to transform
     */
    private ArrayList<Triangle> transformTriangles(Mesh mesh) {
        ArrayList<Triangle> trianglesTransformed = new ArrayList<>();

        for ( Triangle triangle : mesh.getTris() ) {
            Triangle triangleTransformed = new Triangle();
            triangleTransformed.setP(MatrixMath.matrixMultiplyVectors(worldMatrix, triangle.getP()));
            triangleTransformed.setT(triangle.getT());
            trianglesTransformed.add(triangleTransformed);
        }

        return  trianglesTransformed;
    }

    /**
     * This method calculate the brightness of the color pass as a paramether
     * @param light ilumination. 0 for nothing and 1 for max
     * @return return color grey scale illumination based
     */
    private int calculateColor(float light) {
        light = Math.max(0.1f, light);
        light *= 255;
        return (0xff << 24 | (int)light << 16 | (int)light << 8 | (int)light);
    }

    /**
     * The normal vector of a plane is a perpendicular vector for the two directions
     * which form the plane. Do 90 degrees with the two lines
     * @param triangle the triangle
     * @return returns the normal vector for the plane where is the triangle.
     */
    private Vec4df calculateNormalToPlane(Triangle triangle) {
        Vec4df normal = new Vec4df();

        Vec4df line1 = MatrixMath.vectorSub(triangle.getP()[1], triangle.getP()[0]);

        Vec4df line2 = MatrixMath.vectorSub(triangle.getP()[2], triangle.getP()[0]);

        normal.setX( line1.getY() * line2.getZ() - line1.getZ() * line2.getY() );
        normal.setY( line1.getZ() * line2.getX() - line1.getX() * line2.getZ() );
        normal.setZ( line1.getX() * line2.getY() - line1.getY() * line2.getX() );

        normal.normalize();

        return normal;
    }

    /**
     * This method corrects the texture
     * @param triangle the triangle to correct
     */
    private void textureCorrection(Triangle triangle) {
        for ( int i = 0; i < triangle.getT().length; i++ ) {
            triangle.getT()[i].setX(triangle.getT()[i].getX() / triangle.getP()[i].getW());
            triangle.getT()[i].setY(triangle.getT()[i].getY() / triangle.getP()[i].getW());
            triangle.getT()[i].setZ(1.0f / triangle.getP()[i].getW());
        }
    }

    /**
     * This function scales and translate the projected points into something visible.
     * @param triangle the 2D triangle projected.
     * @param width the screen width.
     * @param height the screen height.
     */
    private void offSetProjectedTriangle(Triangle triangle, int width, int height) {
        for ( int i = 0; i < triangle.getP().length; i++ ) {
            triangle.getP()[i].set(MatrixMath.vectorDiv(triangle.getP()[i], triangle.getP()[i].getW()));
            triangle.getP()[i].multiplyXBy(-1.0f);
            triangle.getP()[i].multiplyYBy(-1.0f);
            triangle.getP()[i].set(MatrixMath.vectorAdd(triangle.getP()[i], OFFSET_VIEW));
            triangle.getP()[i].multiplyXBy(0.5f * width);
            triangle.getP()[i].multiplyYBy(0.5f * height);
        }
    }

    /**
     * This method is used for calculate the tre projected triangles from 3D space
     * to 2D space. It needs the screen width and the screen height for translate
     * the result into something visible in the screen
     * @param triangles the 3D triangles to 2D project.
     * @param width screen width.
     * @param height screen height.
     * @return @ArrayList with all 2D triangles projected.
     */
    private ArrayList<Triangle> projectTriangles(ArrayList<Triangle> triangles, int width, int height) {
        ArrayList<Triangle> trianglesProjected = new ArrayList<>();
        Triangle triangleViewed, triangleProjected;
        Vec4df normal, diffTrianglePointCameraOrigin;
        float light;
        int color;
        for ( Triangle triangle : triangles ) {

            normal = calculateNormalToPlane(triangle);

            diffTrianglePointCameraOrigin = MatrixMath.vectorSub(triangle.getP()[0], cameraObj.getOrigin());

            if ( MatrixMath.vectorDotProduct(normal, diffTrianglePointCameraOrigin) < 0.0f ) {
                light = MatrixMath.vectorDotProduct(normal, lightDirection);
                color = calculateColor(light);

                triangleViewed = new Triangle(
                        MatrixMath.matrixMultiplyVectors(matView, triangle.getP()),
                        triangle.getT(),
                        color,
                        light
                );

                ArrayList<Triangle> clippedTriangles = MatrixMath.triangleClipAgainstPlane(
                        PLANE_POINT,
                        PLANE_NORMAL,
                        triangleViewed
                );

                assert clippedTriangles != null;
                for ( Triangle triangleClipped : clippedTriangles ) {
                    triangleProjected = new Triangle(
                            MatrixMath.matrixMultiplyVectors(matProjection, triangleClipped.getP()),
                            triangleClipped.getT(),
                            triangleClipped.getColor(),
                            triangleClipped.getBrightness()
                    );

                    textureCorrection(triangleProjected);

                    offSetProjectedTriangle(triangleProjected, width, height);

                    trianglesProjected.add(triangleProjected);
                }
            }
        }

        numTrianglesDrawn = trianglesProjected.size();
        return trianglesProjected;
    }

    /**
     * This method calculates if one triangle is inside the screen or it isn't.
     * To do it, it goes over all the points which conform the triangle and if one
     * of them is outside the screen, returns false. Else, it returns true.
     * @param triangle the triangle to know if is inside the screen
     * @param width width screen
     * @param height height screen
     * @return return true if the triangle is inside the screen or false if one of its points is outside
     */
    private boolean isInsideScreen(Triangle triangle, int width, int height) {
        for ( Vec4df point : triangle.getP() ) {
            if ( point.getX() > width || point.getX() < 0 || point.getY() > height || point.getY() < 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method clips one triangle against the screen border specified as a parameter
     * @param triangle the triangle to clip
     * @param border the border: 0 to top, 1 to bottom, 2 to left and 3 for right
     * @param width the width screen
     * @param height the height screen
     * @return return an ArrayList with the triangles clipped from the first triangle pass as a parameter
     */
    private ArrayList<Triangle> clipTriangleAgainstBorder(Triangle triangle, int border, int width, int height) {
        switch ( border ) {
            default:
            case 0:
                return MatrixMath.triangleClipAgainstPlane(
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        triangle
                );
            case 1:
                return MatrixMath.triangleClipAgainstPlane(
                        new Vec4df(0.0f, (float) height - 1, 0.0f),
                        new Vec4df(0.0f, -1.0f, 0.0f),
                        triangle
                );
            case 2:
                return MatrixMath.triangleClipAgainstPlane(
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        triangle
                );
            case 3:
                return MatrixMath.triangleClipAgainstPlane(
                        new Vec4df((float) width - 1, 0.0f, 0.0f),
                        new Vec4df(-1.0f, 0.0f, 0.0f),
                        triangle
                );
        }
    }

    /**
     * This method clips all the projected and sorted triangles to fit inside the screen.
     * Really important, reduce the cost of drawing giant triangles, absolutely necessary.
     * @param width width screen
     * @param height height screen
     * @param triangles the triangles that are going to be chopped
     * @return return a queue with all triangles clipped
     */
    private ArrayList<Triangle> rasterizeTriangles(ArrayList<Triangle> triangles, int width, int height) {
        ArrayList<Triangle> finalTriangles = new ArrayList<>();
        LinkedList<Triangle> trianglesOutsideScreenQueue = new LinkedList<>();
        Triangle triangleToTest;

        for ( Triangle triangle : triangles ) {
            if ( isInsideScreen(triangle, width, height) ) {
                finalTriangles.add(triangle);
            } else {
                trianglesOutsideScreenQueue.add(triangle);
                int numNewTriangles = trianglesOutsideScreenQueue.size();

                for ( int i = 0; i < 4; i++ ) {
                    while ( numNewTriangles > 0 ) {
                        triangleToTest = trianglesOutsideScreenQueue.remove();
                        numNewTriangles--;

                        if ( isInsideScreen(triangleToTest, width, height) ) {
                            finalTriangles.add(triangleToTest);
                        } else {
                            trianglesOutsideScreenQueue.addAll(clipTriangleAgainstBorder(triangleToTest, i, width, height));
                        }

                    }
                    numNewTriangles = trianglesOutsideScreenQueue.size();
                }

                finalTriangles.addAll(trianglesOutsideScreenQueue);
                trianglesOutsideScreenQueue.clear();
            }
        }

        numTrianglesDrawn = finalTriangles.size();

        return finalTriangles;
    }

    /**
     * This method generalizes all the calculations needed
     * to get finally the triangles transformed, projected, sorted and rasterized
     * (and some other transformations needed... see the videos about 3D engine of Javidx9)
     * @param mesh the mesh to render
     * @return the triangles all transformed to get drawn on screen
     */
    private ArrayList<Triangle> getTrianglesToRender(Mesh mesh) {
        ArrayList<Triangle> transformedTriangles = transformTriangles(mesh);

        ArrayList<Triangle> projectedTriangles = projectTriangles(transformedTriangles, screenWidth, screenHeight);

        projectedTriangles.sort(
                (o1, o2) -> {
                    float medZ1 = (o1.getP()[0].getZ() + o1.getP()[1].getZ() + o1.getP()[2].getZ()) / 3.0f;
                    float medZ2 = (o2.getP()[0].getZ() + o2.getP()[1].getZ() + o2.getP()[2].getZ()) / 3.0f;
                    return Float.compare(medZ2, medZ1);
                }
        );

        return rasterizeTriangles(projectedTriangles, screenWidth, screenHeight);
    }

    /**
     * This method renders the mesh
     * @param mesh the mesh to render
     * @param texture the texture of the mesh
     */
    public void renderMesh(Mesh mesh, Image texture) {
        renderer3D.renderTriangles(getTrianglesToRender(mesh), texture);
    }

    public void renderMesh(Mesh mesh, int color) {
        renderer3D.renderTriangles(getTrianglesToRender(mesh), color);
    }

    public void renderMesh(Mesh mesh) {
        renderer3D.renderTriangles(getTrianglesToRender(mesh));
    }

    /**
     * This method renders a model, a model
     * contains a mesh of a 3D object and the texture
     * of the mesh
     * @param model the 3D model
     */
    public void renderModel(Model model) {
        renderMesh(model.getMesh(), model.getTexture());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Renderer3D getRenderer3D() {
        return renderer3D;
    }

    public int getNumTrianglesDrawn() {
        return numTrianglesDrawn;
    }

    public Camera getCameraObj() {
        return cameraObj;
    }

    public Mat4x4 getWorldMatrix() {
        return worldMatrix;
    }

    public Perspective getPerspective() {
        return perspective;
    }

    public Vec4df getLightDirection() {
        return lightDirection;
    }

    public void setCamera(Camera camera) {
        this.cameraObj = camera;
    }

    public void setMatView(Mat4x4 matView) {
        this.matView = matView;
    }

    public void setTransform(Mat4x4 transform) {
        worldMatrix = transform;
    }

    public void setPerspective(Perspective perspective) {
        this.perspective = perspective;
        matProjection = buildProjectionMatrix(screenWidth, screenHeight);
    }

    /**
     * This method sets the camera origin to the new origin pass as
     * a parameter and reset the matView. This two operations are needed
     * if is wanted to see a visible change in the final rendering.
     * In this way, the inner working of the matView is encapsulate
     * inside the pipeline class. Before, the user had to set
     * the matView when the camera object is modified
     * @param origin the new origin for the camera object
     */
    public void setCameraOrigin(Vec4df origin) {
        cameraObj.setOrigin(origin);
        setMatView(cameraObj.getMatView());
    }

    public void setLightDirection(Vec4df lightDirection) {
        this.lightDirection = lightDirection;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}

package engine3d.mesh;

import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;

import java.util.ArrayList;

/**
 * The mesh class
 * Contains the triangles of the object
 *
 * @class Mesh
 * @author Sergio Martí Torregrosa
 * @date 18/08/2020
 */
public class Mesh {

    /**
     * The triangles which forms the mesh
     */
    private ArrayList<Triangle> tris;

    /**
     * The constructor
     */
    public Mesh() {
        tris = new ArrayList<>();
    }

    /**
     * Constructor
     * @param tris the triangles what conform the mesh
     */
    public Mesh(ArrayList<Triangle> tris) {
        this.tris = copyTriangles(tris);
    }

    /**
     * Copy constructor
     * @param mesh the instance of the mesh to copy the values
     */
    public Mesh(Mesh mesh) {
        this(mesh.getTris());
    }

    /**
     * This method returns a copied array of triangles as the
     * triangles passed on the parameter
     * @param tris the triangles
     * @return the copied triangles, with different instances
     */
    private ArrayList<Triangle> copyTriangles(ArrayList<Triangle> tris) {
        ArrayList<Triangle> copyTriangles = new ArrayList<>();
        for ( Triangle t : tris ) {
            copyTriangles.add(new Triangle(t));
        }
        return copyTriangles;
    }

    /**
     * This method copy the @ArrayList of @Triangles from the
     * pipeline. It isn't the same instances what are inside the
     * mesh object. It has to be different, to not modify the
     * original mesh.
     * @param mesh the mesh to copy the triangles
     * @return return a copied triangles from the mesh triangles
     */
    public ArrayList<Triangle> copyMeshTriangles(Mesh mesh) {
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
     * This method shows the information of the triangles which conform the mesh
     */
    public void showInformation() {
        StringBuilder out = new StringBuilder();
        out.append("Number of Triangles: ").append(tris.size()).append('\n');
        for ( int i = 0; i < tris.size(); i++ ) {
            out.append("Triangle ").append(i).append('\n');
            for ( int j = 0; j < tris.get(i).getP().length; j++ ) {
                out.append(String.format("X: %f Y: %f Z: %f%n",
                        tris.get(i).getP()[j].getX(),
                        tris.get(i).getP()[j].getY(),
                        tris.get(i).getP()[j].getZ()));
            }
        }
        System.out.println(out);
    }

    // --------------

    /**
     * The getter for the triangles
     * @return the triangles
     */
    public ArrayList<Triangle> getTris() {
        return tris;
    }

    /**
     * The setter for the triangles
     * @param tris the new triangles
     */
    public void setTris(ArrayList<Triangle> tris) {
        this.tris = copyTriangles(tris);
    }

}

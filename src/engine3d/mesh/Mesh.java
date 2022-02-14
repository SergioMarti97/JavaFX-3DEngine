package engine3d.mesh;

import java.util.ArrayList;

/**
 * The mesh class
 * Contains the triangles and the texture image
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

    /**
     * This method shows the information of the triangles which conform the mesh
     */
    public void showInformation() {
        for ( int i = 0; i < tris.size() - 2; i++ ) {
            System.out.printf("Triangulo %d%n", i);
            for ( int j = 0; j < tris.get(i).getP().length; j++ ) {
                System.out.printf("X: %f Y: %f Z: %f%n",
                        tris.get(i).getP()[j].getX(),
                        tris.get(i).getP()[j].getY(),
                        tris.get(i).getP()[j].getZ());
            }
        }
    }

}

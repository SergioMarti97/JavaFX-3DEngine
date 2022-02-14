package engine3d.mesh;

import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class stores all the data of the .obj files
 *
 * .obj files store:
 * - "v" list of vertex (x, y, z, w)
 * - "vt" list of texture vertex (u, v, w)
 * - "vn" list of normal vertex (x, y, z)
 * - "vp" list of parameter space vertex (u, v, w)
 * - "f" list of faces
 *
 * Is similar to the mesh class
 */
public class MeshObject {

    /**
     * list of vertex
     */
    private final ArrayList<Vec4df> v;

    /**
     * list of texture vertex
     */
    private final ArrayList<Vec3df> vt;

    /**
     * list of normalized vertex
     */
    private final ArrayList<Vec4df> vn;

    /**
     * list of parameter space vertex
     */
    private final ArrayList<Vec3df> vp;

    /**
     * list of faces faces
     */
    private final ArrayList<Triangle> f;

    /**
     * Void constructor
     */
    public MeshObject() {
        v = new ArrayList<>();
        vt = new ArrayList<>();
        vn = new ArrayList<>();
        vp = new ArrayList<>();
        f = new ArrayList<>();
    }

    public Triangle buildTriangleNonTextured(String[] splitLine) {
        int[] face = new int[3];
        for ( int i = 1; i < splitLine.length; i++ ) {
            String[] numVertex = splitLine[i].split("//");
            face[i - 1] = Integer.parseInt(numVertex[0]);
        }
        return new Triangle(new Vec4df[] { v.get(face[0] - 1), v.get(face[1] - 1), v.get(face[2] - 1) });
    }

    public Triangle buildTriangleTextured(String[] splitLine) {
        int[] facePoint = new int[4];
        int[] faceTexture = new int[4];
        for ( int i = 1; i < splitLine.length; i++ ) {
            String[] splitNumber = splitLine[i].split("/");
            facePoint[i - 1] = Integer.parseInt(splitNumber[0]);
            faceTexture[i - 1] = Integer.parseInt(splitNumber[1]);
        }
        return new Triangle(new Vec4df[] { v.get(facePoint[0] - 1), v.get(facePoint[1] - 1), v.get(facePoint[2] - 1) },
                new Vec3df[] { vt.get(faceTexture[0] - 1), vt.get(faceTexture[1] - 1), vt.get(faceTexture[2] - 1) });
    }

    /**
     * This method of the Mesh class loads a ".obj" file to understandable data for
     * the classes of the 3D engine. This allows to work with outer 3D models.
     * @param path the path where is the ".obj" file
     * @return return true if the load was fine, or false if it can't load the file
     */
    public boolean load(String path, boolean hasTexture) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            String line = "";

            while (line != null) {
                String[] splitLine = line.split(" ");

                float x, y, z;
                switch (splitLine[0]) {
                    case "v":
                        x = Float.parseFloat(splitLine[1]);
                        y = Float.parseFloat(splitLine[2]);
                        z = Float.parseFloat(splitLine[3]);
                        v.add(new Vec4df(x, y, z));
                        break;
                    case "vt":
                        x = Float.parseFloat(splitLine[1]);
                        y = 1 - Float.parseFloat(splitLine[2]);
                        vt.add(new Vec3df(x, y));
                        break;
                    case "vn":
                        x = Float.parseFloat(splitLine[1]);
                        y = Float.parseFloat(splitLine[2]);
                        z = Float.parseFloat(splitLine[3]);
                        vn.add(new Vec4df(x, y, z));
                        break;
                    case "vp":
                        x = Float.parseFloat(splitLine[1]);
                        y = Float.parseFloat(splitLine[2]);
                        vp.add(new Vec3df(x, y));
                        break;
                    case "f":
                        if (!v.isEmpty()) {
                            if (!hasTexture) {
                                f.add(buildTriangleNonTextured(splitLine));
                            } else {
                                if (!vt.isEmpty()) {
                                    f.add(buildTriangleTextured(splitLine));
                                }
                            }
                        }
                        break;
                }

                line = bf.readLine();
            }
            bf.close();
            return true;
        } catch ( NullPointerException e ) {
            System.out.println("The mesh path is null");
            e.printStackTrace();
            return false;
        } catch ( FileNotFoundException e ) {
            System.out.printf("The file at %s is not found%n", path);
            e.printStackTrace();
            return false;
        } catch ( IOException e ) {
            System.out.printf("The file at %s could not be read%n", path);
            e.printStackTrace();
            return false;
        }
    }

    public Mesh getMesh() {
        return new Mesh(f);
    }


}

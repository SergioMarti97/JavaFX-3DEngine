package engine3d.mesh;

import engine3d.material.Material;
import engine3d.utils.IOUtils;
import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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
 *
 * @see "https://github.com/mokiat/java-data-front"
 * @see "https://github.com/seanrowens/oObjLoader"
 */
public class ObjReader {

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
     * the material file name
     * the extension for this file is ".mtl"
     */
    private String materialFileName;

    /**
     * The meshes
     */
    private ArrayList<MeshObject> o;

    /**
     * The materials
     */
    private HashMap<String, Material> materials;

    /**
     * Void constructor
     */
    public ObjReader() {
        v = new ArrayList<>();
        vt = new ArrayList<>();
        vn = new ArrayList<>();
        vp = new ArrayList<>();
        f = new ArrayList<>();

        o = new ArrayList<>();
        materials = new HashMap<>();
    }

    // ---------------------------------------------------

    /**
     * This method builds a non textured triangle
     */
    public Triangle buildTriangleNonTextured(String[] splitLine) {
        int[] face = new int[3];
        for ( int i = 1; i < splitLine.length; i++ ) {
            String[] numVertex = splitLine[i].split("//");
            face[i - 1] = Integer.parseInt(numVertex[0]);
        }
        return new Triangle(new Vec4df[] { v.get(face[0] - 1), v.get(face[1] - 1), v.get(face[2] - 1) });
    }

    /**
     * This method builds a textured triangle
     */
    public Triangle buildTriangleTextured(String[] splitLine) {
        int[] v = new int[4];
        int[] vt = new int[4];
        int[] vn = new int[4];
        boolean hasNormals = false;
        for ( int i = 1; i < splitLine.length; i++ ) {
            if (splitLine[i].matches("[0-9]+/[0-9]+/[0-9]+")) { // formato: v/vt/vn
                String[] splitNumber = splitLine[i].split("/");
                v[i - 1] = Integer.parseInt(splitNumber[0]);
                vt[i - 1] = Integer.parseInt(splitNumber[1]);
                vn[i - 1] = Integer.parseInt(splitNumber[2]);
                hasNormals = true;
            }
            if (splitLine[i].matches("[0-9]+//[0-9]+")) { // formato: v//vt (espero que sea así...)
                String[] splitNumber = splitLine[i].split("//");
                v[i - 1] = Integer.parseInt(splitNumber[0]);
                vt[i - 1] = Integer.parseInt(splitNumber[1]);
                hasNormals = false;
            }
            if (splitLine[i].matches("[0-9]+/[0-9]+")) { // formato: v/vt (espero que sea así...)
                String[] splitNumber = splitLine[i].split("/");
                v[i - 1] = Integer.parseInt(splitNumber[0]);
                vt[i - 1] = Integer.parseInt(splitNumber[1]);
                hasNormals = false;
            }
        }
        if (!hasNormals) {
            return new Triangle(
                    new Vec4df[]{this.v.get(v[0] - 1), this.v.get(v[1] - 1), this.v.get(v[2] - 1)},
                    new Vec3df[]{this.vt.get(vt[0] - 1), this.vt.get(vt[1] - 1), this.vt.get(vt[2] - 1)});
        } else {
            if (this.vn.size() <  vn[0] - 1 || this.vn.size() < vn[1] - 1 || this.vn.size() < vn[2] - 1) {
                return new Triangle(
                        new Vec4df[]{this.v.get(v[0] - 1), this.v.get(v[1] - 1), this.v.get(v[2] - 1)},
                        new Vec3df[]{this.vt.get(vt[0] - 1), this.vt.get(vt[1] - 1), this.vt.get(vt[2] - 1)},
                        new Vec4df[]{this.vn.get(vn[0] - 1), this.vn.get(vn[1] - 1), this.vn.get(vn[2] - 1)});
            } else if (this.vn.size() > 0) {
                return new Triangle(
                        new Vec4df[]{this.v.get(v[0] - 1), this.v.get(v[1] - 1), this.v.get(v[2] - 1)},
                        new Vec3df[]{this.vt.get(vt[0] - 1), this.vt.get(vt[1] - 1), this.vt.get(vt[2] - 1)},
                        new Vec4df[]{this.vn.get(0), this.vn.get(0), this.vn.get(0)});
            } else {
                return new Triangle(
                        new Vec4df[]{this.v.get(v[0] - 1), this.v.get(v[1] - 1), this.v.get(v[2] - 1)},
                        new Vec3df[]{this.vt.get(vt[0] - 1), this.vt.get(vt[1] - 1), this.vt.get(vt[2] - 1)});
            }
        }

    }

    /**
     * This method of the Mesh class loads a ".obj" file to understandable data for
     * the classes of the 3D engine. This allows to work with outer 3D models.
     *
     * todo load method must work with paths from resources and complete paths
     *
     * @param path the path where is the ".obj" file
     * @return return true if the load was fine, or false if it can't load the file
     */
    public boolean loadObj(String path, boolean hasTexture) {
        try {
            InputStreamReader streamReader = new InputStreamReader(IOUtils.getFileFromResource(path), StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(streamReader);
            String line = "";

            while (line != null) {
                if (line.length() > 1) {
                    line = line.replaceAll(" +", " ");
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
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method reads all the information of the obj file
     * @param path obj file
     * @param hasTexture if the meshes have texture
     */
    public boolean readObjFile(String path, boolean hasTexture) {
        try {
            InputStreamReader streamReader = new InputStreamReader(IOUtils.getFileFromResource(path), StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(streamReader);
            String line = "";

            MeshObject o = null;

            while (line != null) {
                if (line.length() > 1) {
                    line = line.replaceAll(" +", " ");
                    String[] splitLine = line.split(" ");

                    float x, y, z;
                    switch (splitLine[0]) {
                        case "mtllib": // material file name and relative path location
                            String fileName = line.replaceAll("mtllib", "");
                            fileName = fileName.trim();
                            String[] splitPath = path.split("/");
                            StringBuilder p = new StringBuilder();
                            for (int i = 0; i < splitPath.length - 1; i++) {
                                p.append(splitPath[i]).append("/");
                            }
                            materialFileName = p + fileName;
                            break;
                        case "usemtl": // new material found
                            assert o != null;
                            o = new MeshObject(splitLine[1], new ArrayList<>());
                            o.setMaterialName(splitLine[1]);
                            this.o.add(o);
                            break;
                        case "o": // new object found
                            o = new MeshObject(splitLine[1], new ArrayList<>());
                            this.o.add(o);
                            break;
                        case "v":
                            x = Float.parseFloat(splitLine[1]);
                            y = Float.parseFloat(splitLine[2]);
                            z = Float.parseFloat(splitLine[3]);
                            v.add(new Vec4df(x, y, z));
                            break;
                        case "vt":
                            x = Float.parseFloat(splitLine[1]);
                            y = 1 - Float.parseFloat(splitLine[2]); // <- valido para los mapas de spyro
                            //y = Float.parseFloat(splitLine[2]);
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
                            if (o != null && !v.isEmpty()) {
                                if (!hasTexture) {
                                    o.getMesh().getTris().add(buildTriangleNonTextured(splitLine));
                                } else {
                                    if (!vt.isEmpty()) {
                                        o.getMesh().getTris().add(buildTriangleTextured(splitLine));
                                    }
                                }
                            }
                            break;
                    }
                }
                line = bf.readLine();
            }

            return true;
        } catch ( NullPointerException e ) {
            System.out.println("The mesh path is null");
            e.printStackTrace();
        } catch ( FileNotFoundException e ) {
            System.out.printf("The file at %s is not found%n", path);
            e.printStackTrace();
        } catch ( IOException e ) {
            System.out.printf("The file at %s could not be read%n", path);
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method reads all the information of the mtl file
     * @param path mtl file
     */
    public boolean readMtlFile(String path) {
        try {
            InputStreamReader streamReader = new InputStreamReader(IOUtils.getFileFromResource(path), StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(streamReader);
            String line = "";

            Material m = null;

            while (line != null) {
                if (line.length() > 1) {
                    line = line.replaceAll(" +", " ");
                    String[] splitLine = line.split(" ");

                    switch (splitLine[0]) {
                        case "newmtl":
                            m = new Material(splitLine[1]);
                            materials.put(m.getName(), m);
                            break;
                        case "Ns":
                            assert m != null;
                            m.setNs(Float.parseFloat(splitLine[1]));
                            break;
                        case "Ka":
                            assert m != null;
                            m.setKa(new Vec3df(
                                    Float.parseFloat(splitLine[1]),
                                    Float.parseFloat(splitLine[2]),
                                    Float.parseFloat(splitLine[3])
                            ));
                            break;
                        case "Kd":
                            assert m != null;
                            m.setKd(new Vec3df(
                                    Float.parseFloat(splitLine[1]),
                                    Float.parseFloat(splitLine[2]),
                                    Float.parseFloat(splitLine[3])
                            ));
                            break;
                        case "Ks":
                            assert m != null;
                            m.setKs(new Vec3df(
                                    Float.parseFloat(splitLine[1]),
                                    Float.parseFloat(splitLine[2]),
                                    Float.parseFloat(splitLine[3])
                            ));
                            break;
                        case "Ke":
                            assert m != null;
                            m.setKe(new Vec3df(
                                    Float.parseFloat(splitLine[1]),
                                    Float.parseFloat(splitLine[2]),
                                    Float.parseFloat(splitLine[3])
                            ));
                            break;
                        case "Ni":
                            assert m != null;
                            m.setNi(Float.parseFloat(splitLine[1]));
                            break;
                        case "d":
                            assert m != null;
                            m.setD(Float.parseFloat(splitLine[1]));
                            break;
                        case "Tr":
                            assert m != null;
                            m.setD(1 - Float.parseFloat(splitLine[1]));
                            break;
                        case "illum":
                            assert m != null;
                            m.setIllum(Float.parseFloat(splitLine[1]));
                            break;
                    }

                    if (splitLine[0].matches("map_Kd")) {
                        assert m != null;
                        String[] splitPath = path.split("/");
                        m.setImgName("/" + splitPath[1] + "/" + splitLine[1]);
                    }

                }
                line = bf.readLine();
            }
            bf.close();
            return true;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean load(String path) {
        if (readObjFile(path, true)) {
            if (readMtlFile(materialFileName)) {

                LinkedHashMap<String, Material> m = materials.entrySet().stream().sorted((o1, o2) -> {
                    int n1 = Integer.parseInt(o1.getKey().replaceAll("\\D+", "").trim());
                    int n2 = Integer.parseInt(o2.getKey().replaceAll("\\D+","").trim());
                    return Integer.compare(n1, n2);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                for (MeshObject meshObject : o) {
                    meshObject.setMaterial(m.get(meshObject.getMaterialName()));
                }

                return true;
            }
        }
        return false;
    }

    public ArrayList<MeshObject> getObjects() {
        return o;
    }

}

package engine3d.mesh;

import java.util.ArrayList;

/**
 * This class contains the mesh and the
 * texture image of a model 3d
 *
 * A model can be a sum of meshes
 * The meshes can have multiple textures
 * The textures vertex information is stored in
 * the .obj file, but the information about the
 * material and the texture image is stored on
 * the .mtl file
 *
 * @class Model
 * @author Sergio Martí Torregrosa
 * @date 16/11/2020
 */
public class Model {

    private String name;

    private ArrayList<MeshObject> o;

    public Model(ArrayList<MeshObject> o) {
        this.o = o;
    }

    public Model(String name, ArrayList<MeshObject> o) {
        this.name = name;
        this.o = o;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MeshObject> getO() {
        return o;
    }

    public void setO(ArrayList<MeshObject> o) {
        this.o = o;
    }

}

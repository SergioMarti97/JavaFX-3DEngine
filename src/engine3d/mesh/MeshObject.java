package engine3d.mesh;

import engine3d.material.Material;

import java.util.ArrayList;

/**
 * This class represents a mesh object
 * stores the name, the mesh and the material of the object
 */
public class MeshObject {

    private String name;

    private Mesh mesh;

    private String materialName;

    private Material material;

    public MeshObject(String name, Mesh mesh) {
        this.name = name;
        this.mesh = mesh;
    }

    public MeshObject(String name, ArrayList<Triangle> tris) {
        this(name, new Mesh(tris));
    }

    @Override
    public String toString() {
        return "Name: '" + name + '\n' +
                "Number of triangles: " + mesh.getTris().size() + '\n' +
                "Material name: " + materialName + '\n' +
                "Material: " + material;
    }

    // -----------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

}

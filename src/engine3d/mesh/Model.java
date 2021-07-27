package engine3d.mesh;

import olcPGEApproach.gfx.images.Image;

/**
 * This class contains the mesh and the
 * texture image of a model 3d
 *
 * @class Model
 * @author Sergio Martí Torregrosa
 * @date 16/11/2020
 */
public class Model {

    /**
     * The mesh of the model
     */
    private Mesh mesh;

    /**
     * The texture image of the model
     */
    private Image texture;

    public Model(Mesh mesh, Image texture) {
        this.mesh = mesh;
        this.texture = texture;
    }

    ////////////////////////////////////////////////////////////

    public Mesh getMesh() {
        return mesh;
    }

    public Image getTexture() {
        return texture;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

}

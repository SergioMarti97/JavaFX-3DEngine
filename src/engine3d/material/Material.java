package engine3d.material;

import engine3d.vectors.Vec3df;
import olcPGEApproach.gfx.images.Image;

public class Material {

    /**
     * The name of the material
     */
    private String name;

    /**
     * The name of the image (texture)
     */
    private String imgName;

    /**
     * The texture of this material
     */
    private Image texture;

    // --- parametros que no conocemos su uso --- //

    /**
     * Ambient color
     */
    private Vec3df ka;

    /**
     * Diffuse color
     */
    private Vec3df kd;

    /**
     * Specular color
     */
    private Vec3df ks;

    /**
     * Specular exponent
     * ranges between 0 and 1000
     */
    private float ns;

    private Vec3df ke;

    /**
     * Optical density
     * range from 0.001 to 10
     * 1.0 means that light does not bend as it passes through an object
     * Glass has an index of refraction of about 1.5
     */
    private float ni;

    /**
     * transparent
     */
    private float d;

    /**
     * illumination
     *
     * 0. Color on and Ambient off
     * 1. Color on and Ambient on
     * 2. Highlight on
     * 3. Reflection on and Ray trace on
     * 4. Transparency: Glass on, Reflection: Ray trace on
     * 5. Reflection: Fresnel on and Ray trace on
     * 6. Transparency: Refraction on, Reflection: Fresnel off and Ray trace on
     * 7. Transparency: Refraction on, Reflection: Fresnel on and Ray trace on
     * 8. Reflection on and Ray trace off
     * 9. Transparency: Glass on, Reflection: Ray trace off
     * 10. Casts shadows onto invisible surfaces
     */
    private float illum;

    /**
     * Constructor
     * @param name name of the material
     */
    public Material(String name) {
        this.name = name;
    }

    /**
     * This method loads the image of the texture
     */
    public void loadTexture() {
        if (imgName != null) {
            texture = new Image(imgName);
        }
    }

    @Override
    public String toString() {
        String out = "";
        if (name != null) {
            out += "name: " + name + '\n';
        }
        if (imgName != null) {
            out += "img: " + imgName + '\n';
        }
        if (ka != null) {
            out += "Ka: " + ka + '\n';
        }
        if (kd != null) {
            out += "Kd: " + kd + '\n';
        }
        if (ks != null) {
            out += "Ks: " + ks + '\n';
        }
        out += "Ns: " + ns + '\n';
        if (ke != null) {
            out += "Ke: " + ke.toString() + '\n';
        }
        out += "Ni: " + ni + '\n';
        out += "d: " + d + '\n';
        out += "illum: " + illum + '\n';
        return out;
    }

    // -------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
        loadTexture();
    }

    public float getNs() {
        return ns;
    }

    public void setNs(float ns) {
        this.ns = ns;
    }

    public Vec3df getKa() {
        return ka;
    }

    public void setKa(Vec3df ka) {
        this.ka = ka;
    }

    public Vec3df getKd() {
        return kd;
    }

    public void setKd(Vec3df kd) {
        this.kd = kd;
    }

    public Vec3df getKs() {
        return ks;
    }

    public void setKs(Vec3df ks) {
        this.ks = ks;
    }

    public Vec3df getKe() {
        return ke;
    }

    public void setKe(Vec3df ke) {
        this.ke = ke;
    }

    public float getNi() {
        return ni;
    }

    public void setNi(float ni) {
        this.ni = ni;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public float getIllum() {
        return illum;
    }

    public void setIllum(float illum) {
        this.illum = illum;
    }

    public Image getTexture() {
        return texture;
    }

    public void setTexture(Image texture) {
        this.texture = texture;
    }

}

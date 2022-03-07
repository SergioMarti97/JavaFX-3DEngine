package engine3d.mesh;

import engine3d.vectors.Vec3df;
import engine3d.vectors.Vec4df;
import olcPGEApproach.gfx.images.Image;

import java.util.ArrayList;

/**
 * This class contains methods for
 * make different kinds of meshes
 *
 * @author Sergio Martí Torregrosa
 * 16/11/2020
 */
public class MeshFactory {

    /**
     * This method build and returns a unit cube. This is always helpful with 3D debugging.
     * @return a unit 3D cube.
     */
    public static Mesh getUnitCube() {
        Mesh cube = new Mesh();

        ArrayList<Triangle> triangles = new ArrayList<>();

        // SOUTH
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // EAST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 0.0f, 1.0f)},
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // NORTH
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // WEST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // TOP
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // BOTTOM
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        cube.setTris(triangles);

        return cube;
    }

    /**
     * This method returns a plane
     * @return a flat plane
     */
    public static Mesh getFlat() {
        Mesh mesh = new Mesh();

        ArrayList<Triangle> triangles = new ArrayList<>();

        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        mesh.setTris(triangles);

        return mesh;
    }

    /**
     * This method builds the mesh of a
     * walls out. Only the east, west, top and bottom
     * of a unit cube
     * @return the out walls
     */
    public static Mesh getWallsOut() {
        Mesh mesh = new Mesh();

        ArrayList<Triangle> triangles = new ArrayList<>();

        // EAST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 0.0f, 1.0f)},
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // WEST
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // TOP
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(0.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(0.0f, 1.0f, 0.0f),
                        new Vec4df(1.0f, 1.0f, 1.0f),
                        new Vec4df(1.0f, 1.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        // BOTTOM
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(0.0f, 0.0f),
                        new Vec3df(1.0f, 0.0f)
                }));
        triangles.add(new Triangle(
                new Vec4df[] {
                        new Vec4df(1.0f, 0.0f, 1.0f),
                        new Vec4df(0.0f, 0.0f, 0.0f),
                        new Vec4df(1.0f, 0.0f, 0.0f)
                },
                new Vec3df[] {
                        new Vec3df(0.0f, 1.0f),
                        new Vec3df(1.0f, 0.0f),
                        new Vec3df(1.0f, 1.0f)
                }));

        mesh.setTris(triangles);

        return mesh;
    }

    /**
     * This method resumes all the the lines of
     * code what loads from a object file a mesh
     * and the texture of the model
     * @param meshPath the path where is the mesh
     * @return the model
     */
    public static Model getModel(String meshPath) {
        ObjReader reader = new ObjReader();
        reader.load(meshPath);
        return new Model(reader.getObjects());
    }

}

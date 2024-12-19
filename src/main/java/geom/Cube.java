package geom;

import graphics.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Cube {

    public enum Faces {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

    private final ArrayList<Triangle> triangles = new ArrayList<>();
    private final Vector3f position;
    private final int scale = 50;

    public Cube(int x, int y, int z, HashMap<Faces, BufferedImage> textures) {
        this.position = new Vector3f(x, -y, z);

        Vector3f offset = this.position;

        // Vector4f Coordinates with offset
        Vector4f v1 = new Vector4f(-scale + offset.x, -scale + offset.y, -scale + offset.z,1);
        Vector4f v2 = new Vector4f(scale + offset.x, -scale + offset.y, -scale + offset.z,1);
        Vector4f v3 = new Vector4f(scale + offset.x, scale + offset.y, -scale + offset.z,1);
        Vector4f v4 = new Vector4f(-scale + offset.x, scale + offset.y, -scale + offset.z,1);
        Vector4f v5 = new Vector4f(-scale + offset.x, -scale + offset.y, scale + offset.z,1);
        Vector4f v6 = new Vector4f(scale + offset.x, -scale + offset.y, scale + offset.z,1);
        Vector4f v7 = new Vector4f(scale + offset.x, scale + offset.y, scale + offset.z,1);
        Vector4f v8 = new Vector4f(-scale + offset.x, scale + offset.y, scale + offset.z,1);

        // UV Coordinates
        Point uv1 = new Point(0, 0);
        Point uv2 = new Point(1, 0);
        Point uv3 = new Point(1, 1);
        Point uv4 = new Point(0, 1);

        // Triangles that make up the cube
        triangles.add(new Triangle(v1, v2, v3, uv1, uv2, uv3, textures.get(Faces.BACK)));
        triangles.add(new Triangle(v1, v3, v4, uv1, uv3, uv4, textures.get(Faces.BACK)));

        triangles.add(new Triangle(v5, v6, v7, uv1, uv2, uv3, textures.get(Faces.FRONT)));
        triangles.add(new Triangle(v5, v7, v8, uv1, uv3, uv4, textures.get(Faces.FRONT)));

        triangles.add(new Triangle(v1, v5, v8, uv1, uv2, uv3, textures.get(Faces.RIGHT)));
        triangles.add(new Triangle(v1, v8, v4, uv1, uv3, uv4, textures.get(Faces.RIGHT)));

        triangles.add(new Triangle(v2, v6, v7, uv1, uv2, uv3, textures.get(Faces.LEFT)));
        triangles.add(new Triangle(v2, v7, v3, uv1, uv3, uv4, textures.get(Faces.LEFT)));

        triangles.add(new Triangle(v1, v2, v6, uv1, uv2, uv3, textures.get(Faces.TOP)));
        triangles.add(new Triangle(v1, v6, v5, uv1, uv3, uv4, textures.get(Faces.TOP)));

        triangles.add(new Triangle(v4, v3, v7, uv1, uv2, uv3, textures.get(Faces.BOTTOM)));
        triangles.add(new Triangle(v4, v7, v8, uv1, uv3, uv4, textures.get(Faces.BOTTOM)));
    }

    public void draw(BufferedImage img, double[] zBuffer, Camera cam) {
        Matrix4f cameraMatrix = cam.getProjectionView();
        for (Triangle t : triangles) {
            t.draw(cameraMatrix, img, zBuffer);
        }
    }
}

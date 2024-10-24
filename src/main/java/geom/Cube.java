package geom;

import graphics.Camera;
import graphics.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static graphics.Root.cam;

public class Cube {

    public enum Faces {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

    private final ArrayList<Triangle> triangles = new ArrayList<>();
    private final Vertex position;
    private final int scale = 50;

    public Cube(int x, int y, int z, HashMap<Faces, BufferedImage> textures) {
        this.position = new Vertex(x, -y, z);

        Vertex offset = this.position;

        // Vertex Coordinates with offset
        Vertex v1 = new Vertex(-scale + offset.x, -scale + offset.y, -scale + offset.z);
        Vertex v2 = new Vertex(scale + offset.x, -scale + offset.y, -scale + offset.z);
        Vertex v3 = new Vertex(scale + offset.x, scale + offset.y, -scale + offset.z);
        Vertex v4 = new Vertex(-scale + offset.x, scale + offset.y, -scale + offset.z);
        Vertex v5 = new Vertex(-scale + offset.x, -scale + offset.y, scale + offset.z);
        Vertex v6 = new Vertex(scale + offset.x, -scale + offset.y, scale + offset.z);
        Vertex v7 = new Vertex(scale + offset.x, scale + offset.y, scale + offset.z);
        Vertex v8 = new Vertex(-scale + offset.x, scale + offset.y, scale + offset.z);

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

    public void draw(Graphics2D g2, BufferedImage img, double[] zBuffer, Camera cam) {
        int width = img.getWidth();
        int height = img.getHeight();

        for (Triangle t : triangles) {
            t.draw(cam.getPerspective(), width, height, img, zBuffer);
        }
    }
}

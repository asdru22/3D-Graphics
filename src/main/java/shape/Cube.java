package shape;

import graphics.Triangle;
import graphics.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Cube {
    private final ArrayList<Triangle> triangles = new ArrayList<>();

    public Cube(BufferedImage texture) {
        // Vertex Coordinates
        Vertex v1 = new Vertex(-100, -100, -100);
        Vertex v2 = new Vertex(100, -100, -100);
        Vertex v3 = new Vertex(100, 100, -100);
        Vertex v4 = new Vertex(-100, 100, -100);
        Vertex v5 = new Vertex(-100, -100, 100);
        Vertex v6 = new Vertex(100, -100, 100);
        Vertex v7 = new Vertex(100, 100, 100);
        Vertex v8 = new Vertex(-100, 100, 100);

        // UV Coordinates
        Point uv1 = new Point(0, 0);
        Point uv2 = new Point(1, 0);
        Point uv3 = new Point(1, 1);
        Point uv4 = new Point(0, 1);

        // Triangles that make up the cube
        triangles.add(new Triangle(v1, v2, v3, uv1, uv2, uv3, texture)); // Faccia inferiore 1
        triangles.add(new Triangle(v1, v3, v4, uv1, uv3, uv4, texture)); // Faccia inferiore 2

        triangles.add(new Triangle(v5, v6, v7, uv1, uv2, uv3, texture)); // Faccia superiore 1
        triangles.add(new Triangle(v5, v7, v8, uv1, uv3, uv4, texture)); // Faccia superiore 2

        triangles.add(new Triangle(v1, v5, v8, uv1, uv2, uv3, texture)); // Faccia anteriore 1
        triangles.add(new Triangle(v1, v8, v4, uv1, uv3, uv4, texture)); // Faccia anteriore 2

        triangles.add(new Triangle(v2, v6, v7, uv1, uv2, uv3, texture)); // Faccia posteriore 1
        triangles.add(new Triangle(v2, v7, v3, uv1, uv3, uv4, texture)); // Faccia posteriore 2

        triangles.add(new Triangle(v1, v2, v6, uv1, uv2, uv3, texture)); // Faccia laterale destra 1
        triangles.add(new Triangle(v1, v6, v5, uv1, uv3, uv4, texture)); // Faccia laterale destra 2

        triangles.add(new Triangle(v4, v3, v7, uv1, uv2, uv3, texture)); // Faccia laterale sinistra 1
        triangles.add(new Triangle(v4, v7, v8, uv1, uv3, uv4, texture)); // Faccia laterale sinistra 2
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }
}

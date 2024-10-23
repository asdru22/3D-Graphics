package block;

import graphics.Triangle;
import graphics.Vertex;
import math.Matrix3D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Cube {

    public enum Faces {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

    private final ArrayList<Triangle> triangles = new ArrayList<>();
    private final Vertex position;
    private final int scale = 100;
    public Cube(Vertex Position, HashMap<Faces,BufferedImage> textures) {
        this.position = Position;

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

    public void draw(Graphics2D g2, double width, double height, Matrix3D transform) {
        BufferedImage img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);
        for (Triangle t : triangles) {
            Vertex v1 = transform.transform(t.v1);
            v1.x += width / 2;
            v1.y += height / 2;
            Vertex v2 = transform.transform(t.v2);
            v2.x += width / 2;
            v2.y += height / 2;
            Vertex v3 = transform.transform(t.v3);
            v3.x += width / 2;
            v3.y += height / 2;

            Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
            Vertex norm = new Vertex(
                    ab.y * ac.z - ab.z * ac.y,
                    ab.z * ac.x - ab.x * ac.z,
                    ab.x * ac.y - ab.y * ac.x
            );

            double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
            norm.x /= normalLength;
            norm.y /= normalLength;
            norm.z /= normalLength;

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
            double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                    double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                    double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;

                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                        int zIndex = y * img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            // Calcolo delle coordinate di texture utilizzando l'interpolazione
                            double texX = b1 * t.v1u.x + b2 * t.v2u.x + b3 * t.v3u.x;
                            double texY = b1 * t.v1u.y + b2 * t.v2u.y + b3 * t.v3u.y;
                            int texPixel = t.texture.getRGB((int) (texX * (t.texture.getWidth() - 1)),
                                    (int) (texY * (t.texture.getHeight() - 1)));
                            Color texColor = new Color(texPixel, true);
                            img.setRGB(x, y, texColor.getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }
        }
        g2.drawImage(img, 0, 0, null);
    }

}

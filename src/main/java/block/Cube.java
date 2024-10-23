package block;

import graphics.Triangle;
import graphics.Vertex;
import math.Matrix3D;
import math.Matrix4D;

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

    public void draw(Graphics2D g2, double width, double height) {
        BufferedImage img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        // Parametri per la proiezione prospettica
        double fov = 90.0;
        double aspectRatio = width / height;
        double near = 0.1;
        double far = 1000.0;
        Vertex position = new Vertex(100,100, 0);
        Vertex direction = new Vertex(0, 0, -1);

        // Matrice di proiezione prospettica
        Matrix4D perspectiveMatrix = createPerspectiveMatrix(fov, aspectRatio, near, far,position,direction);

        for (Triangle t : triangles) {
            // Trasformazione dei vertici del triangolo
            Vertex v1 = applyMatrix(perspectiveMatrix, t.v1);
            Vertex v2 = applyMatrix(perspectiveMatrix, t.v2);
            Vertex v3 = applyMatrix(perspectiveMatrix, t.v3);

            // Normalizzazione delle coordinate NDC (normalized device coordinates)
            v1 = normalizeToNDC(v1, width, height);
            v2 = normalizeToNDC(v2, width, height);
            v3 = normalizeToNDC(v3, width, height);

            // Calcolo della normale del triangolo
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

            // Estremi del bounding box del triangolo nel piano immagine
            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

            // Calcolo dell'area del triangolo
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
                            double texX = Math.max(0, Math.min(1, b1 * t.v1u.x + b2 * t.v2u.x + b3 * t.v3u.x));
                            double texY = Math.max(0, Math.min(1, b1 * t.v1u.y + b2 * t.v2u.y + b3 * t.v3u.y));
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

    // Funzione per applicare la matrice di proiezione prospettica
    private Vertex applyMatrix(Matrix4D matrix, Vertex vertex) {
        double x = vertex.x * matrix.m11 + vertex.y * matrix.m12 + vertex.z * matrix.m13 + matrix.m14;
        double y = vertex.x * matrix.m21 + vertex.y * matrix.m22 + vertex.z * matrix.m23 + matrix.m24;
        double z = vertex.x * matrix.m31 + vertex.y * matrix.m32 + vertex.z * matrix.m33 + matrix.m34;
        double w = vertex.x * matrix.m41 + vertex.y * matrix.m42 + vertex.z * matrix.m43 + matrix.m44;

        return new Vertex(x / w, y / w, z / w);
    }

    // Funzione per normalizzare le coordinate ai NDC
    private Vertex normalizeToNDC(Vertex vertex, double width, double height) {
        vertex.x = (vertex.x + 1) * 0.5 * width;
        vertex.y = (vertex.y + 1) * 0.5 * height;
        return vertex;
    }

    // Funzione per creare la matrice di proiezione prospettica
// Funzione per creare la matrice di proiezione prospettica con posizione e direzione
    private Matrix4D createPerspectiveMatrix(double fov, double aspectRatio, double near, double far, Vertex position, Vertex direction) {
        double tanFovOver2 = Math.tan(Math.toRadians(fov) / 2);

        // Matrice di proiezione prospettica
        Matrix4D perspectiveMatrix = new Matrix4D();
        perspectiveMatrix.m11 = 1 / (aspectRatio * tanFovOver2);
        perspectiveMatrix.m22 = 1 / tanFovOver2;
        perspectiveMatrix.m33 = -(far + near) / (far - near);
        perspectiveMatrix.m34 = -1;
        perspectiveMatrix.m43 = -(2 * far * near) / (far - near);
        perspectiveMatrix.m44 = 0;

        // Creazione di una matrice di visualizzazione usando la posizione e la direzione (camera look-at)
        Vertex up = new Vertex(0, 1, 0); // Vettore "up" canonico
        direction.normalize();
        Vertex right = crossProduct(up, direction);
        right.normalize();
        up = crossProduct(direction, right); // Ricalcola "up"

        // Matrice di visualizzazione
        Matrix4D viewMatrix = new Matrix4D();
        viewMatrix.m11 = right.x;
        viewMatrix.m12 = right.y;
        viewMatrix.m13 = right.z;
        viewMatrix.m14 = -dotProduct(right, position);

        viewMatrix.m21 = up.x;
        viewMatrix.m22 = up.y;
        viewMatrix.m23 = up.z;
        viewMatrix.m24 = -dotProduct(up, position);

        viewMatrix.m31 = direction.x;
        viewMatrix.m32 = direction.y;
        viewMatrix.m33 = direction.z;
        viewMatrix.m34 = -dotProduct(direction, position);

        // Combinazione delle due matrici (prospettica e vista)
        return perspectiveMatrix.multiply(viewMatrix);
    }

    // Prodotto vettoriale
    private Vertex crossProduct(Vertex a, Vertex b) {
        return new Vertex(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    // Prodotto scalare
    private double dotProduct(Vertex a, Vertex b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
}

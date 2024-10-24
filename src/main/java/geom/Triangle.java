package geom;

import graphics.Vertex;
import math.Matrix4D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Triangle {
    // Vertici del triangolo nello spazio 3D
    public final Vertex v1, v2, v3;
    // Vertici del triangolo nella mappa UV
    public final Point v1u, v2u, v3u;
    // Texture del triangolo
    public final BufferedImage texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3,
                    Point v1u, Point v2u, Point v3u, BufferedImage texture) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v1u = v1u;
        this.v2u = v2u;
        this.v3u = v3u;
        this.texture = texture;
    }

    public void draw(Matrix4D perspectiveMatrix, double width, double height, BufferedImage img, double[] zBuffer){
        // Trasformazione dei vertici del triangolo
        Vertex v1 = applyMatrix(perspectiveMatrix, this.v1);
        Vertex v2 = applyMatrix(perspectiveMatrix, this.v2);
        Vertex v3 = applyMatrix(perspectiveMatrix, this.v3);

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
                // se è dentro al triangolo
                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                    int zIndex = y * img.getWidth() + x;
                    if (zBuffer[zIndex] < depth) {
                        // Calcolo delle coordinate di texture utilizzando l'interpolazione
                        double texX = Math.max(0, Math.min(1, b1 * this.v1u.x + b2 * this.v2u.x + b3 * this.v3u.x));
                        double texY = Math.max(0, Math.min(1, b1 * this.v1u.y + b2 * this.v2u.y + b3 * this.v3u.y));
                        int texPixel = this.texture.getRGB((int) (texX * (this.texture.getWidth() - 1)),
                                (int) (texY * (this.texture.getHeight() - 1)));
                        Color texColor = new Color(texPixel, true);
                        img.setRGB(x, y, texColor.getRGB());
                        zBuffer[zIndex] = depth;
                    }
                }
            }
        }
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

}
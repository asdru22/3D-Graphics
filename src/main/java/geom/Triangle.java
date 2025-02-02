package geom;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;

public record Triangle(Vector3f v1, Vector3f v2, Vector3f v3, Point v1u, Point v2u, Point v3u, BufferedImage texture) {

    public void draw(Matrix4f perspective, Matrix4f view, BufferedImage img, double[] zBuffer) {
        // Apply perspective projection
        Vector3f v1 = transform(perspective, view, this.v1);
        Vector3f v2 = transform(perspective, view, this.v2);
        Vector3f v3 = transform(perspective, view, this.v3);


        // Calculate triangle normal
        Vector3f ab = new Vector3f(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
        Vector3f ac = new Vector3f(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
        Vector3f norm = new Vector3f(
                ab.y * ac.z - ab.z * ac.y,
                ab.z * ac.x - ab.x * ac.z,
                ab.x * ac.y - ab.y * ac.x
        );
        float normalLength = (float) Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
        norm.x /= normalLength;
        norm.y /= normalLength;
        norm.z /= normalLength;

        // Get the triangle's bounding box
        int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
        int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
        int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
        int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

        // Calculate the area of the triangle
        double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                // Check if we are inside the triangle
                if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                    double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                    int zIndex = y * img.getWidth() + x;
                    if (zBuffer[zIndex] < depth) {
                        // Calculate new coordinates using barycentric interpolation
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

    private Vector3f transform(Matrix4f perspective, Matrix4f view, Vector3f Vector3f) {
        return new Vector3f(Vector3f.x, Vector3f.y, Vector3f.z).
                mulPosition(perspective).mulPosition(view);
    }

}
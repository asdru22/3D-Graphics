
package geom;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.awt.*;
import java.awt.image.BufferedImage;

public record Triangle(Vector4f v1, Vector4f v2, Vector4f v3, Point v1u, Point v2u, Point v3u, BufferedImage texture) {

    public void draw(Matrix4f perspective, BufferedImage img, double[] zBuffer) {
        // Apply perspective projection
        Vector4f v1 = transform(perspective, this.v1);
        Vector4f v2 = transform(perspective, this.v2);
        Vector4f v3 = transform(perspective, this.v3);

        // Normalize homogeneous coordinates (divide by w)
        v1.div(v1.w);
        v2.div(v2.w);
        v3.div(v3.w);

        // Calculate triangle normal
        Vector4f ab = new Vector4f(v2).sub(v1);
        Vector4f ac = new Vector4f(v3).sub(v1);
        Vector4f norm = new Vector4f(
                ab.y * ac.z - ab.z * ac.y,
                ab.z * ac.x - ab.x * ac.z,
                ab.x * ac.y - ab.y * ac.x,
                0.0f
        );
        norm.normalize();

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

    private Vector4f transform(Matrix4f matrix, Vector4f vector) {
        return new Vector4f(vector).mul(matrix);
    }
}
package geom;

import math.Float2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Triangle {
    private Float2D v0, v1, v2;
    private Float2D uv0, uv1, uv2;
    private BufferedImage texture;

    public Triangle(Float2D v0, Float2D v1, Float2D v2, Float2D uv0,
                    Float2D uv1, Float2D uv2, BufferedImage texture) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.uv0 = uv0;
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.texture = texture;
    }

    public void draw(Graphics2D g) {
        // bounding box
        int xMin = (int) Math.floor(Math.min(v0.x, Math.min(v1.x, v2.x)));
        int xMax = (int) Math.ceil(Math.max(v0.x, Math.max(v1.x, v2.x)));
        int yMin = (int) Math.floor(Math.min(v0.y, Math.min(v1.y, v2.y)));
        int yMax = (int) Math.ceil(Math.max(v0.y, Math.max(v1.y, v2.y)));

        // compute the deltas that will be used for the horizontal and vertical steps to improve performance
        int deltaW0Col = (int) (v1.y - v2.y);
        int deltaW1Col = (int) (v2.y - v0.y);
        int deltaW2Col = (int) (v0.y - v1.y);
        int deltaW0Row = (int) (v2.x - v1.x);
        int deltaW1Row = (int) (v0.x - v2.x);
        int deltaW2Row = (int) (v1.x - v0.x);

        // area of the parallelogram
        float area = Float2D.edgeCross(v0, v1, v2);
        float bias0 = Float2D.isTopLeft(v1, v2) ? 0.0f : -0.0001f,
                bias1 = Float2D.isTopLeft(v0, v2) ? 0.0f : -0.0001f,
                bias2 = Float2D.isTopLeft(v0, v1) ? 0.0f : -0.0001f;

        // point at the top left of the bounding box
        Float2D p0 = new Float2D(xMin, yMin);
        int w0Row = (int) ((int) Float2D.edgeCross(v1, v2, p0) + bias0);
        int w1Row = (int) ((int) Float2D.edgeCross(v2, v0, p0) + bias1);
        int w2Row = (int) ((int) Float2D.edgeCross(v0, v1, p0) + bias2);

        for (int y = yMin; y <= yMax; y++) {
            float w0 = w0Row;
            float w1 = w1Row;
            float w2 = w2Row;

            for (int x = xMin; x <= xMax; x++) {
                boolean isInside = (w0 >= 0 && w1 >= 0 && w2 >= 0);

                if (isInside) {
                    // alpha, beta, gamma baricentric coords
                    float alpha = w0 / area;
                    float beta = w1 / area;
                    float gamma = w2 / area;

                    // Interpolazione delle coordinate di texture
                    float u = alpha * uv0.x + beta * uv1.x + gamma * uv2.x;
                    float v = alpha * uv0.y + beta * uv1.y + gamma * uv2.y;

                    // Coordinate di texture nella bitmap
                    int texX = (int) (u * (texture.getWidth() - 1));
                    int texY = (int) (v * (texture.getHeight() - 1));

                    // Preleva il colore dalla texture
                    int color = texture.getRGB(texX, texY);
                    g.setColor(new Color(color, true));

                    g.drawLine(x, y, x, y);
                }
                w0 += deltaW0Col;
                w1 += deltaW1Col;
                w2 += deltaW2Col;
            }
            w0Row += deltaW0Row;
            w1Row += deltaW1Row;
            w2Row += deltaW2Row;
        }
    }
}

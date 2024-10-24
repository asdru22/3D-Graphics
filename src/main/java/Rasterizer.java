import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Rasterizer extends JPanel {

    private final Float2D[] vertices = new Float2D[]{
            new Float2D(340, 340),
            new Float2D(380, 340),
            new Float2D(340, 380),
            new Float2D(380, 380)
    };
    private final Float2D[] texCoords = new Float2D[]{
            new Float2D(0, 0),
            new Float2D(1, 0),
            new Float2D(0, 1),
            new Float2D(1, 1)
    };
    private BufferedImage texture;
    private float angle = 0.0f;

    public Rasterizer(JFrame pane) {
        try {
            // Carica l'immagine dalla cartella delle risorse
            texture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/dirt.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.add(this, BorderLayout.CENTER);
        Timer timer = new Timer(1000 / 60, _ -> {
            angle += (float) Math.toRadians(1);
            repaint();
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // black background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        Float2D center = new Float2D((float) getWidth() / 2, (float) getHeight() / 2);
        // Ruota i vertici
        Float2D v0 = Float2D.rotate(vertices[0], center, angle);
        Float2D v1 = Float2D.rotate(vertices[1], center, angle);
        Float2D v2 = Float2D.rotate(vertices[2], center, angle);
        Float2D v3 = Float2D.rotate(vertices[3], center, angle);
        // Disegna i triangoli con la texture
        drawTexturedTriangle(g2, v0, v1, v2, texCoords[0], texCoords[1], texCoords[2]);
        drawTexturedTriangle(g2, v1, v3, v2, texCoords[1], texCoords[3], texCoords[2]);
    }

    private void drawTexturedTriangle(Graphics2D g, Float2D v0, Float2D v1, Float2D v2,
                                      Float2D uv0, Float2D uv1, Float2D uv2) {
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
        float bias0 = isTopLeft(v1, v2) ? 0.0f : -0.0001f,
                bias1 = isTopLeft(v0, v2) ? 0.0f : -0.0001f,
                bias2 = isTopLeft(v0, v1) ? 0.0f : -0.0001f;

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

    private boolean isTopLeft(Float2D start, Float2D end) {
        Float2D edge = Float2D.getEdge(start, end);
        boolean isTopEdge = edge.y == 0 && edge.x > 0;
        boolean isLeftEdge = edge.x == 0 && edge.y > 0;
        return isTopEdge || isLeftEdge;
    }
}
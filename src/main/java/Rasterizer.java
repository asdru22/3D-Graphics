import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Rasterizer extends JPanel {
    private Float2D[] vertices = new Float2D[]{
            new Float2D(340, 340),
            new Float2D(380, 340),
            new Float2D(340, 380),
            new Float2D(390, 390),
            new Float2D(375, 320)
    };

    private float angle = 0.0f;
    private Timer timer;

    public Rasterizer(JFrame pane) {
        pane.add(this, BorderLayout.CENTER);
        timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angle += (float) Math.toRadians(1); // Incrementare l'angolo di 1 grado in radianti
                repaint(); // Richiamare il metodo per ridisegnare il pannello
            }
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // black background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        Float2D center = new Float2D(getWidth() / 2, getHeight() / 2);
        Float2D v0 = Float2D.rotate(vertices[0], center, angle);
        Float2D v1 = Float2D.rotate(vertices[1], center, angle);
        Float2D v2 = Float2D.rotate(vertices[2], center, angle);
        Float2D v3 = Float2D.rotate(vertices[3], center, angle);
        Float2D v4 = Float2D.rotate(vertices[4], center, angle);

        Color[] colors = new Color[]{
                new Color(255, 0, 0),
                new Color(0, 255, 0),
                new Color(0, 0, 255)
        };

        triangleFill(g2, v0, v1, v2, colors);
        triangleFill(g2, v3, v2, v1, colors);
        triangleFill(g2, v4, v1, v0, colors);

    }

    private void triangleFill(Graphics2D g2, Float2D v0, Float2D v1, Float2D v2, Color[] color) {

        // bounding box
        int xMin = (int) Math.floor(Math.min(v0.x, Math.min(v1.x, v2.x)));
        int xMax = (int) Math.ceil(Math.max(v0.x, Math.max(v1.x, v2.x)));
        int yMin = (int) Math.floor(Math.min(v0.y, Math.min(v1.y, v2.y)));
        int yMax = (int) Math.ceil(Math.max(v0.y, Math.max(v1.y, v2.y)));

        // compute the deltas that will be used for the horizontal and
        // vertical steps to improve performance
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

        // topleft most point
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

                    int r = (int) (color[0].getRed() * alpha + color[1].getRed() * beta +
                            color[2].getRed() * gamma);
                    int g = (int) (color[0].getGreen() * alpha + color[1].getGreen() * beta +
                            color[2].getGreen() * gamma);
                    int b = (int) (color[0].getBlue() * alpha + color[1].getBlue() * beta +
                            color[2].getBlue() * gamma);

                    // Controllo dei limiti dei valori RGB
                    r = Math.min(255, Math.max(0, r));
                    g = Math.min(255, Math.max(0, g));
                    b = Math.min(255, Math.max(0, b));

                    g2.setColor(new Color(r, g, b));

                    g2.drawLine(x, y, x, y);
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
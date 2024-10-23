package graphics;

import math.Matrix3D;
import shape.Cube;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RootPanel extends JPanel {
    private final JSlider headingSlider = new JSlider(-180, 180, 0);
    private final JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
    private BufferedImage cogImage;

    public RootPanel(JFrame pane) {
        pane.add(headingSlider, BorderLayout.SOUTH);
        pane.add(pitchSlider, BorderLayout.EAST);
        pane.add(this, BorderLayout.CENTER);
        try {
            cogImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getResource("/cog.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        headingSlider.addChangeListener(_ -> this.repaint());
        pitchSlider.addChangeListener(_ -> this.repaint());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        Cube c = new Cube(cogImage);
        List<Triangle> tris = c.getTriangles();

        double heading = Math.toRadians(headingSlider.getValue());
        Matrix3D headingTransform = new Matrix3D(new double[]{
                Math.cos(heading), 0, -Math.sin(heading),
                0, 1, 0,
                Math.sin(heading), 0, Math.cos(heading)
        });
        double pitch = Math.toRadians(pitchSlider.getValue());
        Matrix3D pitchTransform = new Matrix3D(new double[]{
                1, 0, 0,
                0, Math.cos(pitch), Math.sin(pitch),
                0, -Math.sin(pitch), Math.cos(pitch)
        });
        Matrix3D transform = headingTransform.multiply(pitchTransform);

        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);
        double width = getWidth();
        double height = getWidth();

        for (Triangle t : tris) {
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
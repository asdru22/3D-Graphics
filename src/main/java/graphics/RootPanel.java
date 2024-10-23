package graphics;

import math.Matrix3D;
import shape.Cube;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

        Cube c = new Cube(new Vertex(0, 0, 0), cogImage);
        Cube c2 = new Cube(new Vertex(40, 50, 0), cogImage);

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

        c.draw(g2, getWidth(), getHeight(), transform);
        c2.draw(g2, getWidth(), getHeight(), transform);

    }
}
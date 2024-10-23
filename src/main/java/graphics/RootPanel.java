package graphics;

import block.GrassBlock;
import math.Matrix3D;
import block.Cube;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.List;

public class RootPanel extends JPanel {
    private final JSlider headingSlider = new JSlider(-180, 180, 0);
    private final JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);

    public RootPanel(JFrame pane) {
        pane.add(headingSlider, BorderLayout.SOUTH);
        pane.add(pitchSlider, BorderLayout.EAST);
        pane.add(this, BorderLayout.CENTER);

        headingSlider.addChangeListener(_ -> this.repaint());
        pitchSlider.addChangeListener(_ -> this.repaint());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        GrassBlock c = new GrassBlock(new Vertex(0, 0, 0));

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

    }
}
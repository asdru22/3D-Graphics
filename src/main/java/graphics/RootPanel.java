package graphics;

import block.GrassBlock;
import math.Matrix3D;

import javax.swing.*;
import java.awt.*;

public class RootPanel extends JPanel {
    private final Camera camera;

    public RootPanel(JFrame pane) {
        this.camera = new Camera(new Vertex(0, 10, -1), new Vertex(0, 0, 0), new Vertex(0, 1, 0),45); // Camera looking at the origin

        pane.add(this, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        GrassBlock c = new GrassBlock(new Vertex(0, 0, 0));

        // Get the camera's view matrix
        Matrix3D viewTransform = camera.getViewMatrix();

        // Draw the cube using the camera's view transformation
        c.draw(g2, getWidth(), getHeight(), viewTransform, camera);
    }
}

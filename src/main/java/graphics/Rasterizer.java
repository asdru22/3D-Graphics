package graphics;

import geom.Triangle;
import math.Float2D;

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
        Triangle t1 = new Triangle(v0, v1, v2, texCoords[0], texCoords[1], texCoords[2], texture);
        Triangle t2 = new Triangle(v1, v3, v2, texCoords[1], texCoords[3], texCoords[2], texture);
        t1.draw(g2);
        t2.draw(g2);
    }


}
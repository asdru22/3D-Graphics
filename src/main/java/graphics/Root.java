package graphics;

import block.Dirt;
import block.GrassBlock;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Root extends JPanel {

    public static Camera cam;

    public Root(JFrame pane) {
        pane.add(this, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Configurazione della camera
        cam = new Camera(getWidth(), getHeight(), new Vertex(0, 500, 1000));

        // Creazione del BufferedImage e del zBuffer
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        // Creazione e disegno di più cubi
        GrassBlock grass = new GrassBlock(0, 0, 0);
        grass.draw(g2, img, zBuffer, cam);

        Dirt dirt = new Dirt(0, -100, 0);
        dirt.draw(g2, img, zBuffer, cam);


        g2.drawImage(img, 0, 0, null);
    }
}
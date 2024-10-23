package graphics;

import block.GrassBlock;

import javax.swing.*;
import java.awt.*;

public class RootPanel extends JPanel {
    public RootPanel(JFrame pane) {
        pane.add(this, BorderLayout.CENTER);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        GrassBlock c = new GrassBlock(new Vertex(0, 0, 0));

        c.draw(g2, getWidth(), getHeight());
    }
}
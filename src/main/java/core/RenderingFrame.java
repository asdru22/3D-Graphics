package core;

import javax.swing.*;
import java.awt.*;

public class RenderingFrame extends JFrame {

    public RenderingFrame() {
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        RenderingPanel renderingPanel = new RenderingPanel(this);
        this.add(renderingPanel);

        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("3D Graphics");

        renderingPanel.startRenderingThread();
    }
}

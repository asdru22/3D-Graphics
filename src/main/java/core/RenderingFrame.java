package core;

import javax.swing.*;
import java.awt.*;

public class RenderingFrame extends JFrame {

    public RenderingFrame() {
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        this.setSize(400, 400);

        RenderingPanel renderingPanel = new RenderingPanel(this);
        this.add(renderingPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("3D Graphics");

        renderingPanel.startRenderingThread();
    }
}

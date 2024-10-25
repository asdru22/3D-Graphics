package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class RenderingFrame extends JFrame {

    private RenderingPanel panel;

    public RenderingFrame() {
        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());
        this.setSize(400, 400);

        panel = new RenderingPanel(this);
        this.add(panel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("3D Graphics");

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                onWindowResize();
            }
        });

        panel.startRenderingThread();
    }

    private void onWindowResize() {
        panel.updateCameraSize(getWidth(),getHeight());

    }
}

package core;

import graphics.Camera;
import io.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class RenderingPanel extends JPanel implements Runnable {

    final int TARGET_FPS = 60;
    private int FPS = TARGET_FPS;
    public InputHandler inputHandler = new InputHandler();
    private Thread renderingThread;
    private final RenderingFrame frame;
    private final RenderingScene scene;

    public RenderingPanel(RenderingFrame frame) {
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(inputHandler.keyHandler);
        this.addMouseMotionListener(inputHandler.mousePosHandler);
        this.addMouseListener(inputHandler.mouseListenerHandler);
        this.scene = new RenderingScene(frame.getWidth(),frame.getHeight(), inputHandler);
        this.frame = frame;
    }

    public void startRenderingThread() {
        renderingThread = new Thread(this);
        renderingThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());


        // Creazione del BufferedImage e del zBuffer
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        // Draw the scene
        scene.draw(img,zBuffer);

        g2.drawImage(img, 0, 0, null);
    }

    @Override
    public void run() {
        // TARGET_FPS implementation
        double drawInterval = 10e8 / TARGET_FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (renderingThread != null) {

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                mainLoop();
                delta--;
                drawCount++;
            }

            if (timer >= 10e8) {
                FPS = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        scene.update();
    }

    public void mainLoop() {
        frame.setTitle("3D Graphics (FPS: " + FPS + ")");
        // update information
        update();
        // draw the screen with updated information
        repaint();
    }

    public void updateCameraSize(int width, int height){
        Camera c = scene.getCamera();
        c.resize(width,height);
    }
}
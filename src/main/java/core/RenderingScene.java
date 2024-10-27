package core;

import block.Dirt;
import block.GrassBlock;
import geom.Cube;
import graphics.Camera;
import io.InputHandler;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderingScene {
    private final Camera camera;
    private final ArrayList<Cube> cubes = new ArrayList<>();
    private final InputHandler inputHandler;

    public RenderingScene(int width, int height, InputHandler inputHandler) {
        this.camera = new Camera(width, height, new Vector3f(0, 0, 100));
        this.inputHandler = inputHandler;
        this.cubes.add(new GrassBlock(0, 0, 0));
        this.cubes.add(new Dirt(0, -100, 0));
        this.cubes.add(new Dirt(0, -200, 0));
        this.cubes.add(new Dirt(0, -300, 0));
        this.cubes.add(new Dirt(100, -300, 0));
        this.cubes.add(new Dirt(-100, -300, 0));

    }

    public void update() {
        if (inputHandler.keyHandler.escapePressed) {
            System.exit(0);
        }
        if (inputHandler.keyHandler.upPressed) camera.moveForward(1);
        if (inputHandler.keyHandler.downPressed) camera.moveBack(1);
        if (inputHandler.keyHandler.leftPressed) camera.moveLeft(1);
        if (inputHandler.keyHandler.rightPressed) camera.moveRight(1);
        if (inputHandler.keyHandler.spacePressed) camera.moveUp(1);
        if (inputHandler.keyHandler.shiftPressed) camera.moveDown(1);

        double sensitivity = 0.5;
        int centerX = (int) (camera.width / 2);
        int centerY = (int) (camera.height / 2);

        try {
            Robot robot = new Robot();
            Point mousePos = MouseInfo.getPointerInfo().getLocation();
            int deltaX = mousePos.x - centerX;
            int deltaY = mousePos.y - centerY;

            if (deltaX != 0 || deltaY != 0) {
                camera.addHorizontalRotation(deltaY * sensitivity);
                camera.addVerticalRotation(-deltaX * sensitivity);
            }

            robot.mouseMove(centerX, centerY);

        } catch (AWTException e) {
            System.err.println("Mouse error");
        }
    }

    public void draw(BufferedImage img, double[] zBuffer) {
        // Update the camera in the draw method to ensure correct dimensions
        for (Cube cube : cubes) {
            cube.draw(img, zBuffer, camera);
        }
    }

    public Camera getCamera() {
        return camera;
    }

}

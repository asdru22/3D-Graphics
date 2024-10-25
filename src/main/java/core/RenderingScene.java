package core;

import block.Dirt;
import block.GrassBlock;
import geom.Cube;
import graphics.Camera;
import graphics.Vertex;
import io.InputHandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderingScene {
    private final Camera camera;
    private final ArrayList<Cube> cubes = new ArrayList<>();
    private final InputHandler inputHandler;

    public RenderingScene(int width, int height, InputHandler inputHandler) {
        this.camera = new Camera(width, height, new Vertex(0, 0, 1000));
        this.inputHandler = inputHandler;
        this.cubes.add(new GrassBlock(0, 0, 0));
        this.cubes.add(new Dirt(0, -100, 0));

    }

    public void update() {
        if(inputHandler.keyHandler.upPressed) camera.moveForward(1);
        if(inputHandler.keyHandler.downPressed) camera.moveBack(1);
        if(inputHandler.keyHandler.leftPressed) camera.moveLeft(1);
        if(inputHandler.keyHandler.rightPressed) camera.moveRight(1);
        if(inputHandler.keyHandler.spacePressed) camera.moveUp(1);
        if(inputHandler.keyHandler.shiftPressed) camera.moveDown(1);

    }

    public void draw(BufferedImage img, double[] zBuffer) {
        // Update the camera in the draw method to ensure correct dimensions
        for (Cube cube : cubes) {
            cube.draw(img, zBuffer, camera);
        }
    }

}

package core;

import block.Dirt;
import block.GrassBlock;
import geom.Cube;
import graphics.Camera;
import graphics.Vertex;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderingScene {
    private final Camera camera;
    private final ArrayList<Cube> cubes = new ArrayList<>();

    public RenderingScene(int width,int height) {

        this.camera = new Camera(width,height, new Vertex(0, 500, 1000));
        System.out.println("INIT"+camera.width+","+camera.height);
        this.cubes.add(new GrassBlock(0, 0, 0));
        this.cubes.add(new Dirt(0, -100, 0));

    }

    public void update() {

    }

    public void draw(BufferedImage img, double[] zBuffer) {
        // Update the camera in the draw method to ensure correct dimensions
        for (Cube cube : cubes) {
            cube.draw(img, zBuffer, camera);
        }
    }

}

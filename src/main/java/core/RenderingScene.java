package core;

import block.Dirt;
import block.GrassBlock;
import geom.Cube;
import graphics.Camera;
import graphics.Vertex;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderingScene {
    private final RenderingPanel renderingPanel;
    private Camera camera;
    private final ArrayList<Cube> cubes = new ArrayList<>();

    public RenderingScene(RenderingPanel renderingPanel) {
        this.renderingPanel = renderingPanel;
        this.camera = new Camera(renderingPanel.getWidth(), renderingPanel.getHeight(), new Vertex(0, 500, 1000));
        this.cubes.add(new GrassBlock(0, 0, 0));
        this.cubes.add(new Dirt(0, -100, 0));

    }

    public void update() {

    }

    public void draw(BufferedImage img, double[] zBuffer) {
        for (Cube cube : cubes) {
            cube.draw(img, zBuffer, camera);
        }
    }

}

package block;

import geom.Cube;
import graphics.Vertex;
import util.Texture;

import java.util.HashMap;

public class Dirt extends Cube {
    public Dirt(int x, int y, int z) {
        super(x, y, z, new HashMap<>() {{
            put(Faces.BOTTOM, Texture.getTexture("dirt"));
            put(Faces.LEFT, Texture.getTexture("dirt"));
            put(Faces.RIGHT, Texture.getTexture("dirt"));
            put(Faces.FRONT, Texture.getTexture("dirt"));
            put(Faces.BACK, Texture.getTexture("dirt"));
            put(Faces.TOP, Texture.getTexture("dirt"));
        }});
    }
}

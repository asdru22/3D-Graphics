package block;

import geom.Cube;
import graphics.Vertex;
import util.Texture;

import java.util.HashMap;

public class GrassBlock extends Cube {
    public GrassBlock(int x, int y, int z) {
        super(x, y, z, new HashMap<>() {{
            put(Faces.BOTTOM, Texture.getTexture("dirt"));
            put(Faces.LEFT, Texture.getTexture("grass_block_side"));
            put(Faces.RIGHT, Texture.getTexture("grass_block_side"));
            put(Faces.FRONT, Texture.getTexture("grass_block_side"));
            put(Faces.BACK, Texture.getTexture("grass_block_side"));
            put(Faces.TOP, Texture.getTexture("grass_block_top"));
        }});
    }
}

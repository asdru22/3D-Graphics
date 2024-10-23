import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Texture {
    public int[] pixels;
    private String loc;
    public final int SIZE;

    public Texture(String location, int size) {
        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try (InputStream is = getClass().getResourceAsStream(loc)) {
            if (is == null) {
                throw new IOException("File not found: " + loc);
            }
            BufferedImage image = ImageIO.read(is);
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            System.err.println("Error loading texture from " + loc);
            e.printStackTrace();
        }
    }

    public static Texture wood = new Texture("textures/dirt.png", 16);
    public static Texture brick = new Texture("textures/grass_block_side.png", 16);
    public static Texture bluestone = new Texture("textures/grass_block_top.png", 16);
    public static Texture stone = new Texture("textures/missing.png", 16);
}
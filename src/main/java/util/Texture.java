package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Texture{

public static BufferedImage getTexture(String path) {
    try {
        String resourcePath = String.format("/textures/%s.png", path);
        return ImageIO.read(Objects.requireNonNull(
                Texture.class.getResource(resourcePath), "Resource not found: " + resourcePath));
    } catch (NullPointerException | IOException e) {
        // Gestisce l'errore senza stamparlo nello stack trace
        return getTexture("missing"); // Ritorna la texture predefinita in caso di errore.
    }
    }
}

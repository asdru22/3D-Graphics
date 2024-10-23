package graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Triangle {
    // Vertici del triangolo nello spazio 3D
    public final Vertex v1, v2, v3;
    // Vertici del triangolo nella mappa UV
    public final Point v1u, v2u, v3u;
    // Texture del triangolo
    public final BufferedImage texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3,
                    Point v1u, Point v2u, Point v3u, BufferedImage texture) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v1u = v1u;
        this.v2u = v2u;
        this.v3u = v3u;
        this.texture = texture;
    }
}
package graphics;

public class Vertex {

    public double x, y, z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vertex v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public void normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        x /= length;
        y /= length;
        z /= length;
    }

    // Prodotto vettoriale
    public static Vertex crossProduct(Vertex a, Vertex b) {
        return new Vertex(
                a.y * b.z - a.z * b.y,
                a.z * b.x - a.x * b.z,
                a.x * b.y - a.y * b.x
        );
    }

    // Prodotto scalare
    public static double dotProduct(Vertex a, Vertex b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    // Funzione per normalizzare le coordinate ai NDC
    public void normalizeToScreen(double width, double height) {
        this.x = (this.x + 1) * 0.5 * width;
        this.y = (this.y + 1) * 0.5 * height;
    }

    @Override
    public String toString() {
        return "Vertex(x: " + x + ", y: " + y + ", z: " + z + ")";
    }
}

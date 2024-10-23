package graphics;

public class Vertex {

    public double x, y, z, w;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public Vertex(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    // Subtract two vertices (used to get direction vectors)
    public Vertex subtract(Vertex other) {
        return new Vertex(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    // Add two vertices (used for translations)
    public Vertex add(Vertex other) {
        return new Vertex(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    // Scalar multiplication
    public Vertex multiply(double scalar) {
        return new Vertex(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    // Normalize the vector (make its length 1)
    public Vertex normalize() {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        return new Vertex(this.x / length, this.y / length, this.z / length);
    }

    // Dot product of two vectors
    public double dot(Vertex other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Cross product of two vectors
    public Vertex cross(Vertex other) {
        return new Vertex(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    // Get the length/magnitude of the vector
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    // Override toString for easier debugging
    @Override
    public String toString() {
        return "Vertex(" + x + ", " + y + ", " + z + ")";
    }
}

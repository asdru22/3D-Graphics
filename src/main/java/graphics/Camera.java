package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    // Perspective projection parameters
    private final double fov = 90.0;
    private double aspectRatio;
    private final double near = 0.1;
    private final double far = 1000.0;
    public double width, height;

    public final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public final Vector3f Z_AXIS = new Vector3f(0, 0, 1);

    private Vertex position;
    private final Vertex direction = new Vertex(0, 0, -1);
    private Matrix4f perspectiveMatrix;
    private Matrix4f viewMatrix;

    public Camera(double width, double height, Vertex position) {
        this.position = position;
        this.aspectRatio = width / height;
        this.width = width;
        this.height = height;
        update();
    }

    public void update() {
        perspectiveMatrix = makeProjectionMatrix();
        viewMatrix = makeViewMatrix();

    }

    public Matrix4f makeProjectionMatrix() {

        float yScale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
        float xScale = (float) (yScale / aspectRatio);
        var frustrumLength = far - near;

        return new Matrix4f()
                .m00(xScale)
                .m11(yScale)
                .m22((float) -((far + near) / frustrumLength))
                .m23(-1)
                .m32((float) -((2 * near * far) / frustrumLength))
                .m33(0);
    }

    public Matrix4f makeViewMatrix() {
        var negatedPosition = new Vector3f((float) -position.x, (float) -position.y, (float) -position.z);

        return new Matrix4f()
                .identity()
                .rotate((float) Math.toRadians(direction.x), X_AXIS)
                .rotate((float) Math.toRadians(direction.y), Y_AXIS)
                .rotate((float) Math.toRadians(direction.z), Z_AXIS)
                .translate(negatedPosition);
    }

    public Matrix4f getPerspective() {
        return perspectiveMatrix;
    }
    public Matrix4f getView() {
        return viewMatrix;
    }

    public Vertex getPosition() {
        return position;
    }

    public void setPosition(Vertex position) {
        this.position = position;
        update();
    }

    public void moveForward(double amount) {
        this.position.z -= amount;
        update();
    }

    public void moveBack(double amount) {
        this.position.z += amount;
        update();

    }

    public void moveLeft(double amount) {
        this.position.x += amount;
        update();

    }

    public void moveRight(double amount) {
        this.position.x -= amount;
        update();

    }

    public void moveUp(double amount) {
        this.position.y -= amount;
        update();

    }

    public void moveDown(double amount) {
        this.position.y += amount;
        update();
    }

    public void addHorizontalRotation(double angle) {
        double radians = Math.toRadians(angle);
        direction.x += Math.sin(radians);

    }

    public void addVerticalRotation(double angle) {
        double radians = Math.toRadians(angle);
        direction.y += Math.sin(radians);
        update();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        aspectRatio = (double) width / height;
        update();
    }
}

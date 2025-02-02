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

    private Vector3f position;
    private final Vector3f direction = new Vector3f(0, 0, -1);
    private Matrix4f perspectiveMatrix;
    private Matrix4f viewMatrix;

    public Camera(double width, double height, Vector3f position) {
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
        var negatedPosition = new Vector3f(-position.x, -position.y, -position.z);

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

    public void moveForward(float amount) {
        this.position.z -= amount;
        update();
    }

    public void moveBack(float amount) {
        this.position.z += amount;
        update();

    }

    public void moveLeft(float amount) {
        this.position.x -= amount;
        update();

    }

    public void moveRight(float amount) {
        this.position.x += amount;
        update();

    }

    public void moveUp(float amount) {
        this.position.y -= amount;
        update();

    }

    public void moveDown(float amount) {
        this.position.y += amount;
        update();
    }

    public void addHorizontalRotation(double angle) {
        double radians = Math.toRadians(angle);
        direction.x += (float) Math.sin(radians);

    }

    public void addVerticalRotation(double angle) {
        double radians = Math.toRadians(angle);
        direction.y += (float) Math.sin(radians);
        update();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        aspectRatio = (double) width / height;
        update();
    }
}

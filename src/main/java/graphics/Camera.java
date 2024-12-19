package graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    // Perspective projection parameters
    private final float fov = 90.0f;
    private float aspectRatio;
    private final float near = 0.1f;
    private final float far = 100.0f;
    public double width, height;

    private Vector3f position;
    Vector3f target = new Vector3f(0, 0, 0);
    Vector3f up = new Vector3f(0, 1, 0);             // "Up" direction is Y-axis

    private Matrix4f projection;
    private Matrix4f view;

    public Camera(double width, double height, Vector3f position) {
        this.position = position;
        this.aspectRatio = (float) (width / height);
        this.width = width;
        this.height = height;
        update();
    }

    public void update() {
        projection = new Matrix4f().perspective(fov, aspectRatio, near, far);
        view = new Matrix4f().lookAt(position, target, up);
        Matrix4f projectionView = new Matrix4f();
        projection.mul(view, projectionView);
    }

    public Matrix4f getProjectionView() {
        return projection;
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
        target.x += (float) Math.sin(radians);

    }

    public void addVerticalRotation(double angle) {
        double radians = Math.toRadians(angle);
        target.y += (float) Math.sin(radians);
        update();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        aspectRatio =  (width / height);
        update();
    }
}

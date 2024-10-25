package graphics;

import math.Matrix4D;

public class Camera {
    // Perspective projection parameters
    private final double fov = 90.0;
    private double aspectRatio;
    private final double near = 0.1;
    private final double far = 1000.0;
    public double width, height;

    private Vertex position;
    private final Vertex direction = new Vertex(0, 0, -1);
    private Matrix4D perspectiveMatrix;

    public Camera(double width, double height, Vertex position) {
        this.position = position;
        this.aspectRatio = width / height;
        this.width = width;
        this.height = height;
        updatePerspective();
    }

    public void updatePerspective() {
        perspectiveMatrix = createPerspectiveMatrix();
    }

    private Matrix4D createPerspectiveMatrix() {
        double tanFovOver2 = Math.tan(Math.toRadians(fov) / 2);

        // Perspective projection matrix
        Matrix4D perspectiveMatrix = new Matrix4D();
        perspectiveMatrix.m11 = 1 / (aspectRatio * tanFovOver2);
        perspectiveMatrix.m22 = 1 / tanFovOver2;
        perspectiveMatrix.m33 = -(far + near) / (far - near);
        perspectiveMatrix.m34 = -1;
        perspectiveMatrix.m43 = -(2 * far * near) / (far - near);
        perspectiveMatrix.m44 = 0;

        // Create a view matrix using position and direction (camera look-at)
        Vertex up = new Vertex(0, -1, 0);
        Vertex dir = new Vertex(direction);
        dir.normalize();
        Vertex right = Vertex.crossProduct(up, direction);
        right.normalize();
        up = Vertex.crossProduct(direction, right);

        // View matrix
        Matrix4D viewMatrix = new Matrix4D();
        viewMatrix.m11 = right.x;
        viewMatrix.m12 = right.y;
        viewMatrix.m13 = right.z;
        viewMatrix.m14 = -Vertex.dotProduct(right, position);

        viewMatrix.m21 = up.x;
        viewMatrix.m22 = up.y;
        viewMatrix.m23 = up.z;
        viewMatrix.m24 = -Vertex.dotProduct(up, position);

        viewMatrix.m31 = direction.x;
        viewMatrix.m32 = direction.y;
        viewMatrix.m33 = direction.z;
        viewMatrix.m34 = -Vertex.dotProduct(direction, position);

        // Combine perspective and view matrix
        return perspectiveMatrix.multiply(viewMatrix);
    }

    public Matrix4D getPerspective() {
        return perspectiveMatrix;
    }

    public Vertex getPosition() {
        return position;
    }

    public void setPosition(Vertex position) {
        this.position = position;
        updatePerspective();
    }

    public void moveForward(double amount) {
        this.position.z -= amount;
        updatePerspective();
    }

    public void moveBack(double amount) {
        this.position.z += amount;
        updatePerspective();

    }

    public void moveLeft(double amount) {
        this.position.x += amount;
        updatePerspective();

    }

    public void moveRight(double amount) {
        this.position.x -= amount;
        updatePerspective();

    }

    public void moveUp(double amount) {
        this.position.y -= amount;
        updatePerspective();

    }

    public void moveDown(double amount) {
        this.position.y += amount;
        updatePerspective();
    }

    public void setHorizontalRotation(double angle){
        this.direction.x = (float) Math.sin(Math.toRadians(angle));
    }

    public void setVerticalRotation(double angle){
        this.direction.y = (float) Math.sin(Math.toRadians(angle));
        updatePerspective();
    }

    public void resize(int width, int height){
        this.width = width;
        this.height = height;
        aspectRatio = (double) width / height;
        updatePerspective();
    }
}

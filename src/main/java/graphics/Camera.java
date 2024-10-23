package graphics;

import math.Matrix3D;

public class Camera {
    public Vertex position;
    public Vertex direction; // This represents where the camera is looking
    public Vertex up; // This defines the 'up' direction for the camera
    public double fov; // Field of view

    public Camera(Vertex position, Vertex direction, Vertex up, double fov) {
        this.position = position;
        this.direction = direction;
        this.up = up;
        this.fov = fov;
    }

    // View matrix transformation (converts world coordinates to camera view coordinates)
    public Matrix3D getViewMatrix() {
        // Compute the view matrix using the position and direction of the camera.
        // This implements a full lookAt matrix.
        return Matrix3D.lookAt(position, direction, up);
    }

    // Projection matrix transformation (converts 3D coordinates to 2D screen coordinates)
    public Matrix3D getProjectionMatrix(double width, double height) {
        // Create a perspective projection matrix
        double aspectRatio = width / height;
        return Matrix3D.perspective(fov, aspectRatio, 0.1, 1000); // Adjust near and far planes as needed
    }
}

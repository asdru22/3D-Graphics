package graphics;

import math.Matrix4D;

public class Camera {
    // Parametri per la proiezione prospettica
    double fov = 90.0;
    double aspectRatio;
    double near = 0.1;
    double far = 1000.0;
    Vertex position;
    Vertex direction = new Vertex(0, -0.5, -1);
    Matrix4D perspectiveMatrix;
    public double width, height;

    public Camera(double width, double height, Vertex position) {
        this.position = position;
        this.aspectRatio = width / height;
        this.width = width;
        this.height = height;
        perspectiveMatrix = createPerspectiveMatrix(fov, aspectRatio, near, far, position, direction);
    }

    private Matrix4D createPerspectiveMatrix(double fov, double aspectRatio, double near, double far, Vertex position, Vertex direction) {
        double tanFovOver2 = Math.tan(Math.toRadians(fov) / 2);

        // Matrice di proiezione prospettica
        Matrix4D perspectiveMatrix = new Matrix4D();
        perspectiveMatrix.m11 = 1 / (aspectRatio * tanFovOver2);
        perspectiveMatrix.m22 = 1 / tanFovOver2;
        perspectiveMatrix.m33 = -(far + near) / (far - near);
        perspectiveMatrix.m34 = -1;
        perspectiveMatrix.m43 = -(2 * far * near) / (far - near);
        perspectiveMatrix.m44 = 0;

        // Creazione di una matrice di visualizzazione usando la posizione e la direzione (camera look-at)
        Vertex up = new Vertex(0, -1, 0); // Vettore "up" canonico
        direction.normalize();
        Vertex right = Vertex.crossProduct(up, direction);
        right.normalize();
        up = Vertex.crossProduct(direction, right); // Ricalcola "up"

        // Matrice di visualizzazione
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

        // Combinazione delle due matrici (prospettica e vista)
        return perspectiveMatrix.multiply(viewMatrix);
    }

    public Matrix4D getPerspective() {
        return perspectiveMatrix;
    }
}

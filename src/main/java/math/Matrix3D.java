package math;

import graphics.Vertex;

public class Matrix3D {
    double[] values;

    public Matrix3D(double[] values) {
        this.values = values;
    }

    public Matrix3D multiply(Matrix3D other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] +=
                            this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix3D(result);
    }

    // LookAt method for view matrix calculation (transforms world coordinates relative to camera)
    public static Matrix3D lookAt(Vertex eye, Vertex target, Vertex up) {
        Vertex zAxis = eye.subtract(target).normalize(); // forward
        Vertex xAxis = up.cross(zAxis).normalize();      // right
        Vertex yAxis = zAxis.cross(xAxis).normalize();   // up

        double[] matrix = new double[]{
                xAxis.x, yAxis.x, zAxis.x, 0,
                xAxis.y, yAxis.y, zAxis.y, 0,
                xAxis.z, yAxis.z, zAxis.z, 0,
                -xAxis.dot(eye), -yAxis.dot(eye), -zAxis.dot(eye), 1
        };

        return new Matrix3D(matrix);
    }

    // Perspective projection matrix
    public static Matrix3D perspective(double fov, double aspectRatio, double near, double far) {
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2.0);
        double rangeInv = 1.0 / (near - far);

        return new Matrix3D(new double[]{
                f / aspectRatio, 0,  0,                                0,
                0,               f,  0,                                0,
                0,               0,  (far + near) * rangeInv,          -1,
                0,               0,  near * far * rangeInv * 2,        0
        });
    }

    // Multiply a matrix by a vertex (apply transformation)
    public Vertex transform(Vertex v) {
        double x = v.x * values[0] + v.y * values[4] + v.z * values[8] + v.w * values[12];
        double y = v.x * values[1] + v.y * values[5] + v.z * values[9] + v.w * values[13];
        double z = v.x * values[2] + v.y * values[6] + v.z * values[10] + v.w * values[14];
        double w = v.x * values[3] + v.y * values[7] + v.z * values[11] + v.w * values[15];

        return new Vertex(x / w, y / w, z / w, w); // Perspective division
    }
}
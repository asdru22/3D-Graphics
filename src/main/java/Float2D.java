public class Float2D {
    public float x, y;
    public Float2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Float2D other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void sub(Float2D other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public static Float2D mul(Float2D a, float b) {
        return new Float2D(a.x * b, a.y * b);
    }

    public static Float2D div(Float2D a, float b) {
        return new Float2D(a.x / b, a.y / b);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        float len = length();
        x /= len;
        y /= len;
    }

    public static float dot(Float2D a, Float2D b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float cross(Float2D a, Float2D b) {
        return a.x * b.y - a.y * b.x;
    }

    public static float edgeCross(Float2D a, Float2D b, Float2D p){
        Float2D ab = new Float2D(b.x-a.x, b.y-a.y);
        Float2D ap = new Float2D(p.x-a.x, p.y-a.y);
        return cross(ab, ap);
    }

    public static Float2D getEdge(Float2D start, Float2D end){
        return new Float2D(end.x- start.x, end.y- start.y);
    }

    public static Float2D rotate(Float2D v, Float2D center, double angle){
        Float2D rot = new Float2D(0,0);
        Float2D vCopy = new Float2D(v.x, v.y);
        vCopy.x -= center.x;
        vCopy.y -= center.y;
        rot.x = (float) (vCopy.x * Math.cos(angle) - vCopy.y * Math.sin(angle));
        rot.y = (float) (vCopy.x * Math.sin(angle) + vCopy.y * Math.cos(angle));
        rot.x += center.x;
        rot.y += center.y;
        return rot;
    }

    @Override
    public String toString() {
        return "Float2D(" + x + ", " + y + ")";
    }

}

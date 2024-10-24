public class Test {
    public static void main(String[] args) {
        Float2D vertex = new Float2D(40, 40);
        Float2D center = new Float2D(443, 281);
        double angle = Math.toRadians(4.0);
        System.out.println(
                Float2D.rotate(vertex, center, angle)
        );
    }
}

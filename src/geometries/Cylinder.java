package geometries;

public class Cylinder extends Tube {
    private double height;
    public Cylinder(double height, Ray axis) {
        super(axis);
        this.height = height;
    }

    public Vector getNormal(Point point) {
        return null;
    }
}

package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Cylinder extends Tube {
    private final double height;
    public Cylinder(double height, Ray axis, double radius) {
        super(axis, radius);
        this.height = height;
    }

    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
}

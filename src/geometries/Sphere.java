package geometries;

import primitives.Point;
import primitives.Vector;

public class Sphere extends RadialGeometry {
    private Point center;

    public Sphere(Point center) {
        this.center = center;
    }
    public Vector getNormal(Point point) {
        return null;
    }
}

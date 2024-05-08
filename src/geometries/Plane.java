package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry{
   private final Point q;
   private final Vector normal;

    public Plane(Point p1, Point p2, Point p3) {
        this.normal = null;
        this.q = p1;
    }

    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector getNormal(Point point) {
        return normal;
    }


}

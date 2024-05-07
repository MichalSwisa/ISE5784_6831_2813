package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane {
    final Point q;
    final Vector normal;

    public Plane(Point p1, Point p2, Point p3) {
        this.q = p1;

        Vector u = p2.subtract(p1);
        Vector v = p3.subtract(p1);
        Vector w = u.crossProduct(v);

        this.normal = w.normalize();
    }

    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector getNormal(Point point) {
        return null;
    }


}

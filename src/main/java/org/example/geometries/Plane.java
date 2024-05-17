package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Vector;

/**
 * Represents an infinite plane in three-dimensional space.
 * Implements the Geometry interface to provide a method for obtaining the normal vector to the plane at a given point.
 */
public class Plane implements Geometry {
    /**
     * A point on the plane.
     */
    private final Point q;
    /**
     * The normal vector to the plane.
     */
    private final Vector normal;

    /**
     * Constructs a plane from three non-collinear points.
     * Note: This constructor currently does not calculate the normal vector.
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @param p3 The third point.
     */
    public Plane(Point p1, Point p2, Point p3) {
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        this.normal = v1.crossProduct(v2).normalize();
        this.q = p1;
    }

    /**
     * Constructs a plane with a given point on the plane and a normal vector.
     *
     * @param q      A point on the plane.
     * @param normal The normal vector to the plane.
     */
    public Plane(Point q, Vector normal) {
        this.q = q;
        this.normal = normal.normalize();
    }

    /**
     * Returns the normal vector to the plane.
     *
     * @return The normal vector to the plane.
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * Returns the normal vector to the plane at the given point.
     * Note: This method returns the same normal vector for any given point on the plane.
     *
     * @param point The point on the plane (ignored).
     * @return The normal vector to the plane.
     */
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }


}

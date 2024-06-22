package org.example.geometries;
import java.util.stream.Collectors;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;

import static org.example.primitives.Util.alignZero;
import static org.example.primitives.Util.isZero;

/**
 * Represents an infinite plane in three-dimensional space.
 * Implements the Geometry interface to provide a method for obtaining the normal vector to the plane at a given point.
 */
public class Plane extends Geometry {
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

    @Override
    public List<Point> findIntersections(Ray ray) {
        //if the ray starts at the plane
        if (ray.getHead().equals(q)) {
            return null;
        }






        double t = alignZero(normal.dotProduct(ray.getDirection()));
        //if the ray is parallel to the plane
        if (isZero(t)) {
            return null;
        }

        double t1 = alignZero(normal.dotProduct(q.subtract(ray.getHead())) / t);
        //if the ray is in the opposite direction of the normal
        if (isZero(t1) || t1 < 0) {
            return null;
        }
        //if the ray intersects the plane
        return List.of(ray.getPoint(t1));//??????????/
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



    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<Point> intersections = findIntersections(ray);
        if (intersections == null) {
            return null;
        }
        return intersections.stream()
                .map(point -> new GeoPoint(this, point))
                .collect(Collectors.toList());
    }
}

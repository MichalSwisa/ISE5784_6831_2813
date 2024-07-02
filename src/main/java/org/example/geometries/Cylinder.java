package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Represents a cylinder in three-dimensional space, defined by its height, axis, and radius.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;
    private final Plane bottomCap, topCap;

    /**
     * Constructs a cylinder with the given height, axis, and radius.
     *
     * @param height The height of the cylinder.
     * @param axis   The axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     */
    public Cylinder(Ray axis, double height, double radius) {
        super(axis, radius);
        this.height = height;
        this.bottomCap = new Plane(axis.getHead(), axis.getDirection().scale(-1));
        this.topCap = new Plane(axis.getHead(height), axis.getDirection());
    }

    /**
     * Returns the normal vector to the surface of the cylinder at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getHead();
        /* The point is on the bottom */
        Vector vector, vector1;
        Point point1;
        /* The point is exactly in the center. */
        if (point.equals(p0)) {
            return axis.getDirection().scale(-1);
        }
        vector = p0.subtract(point);
        /* The point on the bottom but not in the center */
        if (isZero(vector.dotProduct(axis.getDirection()))) {
            return axis.getDirection().scale(-1);
        }

        point1 = p0.add(axis.getDirection().scale(height));

        /* The point is exactly in the center. */
        if (point.equals(point1)) {
            return axis.getDirection();
        }
        vector1 = point1.subtract(point);
        /* The point on the top but not in the center */
        if (isZero(vector1.dotProduct(axis.getDirection()))) {
            return axis.getDirection();
        }
        /* The point on the side, handle it like a tube. */
        return super.getNormal(point);
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point point1 = axis.getHead();
        Point point2 = axis.getHead(height);
        List<GeoPoint> result = null;
        /* Find the tube's intersections */
        List<GeoPoint> tubePoints = super.findGeoIntersectionsHelper(ray, maxDistance);
        GeoPoint geoPoint1;
        GeoPoint geoPoint2;
        boolean q0Intersects;
        boolean q1Intersects;

        if (tubePoints != null) {
            if (tubePoints.size() == 2) {
                /* Checks if the intersection points are on the cylinder */
                geoPoint1 = tubePoints.get(0);
                geoPoint2 = tubePoints.get(1);
                q0Intersects = isBetweenCaps(geoPoint1.point);
                q1Intersects = isBetweenCaps(geoPoint2.point);

                if (q0Intersects && q1Intersects) {
                    return tubePoints;
                }

                if (q0Intersects) {
                    result = new java.util.LinkedList<>();
                    result.add(geoPoint1);
                } else if (q1Intersects) {
                    result = new java.util.LinkedList<>();
                    result.add(geoPoint2);
                }
            }

            if (tubePoints.size() == 1) {
                /* Checks if the intersection point is on the cylinder */
                GeoPoint q = tubePoints.getFirst();
                if (isBetweenCaps(q.point)) {
                    result = new java.util.LinkedList<>();
                    result.add(q);
                }
            }
        }
        /* Finds the bottom cap's intersections */

        List<GeoPoint> cap0Point = bottomCap.findGeoIntersectionsHelper(ray, maxDistance);
        if (cap0Point != null) {
            // Checks if the intersection point is on the cap */
            geoPoint1 = cap0Point.getFirst();
            if (geoPoint1.point.distanceSquared(point1) < radius * radius) {
                if (result == null) {
                    result = new java.util.LinkedList<>();
                }
                result.add(geoPoint1);
                if (result.size() == 2) {
                    return result;
                }
            }
        }
        /* Finds the top cap's intersections */
        List<GeoPoint> cap1Point = topCap.findGeoIntersectionsHelper(ray, maxDistance);
        if (cap1Point != null) {
            /* Checks if the intersection point is on the cap */
            geoPoint1 = cap1Point.getFirst();
            if (geoPoint1.point.distanceSquared(point2) < radius * radius) {
                if (result == null) {
                    return List.of(geoPoint1);
                }

                result.add(geoPoint1);
            }
        }

        return result;
    }
    /**
     * Helper function that checks if a points is between the two caps.
     * @param p The point that will be checked.
     * @return True if it is between the caps. Otherwise, false.
     */
    private boolean isBetweenCaps(Point p) {
        Vector vector = axis.getDirection();
        Point point1 = axis.getHead();
        Point point2 = axis.getHead(height);
        /* Checks against zero vector... */
        if (p.equals(point1) || p.equals(point2)) {
            return false;
        }
        return vector.dotProduct(p.subtract(point1)) > 0 &&
               vector.dotProduct(p.subtract(point2)) < 0;
    }
}

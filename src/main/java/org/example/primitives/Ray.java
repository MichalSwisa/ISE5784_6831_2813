package org.example.primitives;

import org.example.geometries.Intersectable.GeoPoint;

import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Represents a ray in a three-dimensional space.
 */
public class Ray {
    private static final double DELTA = 0.00001;
    /**
     * The starting point of the ray.
     */
    private final Point head;
    /**
     * The direction vector of the ray, normalized.
     */
    private final Vector direction;

    /**
     * Constructs a ray with a given starting point and direction.
     *
     * @param head      The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * constructor for Ray class with normal to the direction
     *
     * @param head      the head of the ray
     * @param direction the direction of the ray
     * @param normal    the normal to the direction
     */
    public Ray(Point head, Vector direction, Vector normal) {
        Vector delta = normal.scale(normal.dotProduct(direction) > 0 ? DELTA : -DELTA);
        this.head = head.add(delta);
        this.direction = direction.normalize();
    }

    /**
     * getter for head
     */
    public Point getHead() {
        return head;
    }

    public Point getHead(double t) {
        return head.add(direction.scale(t));
    }

    /**
     * getter for direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * Checks whether this ray is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the given object is a Ray with the same head and direction, false otherwise.
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other) && head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * Returns a string representation of the ray, including its head and direction.
     *
     * @return A string representation of the ray.
     */
    @Override
    public String toString() {
        return "Ray: " +
                "head- " + head +
                ", direction- " + direction;
    }

    /**
     * @param t
     * @return the new point
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return head;
        }
        return head.add(direction.scale(t));
    }

    /**
     * Finds the closest point to the ray's origin from a list of points.
     *
     * @param points the list of points.
     * @return the closest point to the ray's origin, or null if the list is empty.
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }

    /**
     * this function get list of geoPoints and return the closest geoPoint to p0 of
     * the ray
     *
     * @param geoPoints list of GeoPoints for search
     * @return closestPoint closest GeoPoint to p0
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints) {
        if (geoPoints == null || geoPoints.isEmpty())
            return null;

        double minDistance = Double.POSITIVE_INFINITY;
        GeoPoint closestPoint = null;

        for (var p : geoPoints) {
            double distance = p.point.distanceSquared(head);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = p;
            }
        }
        return closestPoint;
    }

}





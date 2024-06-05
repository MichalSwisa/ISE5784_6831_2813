package org.example.primitives;

import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Represents a ray in a three-dimensional space.
 */
public class Ray {
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
     * Finds the closest point to the head of the ray from a given list of points.
     *
     * @param points A list of points to search from.
     * @return The point closest to the head of the ray, or null if the list is empty or null.
     */
    public Point findClosestPoint(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return null;
        }

        Point closestPoint = points.get(0);
        double minDistance = head.distance(closestPoint);

        for (Point point : points) {
            double distance = head.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                closestPoint = point;
            }
        }

        return closestPoint;
    }
}





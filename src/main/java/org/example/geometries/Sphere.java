package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Vector;

/**
 * Represents a sphere in three-dimensional space.
 * Extends the RadialGeometry abstract class to inherit the radius attribute.
 */
public class Sphere extends RadialGeometry {
    /**
     * The center point of the sphere.
     */
    private final Point center;

    /**
     * Constructs a sphere with the given center point and radius.
     *
     * @param center The center point of the sphere.
     * @param radius The radius of the sphere.
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    /**
     * Returns the normal vector to the surface of the sphere at the given point.
     * For a sphere, this method always returns null since the normal is not well-defined.
     *
     * @param point The point on the surface of the sphere.
     * @return Null, as the normal vector is not defined for a sphere.
     */
    @Override
    public Vector getNormal(Point point) {
        return point.subtract(center).normalize();
    }
}

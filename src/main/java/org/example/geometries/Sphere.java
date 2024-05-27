package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;

import static org.example.primitives.Util.alignZero;

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

    public List<Point> findIntersections(Ray ray) {
        // if the ray starts at the center of the sphere
        if (ray.getHead().equals(center)) {
            return List.of(ray.getPoint(radius));
        }
        //check if there is intersection between them
        Vector v = center.subtract(ray.getHead());

        double tm = alignZero(ray.getDirection().dotProduct(v));

        //check if the ray is tangent to the sphere
        double d = alignZero(Math.sqrt(v.lengthSquared() - tm * tm));
        if (d >= radius) return null;
        double th = alignZero(Math.sqrt(radius * radius - d * d));
        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);
        if (t1 > 0 && t2 > 0) {
            return List.of(ray.getPoint(t1), ray.getPoint(t2));
        }
        if (t1 > 0) {
            return List.of(ray.getPoint(t1));
        }
        if (t2 > 0) {
            return List.of(ray.getPoint(t2));
        }
        return null;
    }
}

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


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // if the ray starts at the center of the sphere
        if (ray.getHead().equals(center)) {
            return alignZero(this.radius - maxDistance) > 0 ? null : List.of(new GeoPoint(this, ray.getPoint(this.radius)));
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
        if (t2 <= 0) {
            return null;
        }
        if (t1 <= 0) {
            // One intersection point is behind the ray, the other is in front
            //We will check that our point is at the appropriate distance
            if (alignZero(maxDistance - t2) > 0) {
                return List.of(new GeoPoint(this, ray.getPoint(t2)));
            }
        } else {
            // Both intersection points are in front of the ray
            //We will check that our point is at the appropriate distance
            if (alignZero(maxDistance - t1) > 0 && alignZero(maxDistance - t2) > 0) {
                return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
            } else if (alignZero(maxDistance - t1) > 0) {
                return List.of(new GeoPoint(this, ray.getPoint(t1)));
            } else {
                return List.of(new GeoPoint(this, ray.getPoint(t2)));
            }
        }

        return null;
    }
        //@Override
        //protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //    List<Point> intersections = findIntersections(ray);
        //    if (intersections == null) {
        //        return null;
        //    }
        //    return intersections.stream()
        //            .map(point -> new GeoPoint(this, point))
        //            .collect(Collectors.toList());
        //}

}

package org.example.geometries;

import org.example.primitives.*;
import java.util.List;
import static org.example.primitives.Util.*;

/**
 * Represents a tube in three-dimensional space.
 * Extends the RadialGeometry abstract class to inherit the radius attribute.
 */
   public class Tube extends RadialGeometry {
    /**
     * The axis ray of the tube.
     */
    protected final Ray axis;

    /**
     * Constructs a tube with the given axis ray and radius.
     *
     * @param axis   The axis ray of the tube.
     * @param radius The radius of the tube.
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    /**
     * Returns the normal vector to the surface of the tube at the given point.
     * For a tube, this method always returns null since the normal is not well-defined.
     *
     * @param point The point on the surface of the tube.
     * @return Null, as the normal vector is not defined for a tube.
     */
    @Override
    public Vector getNormal(Point point) {
        Vector pMinusHead = point.subtract(axis.getHead());
        double t = axis.getDirection().dotProduct(pMinusHead);
        /* Check if the point is "front" to the p0 the point in the base */
        if (isZero(t)) {
            return pMinusHead.normalize();
        }
        /* The point on the side calculate the normal */
        Point center = axis.getHead().add(axis.getDirection().scale(t));
        return point.subtract(center).normalize();
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        // Extract the origin and direction of the ray
        // Point rayOrigin = ray.getHead();
        //Vector rayDirection = ray.getDirection();

        // Calculate the discriminant of the quadratic equation
        // double[] abc = Util.discriminantParam(rayDirection, rayOrigin, ray, radius);

        // double discriminant = abc[1] * abc[1] - 4 * abc[0] * abc[2];

        // If the discriminant is negative, the ray does not intersect the cylinder
        // if (discriminant < 0) {
        //     return null;
        //   }

        // Calculate the roots of the quadratic equation
        //  double t1 = (-abc[1] - Math.sqrt(discriminant)) / (2 * abc[0]);
        // double t2 = (-abc[1] + Math.sqrt(discriminant)) / (2 * abc[0]);

        // Calculate the intersection points
        //  Point intersectionPoint1 = ray.getHead().add(ray.getDirection().scale(t1));
        //  Point intersectionPoint2 = ray.getHead().add(ray.getDirection().scale(t2));

        //Add the intersection points to the list
        //return List.of(new GeoPoint(this, intersectionPoint1), new GeoPoint(this, intersectionPoint2));
        return null;
    }
}
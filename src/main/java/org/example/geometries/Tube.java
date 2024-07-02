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

    //..@Override
            //public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

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
       // return null;
   // }

    /**
     * Finds the intersection points of a given ray with the tube up to a maximum distance.
     *
     * @param ray The ray to intersect with the tube.
     * @param maxDistance The maximum distance to look for intersections.
     * @return A list of intersection points (GeoPoints), or null if no intersections are found.
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Vector rayDirection = ray.getDirection();
        Vector v0 = axis.getDirection();
        double temp1DotTemp2 = 0;
        double squaredTemp2 = 0;
        Vector temp1 = rayDirection;
        double vv0 = rayDirection.dotProduct(v0);

        if (!isZero(vv0)) {
            Vector v0vv0 = v0.scale(vv0);
            if (rayDirection.equals(v0vv0)) {
                return null;
            }
            temp1 = rayDirection.subtract(v0vv0);
        }
        /* Calculating temp2 = dp - v0 * (dp,v0) where dp = p0 - p */

        if (!ray.getHead().equals(axis.getHead())) {
            Vector dp = ray.getHead().subtract(axis.getHead());
            Vector temp2 = dp;
            double dpv0 = dp.dotProduct(v0);
            if (isZero(dpv0)) {
                temp1DotTemp2 = temp1.dotProduct(temp2);
                squaredTemp2 = temp2.lengthSquared();
            } else {
                Vector v0dpv0 = v0.scale(dpv0);
                if (!dp.equals(v0dpv0)) {
                    temp2 = dp.subtract(v0dpv0);
                    temp1DotTemp2 = temp1.dotProduct(temp2);
                    squaredTemp2 = temp2.lengthSquared();
                }
            }
        }

        /* ------Getting the quadratic equation: at^2 +bt + c = 0-------- */
        double a = temp1.lengthSquared();
        double b = 2 * temp1DotTemp2;
        double c = alignZero(squaredTemp2 - radius * radius);
        double squaredDelta = alignZero(b * b - 4 * a * c);
        if (squaredDelta <= 0) {
            return null;
        }
        double delta = Math.sqrt(squaredDelta);
        double t1 = alignZero((-b + delta) / (2 * a));
        double t2 = alignZero((-b - delta) / (2 * a));
        double distance1;
        double distance2;
        Point point1;
        Point point2;
        if (t1 > 0 && t2 > 0) {
            point1 = ray.getHead(t1);
            distance1 = ray.getHead().distance(point1);
            point2 = ray.getHead(t2);
            distance2 = ray.getHead().distance(point2);

            if (distance1 <= maxDistance && distance2 <= maxDistance) {
                return List.of(new GeoPoint(this, point1), new GeoPoint(this, point2));
            } else if (distance1 <= maxDistance) {
                return List.of(new GeoPoint(this, point1));
            } else if (distance2 <= maxDistance) {
                return List.of(new GeoPoint(this, point2));
            } else {
                return null;
            }
        }
        if (t1 > 0) {
            point1 = ray.getHead(t1);
            distance1 = ray.getHead().distance(point1);
            if (distance1 <= maxDistance) {
                return List.of(new GeoPoint(this, point1));
            }
        }
        if (t2 > 0) {
            point2 = ray.getHead(t2);
            distance2 = ray.getHead().distance(point2);
            if (distance2 <= maxDistance) {
                return List.of(new GeoPoint(this, point2));
            }
        }
        return null;
    }


   // @Override
   // public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
   //     return findGeoIntersectionsHelper(ray, Double.POSITIVE_INFINITY);
   // }
}
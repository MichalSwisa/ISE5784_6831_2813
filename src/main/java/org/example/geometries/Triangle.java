package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import java.util.stream.Collectors;

import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Represents a triangle in three-dimensional space.
 * Extends the Polygon class to represent a triangle with three vertices.
 */
public class Triangle extends Polygon {
    /**
     * Constructs a triangle with the given vertices.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = plane.findIntersections(ray);
        //if there are no intersections with the plane, there are no intersections with the triangle
        if (intersections == null) {
            return null;
        }

        //if the ray intersects the plane at the triangle's plane
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = ray.getDirection().dotProduct(n1);
        double s2 = ray.getDirection().dotProduct(n2);
        double s3 = ray.getDirection().dotProduct(n3);

        //if the ray is parallel to the triangle's plane
        if (isZero(s1) || isZero(s2) || isZero(s3)) {
            return null;
        }

        if (s1 > 0 && s2 > 0 && s3 > 0 || s1 < 0 && s2 < 0 && s3 < 0) {
            return List.of(intersections.get(0));

        }
        //if the ray intersects the plane but not the triangle
        return null;
    }


    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersections = plane.findGeoIntersections(ray, maxDistance);
        //if there are no intersections with the plane, there are no intersections with the triangle
        if (intersections == null) {
            return null;
        }

        //if the ray intersects the plane at the triangle's plane
        Vector v1 = vertices.get(0).subtract(ray.getHead());
        Vector v2 = vertices.get(1).subtract(ray.getHead());
        Vector v3 = vertices.get(2).subtract(ray.getHead());

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double s1 = ray.getDirection().dotProduct(n1);
        double s2 = ray.getDirection().dotProduct(n2);
        double s3 = ray.getDirection().dotProduct(n3);

        //if the ray is parallel to the triangle's plane
        if (isZero(s1) || isZero(s2) || isZero(s3)) {
            return null;
        }

        if (s1 > 0 && s2 > 0 && s3 > 0 || s1 < 0 && s2 < 0 && s3 < 0) {
            return List.of(new GeoPoint(this, intersections.get(0).point));
        }
        //if the ray intersects the plane but not the triangle
        return null;
    }
    //@Override
    //protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
    //    List<Point> intersections = super.findIntersections(ray); // מתודת מישור קיימת
    //    if (intersections == null) {
    //        return null;
    //    }
    //    return intersections.stream()
    //            .filter(this::isInside) // שיטה שבודקת אם הנקודה בתוך המשולש
    //            .map(point -> new GeoPoint(this, point))
    //            .collect(Collectors.toList());
    //}

    /**
     * Checks if a point is inside the triangle.
     *
     * @param point The point to check.
     * @return true if the point is inside the triangle, false otherwise.
     */
    private boolean isInside(Point point) {
        Vector v1 = vertices.get(0).subtract(point);
        Vector v2 = vertices.get(1).subtract(point);
        Vector v3 = vertices.get(2).subtract(point);

        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();

        double dot12 = n1.dotProduct(n2);
        double dot23 = n2.dotProduct(n3);
        double dot31 = n3.dotProduct(n1);

        return (dot12 > 0) && (dot23 > 0) && (dot31 > 0);
    }
}

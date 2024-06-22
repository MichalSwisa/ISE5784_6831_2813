package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;

import java.util.List;

/**
 * Intersectable interface is the basic interface for all geometries that can be intersected by a ray
 */
public abstract class Intersectable {
    /**
     * findIntersections function returns a list of intersection points of a ray with the geometry
     * @param ray the ray that intersects the geometry
     * @return a list of intersection points of the ray with the geometry
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }
       /**
        * A passive data structure (PDS) representing a geometric point.
        */
       public static class GeoPoint {
           /**
            * The geometry associated with the point.
            */
           public Geometry geometry;

           /**
            * The point in the geometry.
            */
           public Point point;

           /**
            * Constructs a GeoPoint with the specified geometry and point.
            *
            * @param geometry the geometry associated with the point
            * @param point the point in the geometry
            */
           public GeoPoint(Geometry geometry, Point point) {
               this.geometry = geometry;
               this.point = point;
           }

           /**
            * Checks if this GeoPoint is equal to another object.
            *
            * @param obj the object to compare with
            * @return true if the objects are equal, false otherwise
            */
           @Override
           public boolean equals(Object obj) {
               if (this == obj) return true;
               if (obj == null || getClass() != obj.getClass()) return false;

               GeoPoint geoPoint = (GeoPoint) obj;

               if (geometry != geoPoint.geometry) return false;
               return point != null ? point.equals(geoPoint.point) : geoPoint.point == null;
           }

           /**
            * Returns a string representation of the GeoPoint.
            *
            * @return a string representation of the GeoPoint
            */
           @Override
           public String toString() {
               return "GeoPoint{" +
                       "geometry=" + geometry +
                       ", point=" + point +
                       '}';
           }
       }

    /**
     * Finds the geometric intersections with the given ray.
     * This method follows the NVI (Non-Virtual Interface) pattern and delegates the actual work to the protected method {@code findGeoIntersectionsHelper}.
     *
     * @param ray the ray for which intersections are to be found
     * @return a list of GeoPoint objects representing the intersections, or null if no intersections are found
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * Finds the geometric intersections with the given ray.
     * This method is meant to be overridden by subclasses to provide the actual intersection logic.
     *
     * @param ray the ray for which intersections are to be found
     * @return a list of GeoPoint objects representing the intersections, or null if no intersections are found
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

   }

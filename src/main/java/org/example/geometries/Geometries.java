package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Geometries class represents a collection of geometries in 3D Cartesian coordinate system
 */
public class Geometries extends Intersectable {
    private final List<Intersectable> Geometry = new LinkedList<>();

    /**
     * Default constructor that creates an empty collection of geometries.
     */
    Geometries() {
    }

    /**
     * Constructor that initializes the collection with one or more geometries.
     *
     * @param geometries an array of geometries to be added to the collection
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Adds one or more geometries to the collection.
     *
     * @param geometries an array of geometries to be added to the collection
     */
    public void add(Intersectable... geometries) {
        Geometry.addAll(List.of(geometries));

    }

    /**
     * Finds all intersection points between a given ray and the geometries
     * in the collection.
     *
     * @param ray the ray to intersect with the geometries
     * @return a list of intersection points, or {@code null} if there are no intersections
     */
    public List<Point> findIntersections(Ray ray) {
        List<Point> totalIntersections = null;
        for (Intersectable geometry : Geometry) {
            List<Point> intersection = geometry.findIntersections(ray);
            if (intersection != null) {
                if (totalIntersections == null) {
                    totalIntersections = new ArrayList<>();
                }
                totalIntersections.addAll(intersection);
            }
        }
        return totalIntersections;
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> geoIntersections = new ArrayList<>();
        for (Intersectable intersectable : Geometry) {
            List<GeoPoint> intersections = intersectable.findGeoIntersections(ray);
            if (intersections != null) {
                geoIntersections.addAll(intersections);
            }
        }
        return geoIntersections.isEmpty() ? null : geoIntersections;
    }

}

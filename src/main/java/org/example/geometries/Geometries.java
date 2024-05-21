package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;



import java.util.LinkedList;
import java.util.List;
/**
 * Geometries class represents a collection of geometries in 3D Cartesian coordinate system
 */
public class Geometries implements Intersectable{
    private final List<Intersectable> Geometry = new LinkedList<>();

    Geometries(){}

    public Geometries(Intersectable... geometries){
        add(geometries);
    }

    public void add(Intersectable... geometries){
        Geometry.addAll(List.of(geometries));

    }

    public List<Point> findIntersections(Ray ray) {
    return null;}
}

package org.example.geometries;

import org.example.geometries.Intersectable;
import org.example.primitives.Point;
import org.example.primitives.Vector;

/**
 * Represents a geometric shape in three-dimensional space.
 * Defines a method for obtaining the normal vector to the surface at a given point.
 */
public interface Geometry extends Intersectable {
    /**
     * Returns the normal vector to the surface of the geometry at the given point.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector to the surface at the given point.
     */
    Vector getNormal(Point point);

}

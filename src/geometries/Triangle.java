package geometries;

import primitives.Point;

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
}

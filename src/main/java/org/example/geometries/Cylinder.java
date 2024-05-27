package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Represents a cylinder in three-dimensional space, defined by its height, axis, and radius.
 */
public class Cylinder extends Tube {
    /**
     * The height of the cylinder.
     */
    private final double height;

    /**
     * Constructs a cylinder with the given height, axis, and radius.
     *
     * @param height The height of the cylinder.
     * @param axis   The axis ray of the cylinder.
     * @param radius The radius of the cylinder.
     */
    public Cylinder(Ray axis, double height, double radius) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * Returns the normal vector to the surface of the cylinder at the given point.
     */
    @Override
    public Vector getNormal(Point point) {
        Point p0 = axis.getHead();
        /* The point is on the bottom */
        Vector vector, vector1;
        Point point1;
        /* The point is exactly in the center. */
        if (point.equals(p0)) {
            return axis.getDirection().scale(-1);
        }
        vector = p0.subtract(point);
        /* The point on the bottom but not in the center */
        if (isZero(vector.dotProduct(axis.getDirection()))) {
            return axis.getDirection().scale(-1);
        }

        point1 = p0.add(axis.getDirection().scale(height));

        /* The point is exactly in the center. */
        if (point.equals(point1)) {
            return axis.getDirection();
        }
        vector1 = point1.subtract(point);
        /* The point on the top but not in the center */
        if (isZero(vector1.dotProduct(axis.getDirection()))) {
            return axis.getDirection();
        }
        /* The point on the side, handle it like a tube. */
        return super.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

}

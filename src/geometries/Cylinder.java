package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

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
    public Cylinder(double height, Ray axis, double radius) {
        super(axis, radius);
        this.height = height;
    }

    /**
     * Returns the normal vector to the surface of the cylinder at the given point.
     * For a cylinder, this method always returns null since the normal is not well-defined.
     *
     * @param point The point on the surface of the cylinder.
     * @return Null, as the normal vector is not defined for a cylinder.
     */
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
}

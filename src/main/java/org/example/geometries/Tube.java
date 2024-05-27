package org.example.geometries;

import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;

import static org.example.primitives.Util.isZero;
import static org.example.primitives.Util.alignZero;

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
    public List<Point> findIntersections(Ray ray){
       return null;
    }
}

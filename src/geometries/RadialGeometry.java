package geometries;


/**
 * Abstract class representing a radial geometry in three-dimensional space.
 * Radial geometries are geometric shapes defined by a radius.
 * Implements the Geometry interface to provide a method for obtaining the normal vector.
 */
public abstract class RadialGeometry implements Geometry {
    /**
     * The radius of the radial geometry.
     */
    protected double radius;

    /**
     * Constructs a radial geometry with the given radius.
     *
     * @param radius The radius of the radial geometry.
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }

}

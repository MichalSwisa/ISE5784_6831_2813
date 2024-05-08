package primitives;

import static primitives.Util.isZero;

/**
 * Represents a point in three-dimensional coordinate system.
 */
public class Point {
    protected final Double3 xyz;

    /**
     * The origin point (0,0,0).
     */
    public static final Point ZERO = new Point(Double3.ZERO);

    /**
     * Constructs a new point with the specified coordinates.
     *
     * @param x The X-coordinate value
     * @param y The Y-coordinate value
     * @param z The Z-coordinate value
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    /**
     * Constructs a new point from a Double3 object.
     *
     * @param xyz The Double3 object representing the x, y, and z values
     */
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /**
     * Adds a vector to the point and returns the new point.
     *
     * @param vector The vector to add to the current point
     * @return A new point obtained by adding the vector
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Subtracts a point from this point and returns the resulting vector.
     *
     * @param point The point to subtract from this point
     * @return The vector representing the subtraction of the two points
     */
    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }


    /**
     * Calculates the distance between this point and another point.
     *
     * @param point The other point
     * @return The distance between the two points
     */
    public Double distance(Point point) {
        return Math.sqrt(this.distanceSquared(point));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param point The other point
     * @return The squared distance between the two points
     */
    public Double distanceSquared(Point point) {
        double x1 = xyz.d1;
        double y1 = xyz.d2;
        double z1 = xyz.d3;

        double x2 = point.xyz.d1;
        double y2 = point.xyz.d2;
        double z2 = point.xyz.d3;

        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));

    }

    /**
     * Returns a string representation of the point.
     *
     * @return A string representing the point
     */
    @Override
    public String toString() {
        return "Point: " + xyz;
    }

    /**
     * Checks if this point is equal to another object.
     *
     * @param obj The object to compare with
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Point other) && xyz.equals(other.xyz);
    }
}

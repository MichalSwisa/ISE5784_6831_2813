package org.example.primitives;

/**
 * Represents a vector in three-dimensional space.
 */
public class Vector extends Point {
    /**
     * Constructs a vector with the given x, y, and z coordinates.
     *
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     * @param z The z-coordinate of the vector.
     * @throws IllegalArgumentException if the vector is the zero vector.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (x == 0 && y == 0 && z == 0) throw new IllegalArgumentException("ZERO vector not allowed");
    }

    /**
     * Constructs a vector with the given Double3 object.
     *
     * @param xyz The Double3 object representing the vector's coordinates.
     * @throws IllegalArgumentException if the vector is the zero vector.
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) throw new IllegalArgumentException("ZERO vector not allowed");
    }

    /**
     * Adds another vector to this vector.
     *
     * @param vector The vector to add.
     * @return The result of adding the given vector to this vector.
     */
    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    /**
     * Scales this vector by a scalar value.
     *
     * @param scale The scalar value to scale the vector by.
     * @return The scaled vector.
     */
    public Vector scale(double scale) {
        return new Vector(xyz.scale(scale));
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param vector The other vector.
     * @return The dot product of this vector and the given vector.
     */
    public double dotProduct(Vector vector) {
        double u1 = this.xyz.d1;
        double u2 = this.xyz.d2;
        double u3 = this.xyz.d3;

        double v1 = vector.xyz.d1;
        double v2 = vector.xyz.d2;
        double v3 = vector.xyz.d3;

        return (u1 * v1 + u2 * v2 + u3 * v3);
    }

    /**
     * Calculates the cross product of this vector with another vector.
     *
     * @param v The other vector.
     * @return The cross product of this vector and the given vector.
     */
    public Vector crossProduct(Vector v) {
        return new Vector(
                new Double3(
                        xyz.d2 * v.xyz.d3 - xyz.d3 * v.xyz.d2,
                        xyz.d3 * v.xyz.d1 - xyz.d1 * v.xyz.d3,
                        xyz.d1 * v.xyz.d2 - xyz.d2 * v.xyz.d1));
    }

    /**
     * Calculates the squared length of this vector.
     *
     * @return The squared length of this vector.
     */
    public double lengthSquared() {
        return this.dotProduct(this);
    }

    /**
     * Calculates the length of this vector.
     *
     * @return The length of this vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Normalizes this vector.
     *
     * @return The normalized vector.
     */
    public Vector normalize() {
        return scale(1 / length());
    }

    /**
     * Checks if this vector and the given vector are both unit vectors and
     * are either equal or opposites.
     *
     * @param vector the vector to compare with this vector
     * @return {@code true} if both vectors are unit vectors and either equal or opposites;
     *         {@code false} otherwise
     */
    public boolean isSameNormal(Vector vector) {
        if (!(Util.isZero(length() - 1) && Util.isZero(vector.length() - 1))) {
            return false;
        }
        return equals(vector) || equals(vector.scale(-1));
    }

    /**
     * Checks whether this vector is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the given object is a Vector with the same coordinates, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.equals(other); ///??????
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vector" + super.toString();
    }
}

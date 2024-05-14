package org.example.primitives;

/**
 * Represents a ray in a three-dimensional space.
 */
public class Ray {
    /**
     * The starting point of the ray.
     */
    private final Point head;
    /**
     * The direction vector of the ray, normalized.
     */
    private final Vector direction;

    /**
     * Constructs a ray with a given starting point and direction.
     *
     * @param head      The starting point of the ray.
     * @param direction The direction vector of the ray.
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize();
    }

    /**
     * Checks whether this ray is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the given object is a Ray with the same head and direction, false otherwise.
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other) && head.equals(other.head) && direction.equals(other.direction);
    }

    /**
     * Returns a string representation of the ray, including its head and direction.
     *
     * @return A string representation of the ray.
     */
    @Override
    public String toString() {
        return "Ray: " +
                "head- " + head +
                ", direction- " + direction;
    }
}




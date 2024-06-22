package org.example.geometries;
import org.example.primitives.*;

import java.util.List;
/**
 * Represents a geometric shape in three-dimensional space.
 * Defines a method for obtaining the normal vector to the surface at a given point.
 */
public abstract class Geometry extends Intersectable {
    /**
     * Returns the normal vector to the surface of the geometry at the given point.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector to the surface at the given point.
     */
    protected Color emission = Color.BLACK;
    /**
     * The material of the geometry.
     */
    private Material material = new Material();

    // ***************** Getters ********************** //

    /**
     * Returns the emission color of the geometry.
     *
     * @return The emission color.
     */
    public Color getEmission()
    {
        return emission;
    }

    /**
     * Returns the material of the geometry.
     *
     * @return The geometry material.
     */
    public Material getMaterial()
    {
        return material;
    }

    // ***************** Setters (builder pattern) ********************** //

    /**
     * Sets the emission color of the geometry.
     *
     * @param emission The new emission color.
     * @return This Geometry object (builder pattern).
     */
    public Geometry setEmission(Color emission)
    {
        this.emission = emission;
        return this;
    }

    /**
     * Sets the material of the geometry.
     *
     * @param material The new material.
     * @return This Geometry object (builder pattern).
     */
    public Geometry setMaterial(Material material)
    {
        this.material = material;
        return this;
    }

    /*************************** Operations *******************/

    /**
     * Calculates the normal vector of the geometry at a given point on its surface.
     *
     * @param point The point on the surface of the geometry.
     * @return The normal vector.
     */
    public abstract Vector getNormal(Point point);
}

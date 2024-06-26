package org.example.lighting;

import org.example.primitives.Color;
import org.example.primitives.Point;
import org.example.primitives.Vector;

/**
 * LightSource interface to Light Source operations
 *
 * @author Yael and Shira
 */
public interface LightSource {

    // ***************** getters ********************** //

    /**
     * get point and return the Intensity of this light on this point
     *
     * @param p The point for which we want to know the light intensity
     * @return color of this point
     */
    public Color getIntensity(Point p);

    /**
     * get point and return the vector from the light to the point
     *
     * @param p The point for which we want to know the direction to the light source
     * @return vector direction to light source
     */
    public Vector getL(Point p);

    /**
     * Gets the distance from the light source to a given point.
     *
     * @param point The point to get the distance to.
     * @return The distance from the light source to the point.
     */
    double getDistance(Point point);
}
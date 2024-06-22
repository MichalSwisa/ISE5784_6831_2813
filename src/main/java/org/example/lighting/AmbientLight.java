package org.example.lighting;

import org.example.primitives.Color;
import org.example.primitives.Double3;

/**
 * The AmbientLight class represents ambient light in a scene.
 * It contains the intensity of the ambient light, which is calculated
 * based on the original intensity and an attenuation coefficient.
 */
public class AmbientLight extends Light {
    /**
     * A static final instance representing no ambient light (intensity is black, attenuation is zero).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

  //  private final Color intensity;

    // ******************* constructors *****************
    /**
     * constructor calculate the intensity (Double3 object) use super constructor
     *
     * @param light light intensity according to the RGB component
     * @param ka    attenuation coefficient of the light on the surface
     */
    public AmbientLight(Color light, Double3 ka) {
        super(light.scale(ka));
    }

    /**
     * constructor calculate the intensity (java.double object) use super
     * constructor
     *
     * @param light light intensity according to the RGB component
     * @param ka    attenuation coefficient of the light on the surface
     */
    public AmbientLight(Color light, double ka) {
        super(light.scale(ka));
    }

    /**
     * Gets the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
   // public Color getIntensity() {
     //   return intensity;
    //}
}


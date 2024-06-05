package org.example.lighting;

import org.example.primitives.Color;
import org.example.primitives.Double3;

/**
 * The AmbientLight class represents ambient light in a scene.
 * It contains the intensity of the ambient light, which is calculated
 * based on the original intensity and an attenuation coefficient.
 */
public class AmbientLight {
    /**
     * A static final instance representing no ambient light (intensity is black, attenuation is zero).
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, 0.0);

    private final Color intensity;

    /**
     * Constructs an AmbientLight with specified intensity and attenuation coefficient.
     *
     * @param IA the original intensity (color) of the ambient light
     * @param kA the attenuation coefficient as a Double3 object
     */
    public AmbientLight(Color IA, Double3 kA) {
        this.intensity = IA.scale(kA);
    }

    /**
     * Constructs an AmbientLight with specified intensity and attenuation coefficient.
     *
     * @param IA the original intensity (color) of the ambient light
     * @param kA the attenuation coefficient as a double
     */
    public AmbientLight(Color IA, double kA) {
        this.intensity = IA.scale(kA);
    }

    /**
     * Gets the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }
}

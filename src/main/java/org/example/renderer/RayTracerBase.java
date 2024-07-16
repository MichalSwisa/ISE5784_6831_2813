package org.example.renderer;
import org.example.primitives.Color;
import org.example.primitives.Ray;
import org.example.scene.Scene;

import java.util.List;

/**
 * The abstract class RayTracerBase represents the base class for ray tracing algorithms.
 * It contains a protected field for the scene and an abstract public method for tracing rays.
 */
public abstract class RayTracerBase {
    /**
     * The scene used for ray tracing.
     */
    protected final Scene scene;

    /**
     * Constructs a RayTracerBase object with a given scene.
     *
     * @param scene The scene to be used for ray tracing.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Abstract method for tracing rays and calculating colors.
     *
     * @param ray The ray to trace.
     * @return The color calculated for the traced ray.
     */
    public abstract Color traceRay(Ray ray);
    /**
     * Abstract method for tracing rays and determining the resulting color.
     *
     * @param ray The list of rays to trace.
     * @return The resulting color after tracing the rays.
     */
    public abstract Color traceRay(List<Ray> ray);
}

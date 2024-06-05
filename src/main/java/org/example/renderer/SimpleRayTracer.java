package org.example.renderer;

import org.example.geometries.Intersectable;
import org.example.primitives.Point;
import org.example.primitives.Color;
import org.example.primitives.Ray;
import org.example.scene.Scene;

import java.util.List;

/**
 * The SimpleRayTracer class represents a simple implementation of a ray tracer,
 * inheriting from the abstract class RayTracerBase.
 * It contains a constructor that initializes the scene.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Constructs a SimpleRayTracer object with a given scene.
     *
     * @param scene The scene to be used for ray tracing.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * A basic implementation of the {@code RayTracerBase}
     * class that computes the color of the closest intersection point with the scene.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        else
            return calcColor(ray.findClosestPoint(intersections));
    }

    /**
     * Computes the color of the intersection point using the Phong reflection model.
     *
     * @param point the intersection point
     * @return the color of the intersection point
     */
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }

}

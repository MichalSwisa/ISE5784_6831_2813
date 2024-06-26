package org.example.renderer;

import org.example.geometries.Intersectable.GeoPoint;
import org.example.lighting.LightSource;
import org.example.primitives.*;
import org.example.scene.Scene;

import java.util.List;

/**
 * The SimpleRayTracer class is a simple implementation of the RayTracerBase class.
 */
public class SimpleRayTracer extends RayTracerBase {
    /**
     * Size of the ray origin offset for shadow rays.
     * The value can be adjusted based on the scale of the shapes in your image so that the offset is not noticeable.
     */
    private static final double DELTA = 0.1;

    /**
     * Constructs a SimpleRayTracer with the specified scene.
     *
     * @param scene the scene to be traced.
     */
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * Checks if a point is illuminated or in shadow relative to a light source.
     *
     * @param gp The point on the geometry in the scene.
     * @param l  The vector towards the light source.
     * @param n  The normal vector at the point.
     * @return true if the point is illuminated, false if it is in shadow.
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // Direction from point to light source
        Vector epsVector = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections == null) return true;

        double lightDistance = lightSource.getDistance(gp.point);
        for (GeoPoint geop : intersections) {
            if (geop.point.distance(gp.point) < lightDistance)
                return false;
        }
        return true;

    }

    /**
     * Traces a ray through the scene and calculates the color at the point where the ray intersects with an object.
     *
     * @param ray the ray to trace through the scene
     * @return the color at the point where the ray intersects with an object, or the background color if no intersection is found
     */
    @Override
    public Color traceRay(Ray ray) {
        // Get all intersection points
        var intersectionsLst = scene.geometries.findGeoIntersectionsHelper(ray);

        // No intersection points
        return intersectionsLst == null ? scene.background
                // Return the color of the closest intersection point
                : calcColor(ray.findClosestGeoPoint(intersectionsLst), ray);
    }

    /**
     * Calculates the color of a point in the scene.
     *
     * @param geoPoint The point on the geometry in the scene.
     * @param ray      The ray from the camera to the intersection.
     * @return The color of the point.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(geoPoint.geometry.getEmission())
                .add(calcLocalEffects(geoPoint, ray));
    }
    //הפונקציה traceRay מאתרת את נקודות החיתוך של הקרן עם הגאומטריות בסצנה.
    // אם לא נמצאו נקודות חיתוך, היא מחזירה את צבע הרקע של הסצנה.
    // אחרת, היא מוצאת את הנקודה הקרובה ביותר לראש הקרן ומחשבת את הצבע של הנקודה הזו.
    //הפונקציה calcColor מחשבת את הצבע של נקודה בסצנה בהתבסס על תאורת הסביבה הקיימת ומחזירה את הצבע הזה.

    /**
     * Calculates the effect of different light sources on a point in the scene
     * according to the Phong model.
     *
     * @param intersection The point on the geometry in the scene.
     * @param ray          The ray from the camera to the intersection.
     * @return The color of the point affected by local light sources.
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
        Vector v = ray.getDirection();
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = Util.alignZero(n.dotProduct(v));
        if (nv == 0)
            return Color.BLACK;

        int nShininess = intersection.geometry.getMaterial().nShininess;

        Double3 kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;

        Color color = Color.BLACK;
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = Util.alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sign(nv)
                if (unshaded(intersection, l, n, lightSource)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point);
                    color = color.add(calcDiffuse(kd, nl, lightIntensity),
                            calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
                }
            }
        }
        return color;
    }

    /**
     * Calculates the diffuse component of light reflection.
     *
     * @param kd             The diffuse reflection coefficient.
     * @param nl             The dot product between the normal vector and the light
     *                       vector.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the diffuse reflection.
     */
    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity) {
        return lightIntensity.scale(kd.scale(Math.abs(nl)));
    }

    /**
     * Calculates the specular component of light reflection.
     *
     * @param ks             The specular reflection coefficient.
     * @param l              The light vector.
     * @param n              The normal vector.
     * @param nl             The dot product between the normal vector and the light
     *                       vector.
     * @param v              The view vector.
     * @param nShininess     The shininess coefficient.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -Util.alignZero(r.dotProduct(v));
        if (minusVR <= 0) {
            return Color.BLACK; // View from direction opposite to r vector
        }
        return lightIntensity.scale(ks.scale(Math.pow(minusVR, nShininess)));
    }

}




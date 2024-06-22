package org.example.lighting;

import org.example.primitives.*;

import static org.example.primitives.Util.alignZero;

/**
 * The SpotLight class represents a spotlight in a scene. It is a type of point
 * light with an additional direction vector.
 */
public class SpotLight extends PointLight
{

    private Vector direction;
//    private double narrowBeam = 1;

    /**
     * Constructs a SpotLight object with the specified intensity, position, and
     * direction.
     *
     * @param intensity The intensity of the spotlight.
     * @param position  The position of the spotlight.
     * @param direction The direction vector of the spotlight.
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

//    /**
//     * setter for narrowBeam
//     *
//     * @param narrowBeam the new value for narrowBeam
//     * @return this light
//     */
//    public SpotLight setNarrowBeam(double narrowBeam)
//    {
//        this.narrowBeam = narrowBeam;
//        return this;
//    }

    @Override
    public Color getIntensity(Point p)
    {
        double projection = alignZero(direction.dotProduct(getL(p)));
        double factor = Math.max(0, projection);
        Color pointlightIntensity = super.getIntensity(p);
        return pointlightIntensity.scale(factor);
    }

}
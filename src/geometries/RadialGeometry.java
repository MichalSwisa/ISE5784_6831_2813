package geometries;

import primitives.Point;

public abstract class RadialGeometry implements Geometry {
    protected double radius;

    public RadialGeometry(double radius) {
        this.radius = radius;
    }

    public Vector getNormal(Point point){}
}

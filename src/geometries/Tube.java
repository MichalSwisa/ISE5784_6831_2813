package geometries;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

public class Tube extends RadialGeometry {
    protected Ray axis;

    public Tube(Ray axis, double radius){
        super(radius);
        this.axis = axis;
    }
    public Vector getNormal(Point point) {
        return null;
    }
}

package geometries;

public class Tube extends RadialGeometry {
    protected Ray axis;

    public Tube(Ray axis){
        this.axis = axis;
    }
    public Vector getNormal(Point point) {
        return null;
    }
}

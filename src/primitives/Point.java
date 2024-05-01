package primitives;

import static primitives.Util.isZero;

public class Point {
    protected Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Point add(Vector vector){
        return new Point(xyz.add(vector.xyz));
    }

    public Vector subtract(Point point){
        Double3 result = xyz.subtract(point.xyz);

        if (result.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("ZERO vector not allowed");
        }

        return new Vector(result);//
    }



    public Double distance(Point point){
        return this.distanceSquared(point);
    }
    public Double distanceSquared(Point point){
        double x1 = xyz.d1;
        double y1 = xyz.d2;
        double z1 = xyz.d3;

        double x2 = point.xyz.d1;
        double y2 = point.xyz.d2;
        double z2 = point.xyz.d3;

        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));

    }

    public String toString() {
         return "Point: " + xyz.toString();
    }

    public boolean equals(Object obj){
        if (this == obj) return true;
        return (obj instanceof Double3 other) && xyz.equals(other.xyz);
    }
}

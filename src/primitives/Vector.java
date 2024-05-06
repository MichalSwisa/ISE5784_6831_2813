package primitives;

public class Vector extends Point {
    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO)) throw new IllegalArgumentException("ZERO vector not allowed");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO)) throw new IllegalArgumentException("ZERO vector not allowed");
    }

    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }
     public Vector scale(double scale) {
        return new Vector(xyz.scale(scale));
     }

     public double dotProduct(Vector vector) {
         double u1 = this.xyz.d1;
         double u2 = this.xyz.d2;
         double u3 = this.xyz.d3;

         double v1 = vector.xyz.d1;
         double v2 = vector.xyz.d2;
         double v3 = vector.xyz.d3;

         return (u1 * v1 + u2 * v2 + u3 * v3);
     }

     public Vector crossProduct(Vector vector) {
         double x1 = this.xyz.d1;
         double x2 = this.xyz.d2;
         double x3 = this.xyz.d3;

         double y1 = vector.xyz.d1;
         double y2 = vector.xyz.d2;
         double y3 = vector.xyz.d3;

         return new Vector(x2*y3-y2*x3,x3*y1-y3*x1,x1*y2-y1*x2);
     }
     public double lengthSquared(){
        return this.dotProduct(this);
     }
     public double length(){
        return Math.sqrt(this.lengthSquared());
     }
     public Vector normalize() {
        double len = this.length();
        return new Vector(this.xyz.reduce(len));
     }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Vector other)
                && super.equals(obj); ///??????
    }


    @Override
    public String toString() {
        return "Vector" + xyz;
    }
}

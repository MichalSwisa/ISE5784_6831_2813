package org.example.geometries;


import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.example.primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal();
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal();
    }

    public List<Point> findIntersections(Ray ray) {
        List<Point> intersections = plane.findIntersections(ray);
        //if there are no intersections with the plane, there are no intersections with the polygon
        if (intersections == null) {
            return null;
        }

        Point checkPoint = intersections.get(0);
        List<Vector> result = new ArrayList<>();
        Point last = vertices.get(size - 1);
        //we will use the method of ni=(pi-pi-1)x(pi-1-Pinter) to check if the point is inside the polygon
        try {
            for (Point p : vertices) {//we will add all of the vectors to the list
                result.add(p.subtract(last).crossProduct(last.subtract(checkPoint)));
                last = p;
            }
            Vector lastVec = result.getLast();
            for (Vector v : result) {//we will check if the vectors are in the same direction
                if (v.dotProduct(lastVec) <= 0) {
                    return null;
                }
                lastVec = v;
            }
        }
        //if the point is on the edge of the polygon
        catch (IllegalArgumentException e) {
            return null;
        }
        return intersections;

    }
    //@Override
    //protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray)
    //{
    //    return List.of();
    //}

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double distance){
        List<GeoPoint> intersections=plane.findGeoIntersections(ray, distance);
        //if there are no intersections with the plane, there are no intersections with the polygon
        if(intersections==null){
            return null;
        }

        GeoPoint checkPoint=intersections.get(0);
        List<Vector> result=new LinkedList<>();
        Point last=vertices.get(size-1);
        //we will use the method of ni=(pi-pi-1)x(pi-1-Pinter) to check if the point is inside the polygon
        try{
            for(Point p:vertices){//we will add all of the vectors to the list
                result.add(p.subtract(last).crossProduct(last.subtract(checkPoint.point)));
                last=p;
            }
            Vector lastVec=result.getLast();
            for(Vector v:result){//we will check if the vectors are in the same direction
                if(v.dotProduct(lastVec)<=0){
                    return null;
                }
                lastVec=v;
            }
        }
        //if the point is on the edge of the polygon
        catch (IllegalArgumentException e){
            return null;
        }
        return List.of(new GeoPoint(this,checkPoint.point));
    }
}

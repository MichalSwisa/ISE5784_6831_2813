package geometries;


import org.example.geometries.Polygon;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
/**
 * Testing Polygons
 * @author Dan
 */
public class PolygonTests  {
    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /** Test method for {@link Polygon#Polygon(Point...)}. */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        assertDoesNotThrow(() -> new Polygon(new Point(0, 0, 1),
                        new Point(1, 0, 0),
                        new Point(0, 1, 0),
                        new Point(-1, 1, 1)),
                "Failed constructing a correct polygon");

        // TC02: Wrong vertices order
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                "Constructed a polygon with wrong order of vertices");

        // TC03: Not in the same plane
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                "Constructed a polygon with vertices that are not in the same plane");

        // TC04: Concave quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0.5, 0.25, 0.5)), //
                "Constructed a concave polygon");

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                        new Point(0, 0.5, 0.5)),
                "Constructed a polygon with vertix on a side");

        // TC11: Last point = first point
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                "Constructed a polygon with vertice on a side");

        // TC12: Co-located points
        assertThrows(IllegalArgumentException.class, //
                () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                "Constructed a polygon with vertice on a side");

    }

    /** Test method for {@link Polygon#getNormal(Point)}. */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Polygon pol = new Polygon(pts);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pol.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), DELTA, "Polygon's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertEquals(0d, result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1])), DELTA,
                    "Polygon's normal is not orthogonal to one of the edges");
    }

    /**
     * Test method for {@link Polygon#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections(){

        Polygon polygon = new Polygon(new Point(0, 0.5, -1),
                new Point(0, -0.5, -1),
                new Point(0, -1, 0),
                new Point(0, 0, 1),
                new Point(0, 1, 0)
        );
        // ============ Equivalence Partitions Tests ==============
        // TC01: test case where the point is inside the polygon (1 point)
        Point p1 = new Point(0, 0, 0.5);
        List<Point> result = polygon.findIntersections(new Ray(new Point(1, 0, 0),
                new Vector(-1, 0, 0.5)));
        assertEquals(1, result.size(), "Wrong number of points, TC01");
        assertEquals(List.of(p1), result, "Wrong point, TC01");
        // TC02: test case where the point is outside against edge (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1,0.7,0.7))),
                "Ray's line out of polygon, TC02");
        // TC02: test case where the point is outside against vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1,0,2))),
                "Ray's line out of polygon, TC03");

        // =============== Boundary Values Tests ==================
        // TC03: test case where the ray begins before the plane and on edge (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1,1,0))),
                "Ray's line out of polygon, TC11");
        // TC04: test case where the ray begins before the plane and in vertex (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1,0,5))),
                "Ray's line out of polygon, TC12");
        // TC05: test case where the ray begins before the plane and on edge's continuation (0 points)
        assertNull(polygon.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1,1,-1))),
                "Ray's line out of polygon, TC13");
    }
}

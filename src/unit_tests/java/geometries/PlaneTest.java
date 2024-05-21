package geometries;

import org.example.geometries.Plane;
import org.example.primitives.Point;
import org.example.primitives.Vector;
import org.example.primitives.Ray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Testing Plane.
 */
public class PlaneTest {
    /**
     * Test method for {@link Plane#Plane(Point, Point, Point)}.
     */
    @Test
    public void testConstructor() {

        Point x1 = new Point(1, 0, 0);
        Point y1 = new Point(2, 0, 0);
        Point z1 = new Point(3, 0, 0);

        Point xy2 = new Point(1, 0, 0);
        Point z2 = new Point(3, 4, 0);

        /* =============== Boundary Values Tests ================== */

        /* TC01: some points on the same line. */
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(x1, y1, z1),
                "ERROR: Plane constructor should throw exception " +
                        "when some points on the same line");

        /* TC02: some points are the same. */
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(xy2, xy2, z2),
                "ERROR: Plane constructor should throw exception " +
                        "when some points are the same");
    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {

        Point a = new Point(1, 2, 3);
        Point b = new Point(2, 1, 4);
        Point c = new Point(2, 1, 1);

        Plane plane = new Plane(a, b, c);
        Vector expectedVector = new Vector(3, 3, 0).normalize();

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check normal in specific point. */
        assertTrue(expectedVector.isSameNormal(plane.getNormal(a)),
                "ERROR: getNormal() doesn't work correctly.");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections(){
        Plane p =new Plane(new Point(1, 1, 1),new Vector(1,1,1));
        List<Point> result;
        // ============ Equivalence Partitions Tests ==============

        //TC01 start not in the plane and Intsersect the plane
        result = p.findIntersections(new Ray(new Point(0, 1, 1), new Vector(1, 0, 1)));
        assertEquals(result, List.of(new Point(0.5, 1, 1.5)), "Error start not in the plane and Intsersect the plane");

        //TC02 start not in the plane and not Intsersect the plane
        result = p.findIntersections(new Ray(new Point(0, 1, 1), new Vector(0, -1, 1)));
        assertNull(result, "start not in the plane and not Intsersect the plane");

        // =============== Boundary Values Tests ==================

        //TC03 on the plane and not Intsersect the plane (paralel)
        result = p.findIntersections(new Ray(new Point(2, 1, 0), new Vector(0, -1, 1)));
        assertNull(result, "on the plane and not Intsersect the plane (paralel)");

        //TC04 not on the plane and not Intsersect the plane (paralel(up))
        result = p.findIntersections(new Ray(new Point(5, 1, 1), new Vector(0, -1, 1)));
        assertNull(result, "not on the plane and not Intsersect the plane (paralel(up))");

        //TC05 not on the plane and not Intsersect the plane (paralel(down))
        result = p.findIntersections(new Ray(new Point(-1, 1, 1), new Vector(0, -1, 1)));
        assertNull(result, "not on the plane and not Intsersect the plane (paralel(down))");

        //TC06 perpendicular to the plane (before)
        result = p.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 1)));
        assertEquals(result, List.of(new Point(1, 1, 1)), "perpendicular to the plane (before)");

        //TC07 perpendicular to the plane (on)
        result = p.findIntersections(new Ray(new Point(2, 1, 0), new Vector(1, 1, 1)));
        assertNull(result, "perpendicular to the plane (on)");

        //TC08 perpendicular to the plane (after)
        result = p.findIntersections(new Ray(new Point(2, 2, 2), new Vector(1, 1, 1)));
        assertNull(result, "perpendicular to the plane (after)");

        //TC09 starts on the normal but the ray not on the plane
        result = p.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 0, 0)));
        assertNull(result, "starts on the normal but the ray not on the plane");

        //TC10 starts on the normal and the ray on the plane
        result = p.findIntersections(new Ray(new Point(1, 1, 1), new Vector(0, -1, 1)));
        assertNull(result, "starts on the normal and the ray on the plane");

        //TC11 starts on the normal and the ray not on the plane
        result = p.findIntersections(new Ray(new Point(2, 1, 0), new Vector(1, 0, 0)));
        assertNull(result, "starts on the normal and the ray not on the plane");
    }

}
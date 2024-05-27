package geometries;

import org.example.geometries.Triangle;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Triangle.
 */
public class TriangleTest {
    /**
     * Test method for {@link Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        Point a = new Point(6, 8, 0);
        Point b = new Point(0, 0, 0);
        Point c = new Point(9, 0, 0);

        Triangle triangle = new Triangle(a, b, c);
        Vector normalizeVector = new Vector(0, 0, 1);
        Vector resultNormal = triangle.getNormal(a);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check normal in specific point. */
        assertTrue(normalizeVector.isSameNormal(resultNormal),
                "ERROR: getNormal() doesn't work correctly.");
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray intersects the triangle (1 point)
        assertEquals(List.of(new Point(1d / 3, 1d / 3, 1d / 3)),
                triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 1, 1))),
                "Ray intersects the triangle"
        );
        // TC02: outside against edge
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(-1, -1, -1))),
                "Ray outside against edge"
        );
        // TC03: outside against vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(-1, -1, 0))),
                "Ray outside against vertex"
        );
        // =============== Boundary Values Tests ==================
        // TC04: Ray intersects the triangle on edge
        assertNull(triangle.findIntersections(new Ray(new Point(0.5, 0.5, -1), new Vector(0, 0, 1))),
                "Ray intersects the triangle on edge"
        );
        // TC05: Ray intersects the triangle on vertex
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray intersects the triangle on vertex"
        );
        // TC06: Ray intersects the triangle on edge's continuation
        assertNull(triangle.findIntersections(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0))),
                "Ray intersects the triangle on edge's continuation"
        );


    }
}
package primitives;

import org.example.primitives.Point;
import org.example.primitives.Util;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Point.
 */
public class PointTest {
    /**
     * Test method for {@link Point#add(Vector)}.
     */
   // @Test
    @org.junit.jupiter.api.Test
    public void testAdd() {
        Point point = new Point(1, 2, 3);

        /* ============ Equivalence Partitions Tests ============== */
        /* TC01: Add valid point to valid vector. */
        assertEquals(point.add(new Vector(-1, -2, -3)), new Point(0, 0, 0),
                "ERROR: Point + Vector doesn't work correctly");
    }


    /**
     * Test method for {@link Point#subtract(Point)}.
     */
    @Test
    public void testSubtract() {
        Point point = new Point(1, 2, 3);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Subtract valid point to valid vector. */
        assertEquals(new Vector(1, 1, 1), new Point(2, 3, 4).subtract(point),
                "ERROR: Point - Subtract valid point to valid vector doesn't work correctly");

        /* =============== Boundary Values Tests ================== */

        /* TC02: Check if the zero vector result handle correctly */
        assertThrows(IllegalArgumentException.class,
                () -> point.subtract(point),
                "ERROR: Point - the zero vector result does not handle correctly");
    }

    /**
     * Test method for {@link Point#distance(Point)}.
     */
    @Test
    public void testDistance() {
        Point point1 = new Point(1, 2, 3);
        Point point2 = new Point(5, 4, 32);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check with valid points. */
        assertTrue(Util.isZero(point1.distance(point2) - 29.34280150224242),
                "ERROR: distance() doesn't work correctly");

        /* =============== Boundary Values Tests ================== */

        /* TC02: Check the distance to the same point. */
        assertTrue(Util.isZero(point1.distance(point1) - 0),
                "ERROR: distance() doesn't work correctly if they are the same point");
    }

    /**
     * Test method for {@link Point#distanceSquared(Point)}.
     */
    @Test
    public void distanceSquared() {
        /* ============ Equivalence Partitions Tests ============== */

        Point point1 = new Point(1, 2, 3);
        Point point2 = new Point(5, 4, 32);

        /* TC01: Check the distance squared between two valid points */
        assertTrue(Util.isZero(point1.distanceSquared(point2) - 861),
                "ERROR: distanceSquared() doesn't work correctly");

        /* =============== Boundary Values Tests ================== */

        /* TC02: Check the distance squared to the same point. */
        assertTrue(Util.isZero(point1.distanceSquared(point1) - 0),
                "ERROR: distanceSquared() doesn't work correctly if they are the same point");
    }
}
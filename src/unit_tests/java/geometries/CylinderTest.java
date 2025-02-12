package geometries;


import org.example.geometries.Cylinder;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing Cylinder.
 */
class CylinderTest {
    /**
     * Test method for {@link Cylinder#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {

        Vector direction = new Vector(0, 0, 1);
        Point p0 = new Point(0, 0, -1);
        Ray axis = new Ray(p0, direction);
        Cylinder cylinder = new Cylinder(axis, 2, 1);

        Point sidePoint = new Point(0, 1, 0);
        Vector exceptVectorSide = new Vector(0, 2, 0).normalize();

        Point topBaseCenterPoint = new Point(0, 0, 1);
        Vector exceptVectorCenterTopBase = new Vector(0, 0, 2).normalize();

        Point bottomBaseCenterPoint = new Point(0, 0, -1);
        Vector exceptVectorCenterBottomBase = new Vector(0, 0, -2).normalize();

        Point topBasePoint = new Point(-0.5, 0, 1);
        Vector exceptVectorTopBase = new Vector(0, 0, 1);

        Point bottomBasePoint = new Point(0.5, 0, -1);
        Vector exceptVectorBottomBase = new Vector(0, 0, -1);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: point on the sidePoint. */
        assertEquals(exceptVectorSide,
                cylinder.getNormal(sidePoint),
                "ERROR: getNormal() point on the side doesn't work correctly.");

        /* TC02: point the top base. */
        assertEquals(exceptVectorTopBase,
                cylinder.getNormal(topBasePoint),
                "ERROR: getNormal() point on the top base doesn't work correctly.");

        /* TC03: point the bottom base. */
        assertEquals(exceptVectorBottomBase,
                cylinder.getNormal(bottomBasePoint),
                "ERROR: getNormal() point on the top base doesn't work correctly.");

        /* =============== Boundary Values Tests ================== */

        /* TC04: point in the center the top base. */
        assertEquals(exceptVectorCenterTopBase,
                cylinder.getNormal(topBaseCenterPoint),
                "ERROR: getNormal() point in the center on the top base " +
                        "doesn't work correctly.");

        /* TC05: point in the center the bottom base. */
        assertEquals(exceptVectorCenterBottomBase,
                cylinder.getNormal(bottomBaseCenterPoint),
                "ERROR: getNormal() point in the center on the top base " +
                        "doesn't work correctly.");
    }

    /**
     * Test method for {@link Cylinder#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {

    }

}
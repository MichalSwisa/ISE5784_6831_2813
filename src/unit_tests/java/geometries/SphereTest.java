package geometries;

import org.example.geometries.Sphere;
import org.example.primitives.Point;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing Sphere.
 */
public class SphereTest {
    /**
     * Test method for {@link Sphere#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {
        Sphere sphere = new Sphere(new Point(0, 0, 0), 2);
        Vector normalizeVector = new Vector(1.73, 0, 1).normalize();
        Vector resultNormal = sphere.getNormal(new Point(1.73, 0, 1));

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check normal in specific point. */
        assertTrue(normalizeVector.isSameNormal(resultNormal),
                "ERROR: getNormal() doesn't work correctly.");
    }
}
package geometries;

import org.example.geometries.Tube;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testing Tube.
 */
public class TubeTest {
    /**
     * Test method for {@link Tube#getNormal(Point)}.
     */
    @Test
    public void testGetNormal() {
        Point p0 = new Point(0, 0, 0.5);
        Vector direction = new Vector(0, 0, 1);
        Ray ray = new Ray(p0, direction);
        Tube tube = new Tube(ray, 2);
        Vector exceptedVector = new Vector(0, -1, 0);
        Point point = new Point(0, -2, 2);
        Point point1 = new Point(0, -2, 0.5);

        /* TC01: normal situation normal vector to a point on the tube not paralleled to p0. */
        assertTrue(exceptedVector.isSameNormal(tube.getNormal(point)),
                "ERROR: getNormal() doesn't work correctly.");

        /* TC02: edge situation normal vector to a point on the tube paralleled to p0. */
        assertTrue(exceptedVector.isSameNormal(tube.getNormal(point1)),
                "ERROR: getNormal() doesn't work correctly when it's in the edge case.");

    }
}
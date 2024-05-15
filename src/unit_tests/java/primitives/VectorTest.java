package primitives;

import org.example.primitives.Double3;
import org.example.primitives.Point;
import org.example.primitives.Vector;
import org.junit.jupiter.api.Test;

import static org.example.primitives.Util.isZero;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Vector.
 */
class VectorTest {
    /**
     * Test method for {@link Vector#Vector(Double3)}.
     */
    @Test
    public void testConstructor() {
        /* =============== Boundary Values Tests ================== */

        /* TC01: Check if the zero vector result handle correctly */
        assertThrows(IllegalArgumentException.class, () -> new Vector(new Double3(0, 0, 0)),
                "ERROR: zero vector doesn't throw exception");
    }

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    public void testAdd() {

        Vector point1 = new Vector(1, 2, 3);
        Vector point2 = new Vector(-1, -2, -3);
        Vector point3 = new Vector(2, 4, 7);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Add valid point to valid vector. */
        assertEquals(new Vector(3, 6, 10), point1.add(point3),
                "ERROR: Vector + Vector does not work correctly");

        /* =============== Boundary Values Tests ================== */

        /* TC02: Check if the zero vector result handle correctly */
        assertThrows(IllegalArgumentException.class, () -> point1.add(point2),
                "ERROR: Vector + Vector doesn't work correctly in zero vector");
    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    public void testScale() {

        Vector point1 = new Vector(1, 2, 3);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check correct values. */
        assertEquals(new Vector(3.2, 6.4, 9.6), point1.scale(3.2),
                "ERROR: Vector * scalar does not work correctly");

        /* =============== Boundary Values Tests ================== */

        /* TC02: Check if the zero vector result handle correctly */
        assertThrows(IllegalArgumentException.class, () -> point1.scale(0),
                "ERROR: Vector * scalar doesn't work correctly in zero vector");
    }


    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     */
    @Test
    public void testDotProduct() {

        Vector vector1 = new Vector(1, 2, 3);
        Vector vector2 = new Vector(-2, -4, -6);
        Vector vector3 = new Vector(0, 3, -2);
        Vector vector4 = new Vector(7, 3, 1);

        Point pointA = new Point(1, 3, 9);
        Point pointB = new Point(-4, -7, 0);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: Check correct values. */
        assertTrue(isZero(vector1.dotProduct(vector4) - 16),
                "ERROR: dotProduct() failed with valid vectors wrong value");

        /* =============== Boundary Values Tests ================== */

        /* TC02: parallel vectors. */
        assertTrue(isZero(vector1.dotProduct(vector2) + 28),
                "ERROR: dotProduct() failed with parallel vectors wrong value");

        /* TC03: vectors with same direction. */
        Vector u = vector1.normalize();
        assertFalse(vector1.dotProduct(u) < 0,
                "ERROR: the normalized vector is opposite to the original one");


        /* TC04: vectors with reverse directions. */
        Vector va1 = pointA.subtract(pointB);
        Vector va2 = pointB.subtract(pointA);
        assertTrue(isZero(va1.dotProduct(va2) + 206),
                "ERROR: dotProduct() reverse direction vectors wrong value");


        /* TC05: vectors with obtuse angle between them. */
        assertTrue(isZero(vector3.dotProduct(new Vector(1, 1, 3)) + 3),
                "ERROR: dotProduct() obtuse angle between vectors wrong value");


        /* TC06: Orthogonal vectors. */
        assertTrue(isZero(vector1.dotProduct(vector3)),
                "ERROR: dotProduct() for orthogonal vectors is not zero");

        /* TC07: vectors with sharp angle between them. */
        assertTrue(isZero(vector3.dotProduct(new Vector(1, 2, -6)) - 18),
                "ERROR: dotProduct() wrong value");
    }


    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    public void testCrossProduct() {
        Point a = new Point(1, 3, 9);
        Point b = new Point(-4, -7, 0);

        Vector vector1 = new Vector(1, 2, 3);
        Vector vector2 = new Vector(-1, -2, -3);
        Vector vector3 = new Vector(0, 3, -2);

        Vector vr = vector1.crossProduct(vector3);
        Vector nVector = vector1.normalize();

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: dot product with sharp angle between vectors. */

        assertTrue(isZero(vr.length() - vector1.length() * vector3.length()),
                "ERROR: crossProduct() wrong result length");

        assertTrue(isZero(vr.dotProduct(vector1)) && isZero(vr.dotProduct(vector3)),
                "ERROR: crossProduct() result is not orthogonal to its operands");

        /* =============== Boundary Values Tests ================== */

        /* TC02: vectors with reverse directions. */
        Vector va1 = a.subtract(b);
        Vector va2 = b.subtract(a);
        assertThrows(IllegalArgumentException.class, () -> va1.crossProduct(va2),
                "ERROR: the normalized vector is not parallel to the original one");


        /* TC03: orthogonal vectors. */
        assertEquals(new Vector(-13, 2, 3), vector1.crossProduct(vector3),
                "ERROR: crossProduct() wrong value");


        /* TC04: vectors with obtuse angle between them. */
        assertEquals(new Vector(11, -2, -3), vector3.crossProduct(new Vector(1, 1, 3)),
                "ERROR: crossProduct() wrong value");

        /* TC05: vectors with sharp angle between them. */
        assertEquals(new Vector(-14, -2, -3), vector3.crossProduct(new Vector(1, 2, -6)),
                "ERROR: crossProduct() wrong value");

        /* TC06: vectors with same direction. */
        assertThrows(IllegalArgumentException.class, () -> vector1.crossProduct(nVector),
                "ERROR: the normalized vector is not parallel to the original one");


        /* TC07: parallel vectors. */
        assertThrows(IllegalArgumentException.class, () -> vector1.crossProduct(vector2),
                "ERROR: crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    public void testLengthSquared() {
        Vector vector = new Vector(1, 2, 3);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: check length of square vector. */
        assertTrue(isZero(vector.lengthSquared() - 14), "ERROR: lengthSquared() wrong value");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    public void testLength() {
        Vector vector = new Vector(0, 3, 4);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: check length of vector. */
        assertTrue(isZero(vector.length() - 5), "ERROR: length() wrong value");
    }


    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    public void testNormalize() {
        Vector vector1 = new Vector(1, 2, 3);

        /* ============ Equivalence Partitions Tests ============== */

        /* TC01: check if normalize of valid vector. */
        Vector nVector = vector1.normalize();
        assertTrue(isZero(nVector.length() - 1), "ERROR: the normalized vector is not a unit vector");
    }
}
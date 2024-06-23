package renderer;


import org.example.geometries.Geometry;
import org.example.geometries.Plane;
import org.example.geometries.Sphere;
import org.example.geometries.Triangle;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;
import org.example.renderer.Camera;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for Camera and Geometries.
 */
public class IntegrationTests {
    private final Camera.Builder builder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVpDistance(10);
    private Camera camera;

    /**
     * Calculates the number of intersections between a ray sent from the camera
     * and a given shape.
     *
     * @param camera The camera object
     * @param shape  The geometry shape to find intersections with
     * @return The total number of intersections found
     */
    int intersectionsSum(Camera camera, Geometry shape) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Constructing a ray through pixel [i,j]
                Ray ray = camera.constructRay(3, 3, i, j);
                // Finding intersections between the ray and the shape
                List<Point> points = shape.findIntersections(ray);
                if (points != null) {
                    count += points.size();
                }
            }
        }
        return count;
    }

    /**
     * Sphere integration test cases.
     */
    @Test
    void sphereTest() {
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);
        // TC01: Sphere is in front of the view plane, expecting 2 intersections
        assertEquals(2, intersectionsSum(camera, sphere), "Sphere is in front of the view plane");

        // TC02: Sphere is intersecting all view planes, expecting 18 intersections
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertEquals(18, intersectionsSum(camera, sphere), "Camera ray starts inside the sphere");

        // TC03: Sphere is partially intersecting the camera view, expecting 10 intersections
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, -2), 2);
        assertEquals(10, intersectionsSum(camera, sphere), "Camera ray starts inside the sphere");

        // TC04: View plane is inside the sphere, expecting 9 intersections
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 1))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(Point.ZERO, 4);
        assertEquals(9, intersectionsSum(camera, sphere), "View plane is inside the sphere");

        // TC05: Sphere is behind the camera, expecting 0 intersections
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, 1), 0.5);
        assertEquals(0, intersectionsSum(camera, sphere), "Sphere is behind the camera");
    }

    /**
     * Plane integration test cases.
     */
    @Test
    void planeTest() {
        // TC01: Plane is in front of the view plane, expecting 9 intersections
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        Plane plane = new Plane(new Point(0, 0, -2), new Vector(0, 0, 1));
        assertEquals(9, intersectionsSum(camera, plane), "Plane is in front of the view plane");

        // TC02: Plane is in an angle in front of the view plane, expecting 9 intersections
        plane = new Plane(new Point(1, 2, -3.5),
                new Point(1, 3, -3),
                new Point(4, 2, -3.5));
        assertEquals(9, intersectionsSum(camera, plane), "Plane is in an angle in front of the view plane");

        // TC03: Plane is in a different angle in front of the view plane, expecting 6 intersections
        plane = new Plane(new Point(1, 2, -3.5),
                new Point(1, 3, -2.5),
                new Point(4, 2, -3.5));
        assertEquals(6, intersectionsSum(camera, plane), "Plane is in a different angle in front of the view plane");
    }

    /**
     * Triangle integration test cases.
     */
    @Test
    void TriangleTest() {
        // TC01: Triangle is in front of the view plane, expecting 1 intersection
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        Triangle triangle = new Triangle(new Point(-1, -1, -2),
                new Point(1, -1, -2),
                new Point(0, 1, -2));
        assertEquals(1, intersectionsSum(camera, triangle), "Triangle is in front of the view plane");

        // TC02: Triangle is in front of the view plane at an angle, expecting 2 intersections
        triangle = new Triangle(new Point(-1, -1, -2),
                new Point(1, -1, -2),
                new Point(0, 20, -2));
        assertEquals(2, intersectionsSum(camera, triangle), "Triangle is in front of the view plane at an angle");
    }
}

package renderer;

import org.example.geometries.Geometry;
import org.example.geometries.Plane;
import org.example.geometries.Sphere;
import org.example.geometries.Triangle;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.renderer.Camera;
import org.junit.jupiter.api.Test;
import org.example.primitives.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

/* מבחני אינטגרציה (מצלמה וצורות) */
public class IntegrationTests {
    private final Camera.Builder builder = Camera.getBuilder()
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVpDistance(10);
    private Camera camera;

    /* חישוב מספר החיתוכים בין קרן שנשלחה ממצלמה לבין צורה
     * @param camera המצלמה
     * @param shape הצורה שאנו רוצים למצוא חיתוכים עימה
     * @return מספר החיתוכים
     */
    int intersectionsSum(Camera camera, Geometry shape) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // בניית קרן דרך הפיקסל [i,j]
                Ray ray = camera.constructRay(3, 3, i, j);
                // מציאת החיתוכים בין הקרן לצורה
                List<Point> points = shape.findIntersections(ray);
                if (points != null) {
                    count += points.size();
                }
            }
        }
        return count;
    }

    /* מבחן עבור כדור */
    @Test
    void sphereTest() {
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .build();
        Sphere sphere = new Sphere(new Point(0, 0, -3), 1);
        // TC01: הכדור נמצא מול מישור התצוגה 2 חיתוכים
        assertEquals(2, intersectionsSum(camera, sphere), "הכדור נמצא מול מישור התצוגה");

        // TC02: הכדור נמצא מול כל מישור התצוגה 18 חיתוכים
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, -2.5), 2.5);
        assertEquals(18, intersectionsSum(camera, sphere), "קרן המצלמה מתחילה בכדור ומצטלב איתו");

        // TC03: הכדור נמצא מול חלק מהמצלמה 10 חיתוכים
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, -2), 2);
        assertEquals(10, intersectionsSum(camera, sphere), "קרן המצלמה מתחילה בכדור ומצטלב איתו");

        // TC04: מישור התצוגה בתוך הכדור 9 חיתוכים
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 1))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(Point.ZERO, 4);
        assertEquals(9, intersectionsSum(camera, sphere), "מישור התצוגה נמצא בתוך הכדור");

        // TC05: הכדור מאחורי המצלמה 0 חיתוכים
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setLocation(new Point(0, 0, 0.5))
                .setVpDistance(1)
                .build();
        sphere = new Sphere(new Point(0, 0, 1), 0.5);
        assertEquals(0, intersectionsSum(camera, sphere), "הכדור נמצא מאחורי המצלמה");
    }

    /* מבחן עבור מישור */
    @Test
    void planeTest() {
        // TC01: המישור נמצא מול מישור התצוגה 9 חיתוכים
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)).setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        Plane plane = new Plane(new Point(0, 0, -2), new Vector(0, 0, 1));
        assertEquals(9, intersectionsSum(camera, plane), "המישור נמצא מול מישור התצוגה");

        // TC02: המישור נמצא מול מישור התצוגה בזווית 9 חיתוכים
        plane = new Plane(new Point(1, 2, -3.5),
                new Point(1, 3, -3),
                new Point(4, 2, -3.5));
        assertEquals(9, intersectionsSum(camera, plane), "המישור נמצא מול מישור התצוגה בזווית");

        // TC03: המישור נמצא מול מישור התצוגה בזווית 6 חיתוכים
        plane = new Plane(new Point(1, 2, -3.5),
                new Point(1, 3, -2.5),
                new Point(4, 2, -3.5));
        assertEquals(6, intersectionsSum(camera, plane), "המישור נמצא מול מישור התצוגה בזווית");
    }

    /* מבחן עבור משולש */
    @Test
    void TriangleTest() {
        // TC01: המשולש נמצא מול מישור התצוגה 1 חיתוך
        camera = builder.setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(3, 3)
                .setVpDistance(1)
                .setLocation(Point.ZERO)
                .build();
        Triangle triangle = new Triangle(new Point(-1, -1, -2),
                new Point(1, -1, -2),
                new Point(0, 1, -2));
        assertEquals(1, intersectionsSum(camera, triangle), "המשולש נמצא מול מישור התצוגה");

        // TC02: המשולש נמצא מול מישור התצוגה 2 חיתוכים
        triangle = new Triangle(new Point(-1, -1, -2),
                new Point(1, -1, -2),
                new Point(0, 20, -2));
        assertEquals(2, intersectionsSum(camera, triangle), "המשולש נמצא מול מישור התצוגה");
    }
}

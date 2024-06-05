package renderer;
import static java.awt.Color.*;
import org.example.geometries.Sphere;
import org.example.geometries.Triangle;
import org.example.lighting.AmbientLight;
import org.example.primitives.Double3;
import org.example.primitives.Point;
import org.example.primitives.Vector;
import org.example.primitives.Color;
import org.example.renderer.Camera;
import org.example.renderer.ImageWriter;


import org.example.renderer.SimpleRayTracer;
import org.example.scene.Scene;
import org.junit.jupiter.api.Test;

public class RendererTest {
    /** Scene of the tests */
    private final Scene scene = new Scene("Test scene");

    /** Camera builder of the tests */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(Point.ZERO).setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpDistance(100)
            .setVpSize(500, 500);

    /**
     * Produce a scene with a basic 3D model and render it into a PNG image with a grid.
     */
    @Test
    public void renderTwoColorTest() {
        scene.geometries.add(new Sphere(new Point(0, 0, -100), 50d ),
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100)), // down left
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down right
        scene.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE))
                .setBackground(new Color(75, 127, 90));

        camera
                .setImageWriter(new ImageWriter("base render test", 1000, 1000))
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage();
    }
}

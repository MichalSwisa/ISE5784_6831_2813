package renderer;


import org.example.geometries.Sphere;
import org.example.geometries.Triangle;
import org.example.lighting.AmbientLight;
import org.example.primitives.Color;
import org.example.primitives.Double3;
import org.example.primitives.Point;
import org.example.primitives.Vector;
import org.example.renderer.Camera;
import org.example.renderer.ImageWriter;
import org.example.renderer.SimpleRayTracer;
import org.example.scene.Scene;
import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class RenderTests {
    /**
     * Scene of the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder of the tests
     */
    private final Camera.Builder camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), Vector.Y)
            .setVpDistance(100)
            .setVpSize(500, 500);

    /**
     * Produce a scene with basic 3D model and render it into a png image with a
     * grid
     */
    @Test
    public void renderTwoColorTest() {
        scene.geometries.add(new Sphere(new Point(0, 0, -100), 50d),
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
                // left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100),
                        new Point(-100, -100, -100)), // down
                // left
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down
        scene.setAmbientLight(new AmbientLight(new Color(255, 191, 191), Double3.ONE))
                .setBackground(new Color(75, 127, 90));

        // right
        camera
                .setImageWriter(new ImageWriter("base render test", 1000, 1000))
                .build()
                .renderImage()
                .printGrid(100, new Color(YELLOW))
                .writeToImage();
    }

    // For stage 6 - please disregard in stage 5

    /**
     * Produce a scene with basic 3D model - including individual lights of the
     * bodies and render it into a png image with a grid
     */
    @Test
    public void renderMultiColorTest() {
        scene.geometries.add( // center
                new Sphere(new Point(0, 0, -100), 50),
                // up left
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                        .setEmission(new Color(GREEN)),
                // down left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                        .setEmission(new Color(RED)),
                // down right
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                        .setEmission(new Color(BLUE)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.2, 0.2, 0.2))); //

        camera
                .setImageWriter(new ImageWriter("color render test", 1000, 1000))
                .build()
                .renderImage()
                .printGrid(100, new Color(WHITE))
                .writeToImage();

    }
    ///** Test for XML based scene - for bonus */
    //@Test
    //public void basicRenderXml() {
    // enter XML file name and parse from XML file into scene object
    // using the code you added in appropriate packages
    // ...
    // NB: unit tests is not the correct place to put XML parsing code

    //    camera
    //           .setImageWriter(new ImageWriter("xml render test", 1000, 1000))
    //          .build()
    //          .renderImage()
    //          .printGrid(100, new Color(YELLOW))
    //          .writeToImage();
    //}
    // */
}

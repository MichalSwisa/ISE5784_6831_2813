package renderer;

import org.example.geometries.*;
import org.example.lighting.AmbientLight;
import org.example.lighting.DirectionalLight;
import org.example.lighting.PointLight;
import org.example.lighting.SpotLight;
import org.example.primitives.*;
import org.example.renderer.Camera;
import org.example.renderer.ImageWriter;
import org.example.renderer.SimpleRayTracer;
import org.example.scene.Scene;
import org.junit.jupiter.api.Test;

import static java.awt.Color.*;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), Vector.Y)
            .setRayTracer(new SimpleRayTracer(scene));

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        scene.geometries.add(
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setKt(new Double3(0.5, 0, 0))),
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(1)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
                .setVpSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }


    @Test
    public void generalPicture() {
        //scene.geometries.add(
        //      new Sphere(new Point(0, -10, -120), 10d).setEmission(new Color(BLUE))
        //            .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
        //        new Sphere(new Point(0, 0, -120), 25d).setEmission(new Color(RED))
        //              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),
        //new Tube(new Ray(new Point(10, 5, -70), new Vector(0, 0,-1)), 20d).setEmission(new Color(GREEN))
        //             .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(80)),
        // new Triangle(new Point(-120, -120, -85), new Point(-70, 70, -140), new Point(75, 75, -150))
        ///            .setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(60)));
        //scene.lights.add(
        //      new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
        //            .setKl(0.0004).setKq(0.0000006));
        //scene.lights.add(
        //      new DirectionalLight(new Color(400, 200, 0), new Vector(-1, -1, -1)));

        //cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
        //      .setVpSize(150, 150)
        //    .setImageWriter(new ImageWriter("general picture", 500, 500))
        //  .build()
        //.renderImage()
        //.writeToImage();


       //scene.geometries.add(
       //        new Sphere(new Point(-50, 0, -100), 50d).setEmission(new Color(0, 0, 255)) // Blue sphere
       //                .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
       //        new Sphere(new Point(50, 0, -100), 30d).setEmission(new Color(0, 255, 0)) // Green sphere
       //                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKr(0.5)),
       //        new Polygon(new Point(-60, -50, -150), new Point(60, -50, -150), new Point(60, -50, -50), new Point(-60, -50, -50)) // Colorful polygon
       //                .setEmission(new Color(50, 100, 150))
       //                .setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(50).setKr(0.3)),
       //        new Polygon(new Point(-200, -50, -150), new Point(200, -50, -150), new Point(100, -50, 50), new Point(-100, -50, 50)) // Colorful polygon
       //                .setEmission(new Color(50, 100, 150))
       //                .setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(50).setKr(0.3)),
       //        new Triangle(new Point(-20, -50, -100), new Point(20, -50, -100), new Point(0, 50, -100)) // Orange prism
       //                .setEmission(new Color(255, 165, 0)) // Orange color
       //                .setMaterial(new Material().setKd(0.4).setKs(0.4).setShininess(90)));

       //scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

       //scene.lights.add(
       //        new DirectionalLight(new Color(400, 300, 300), new Vector(-1, -1, -1)));
       //scene.lights.add(
       //        new PointLight(new Color(500, 300, 0), new Point(50, 50, 50))
       //                .setKl(0.0005).setKq(0.0005));
       //scene.lights.add(
       //        new SpotLight(new Color(600, 400, 400), new Point(-50, -50, 25), new Vector(1, 1, -2))
       //                .setKl(0.0001).setKq(0.0001));

       //cameraBuilder.setLocation(new Point(0, 0, 300)).setVpDistance(300)
       //        .setVpSize(200, 200)
       //        .setImageWriter(new ImageWriter("general picture", 500, 500))
       //        .build()
       //        .renderImage()
       //        .writeToImage();

        scene.geometries.add(
                new Sphere(new Point(-50, 0, -100), 50d).setEmission(new Color(0, 0, 255)) // Blue sphere
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(new Point(50, 0, -100), 30d).setEmission(new Color(0, 255, 0)) // Green sphere
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100).setKr(0.5)),
                new Polygon(new Point(-60, -50, -150), new Point(60, -50, -150), new Point(60, -50, -50), new Point(-60, -50, -50)) // Colorful polygon
                        .setEmission(new Color(50, 100, 150))
                        .setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(50).setKr(0.3)),
                new Polygon(new Point(-200, -50, -150), new Point(200, -50, -150), new Point(100, -50, 50), new Point(-100, -50, 50)) // Colorful polygon
                        .setEmission(new Color(50, 100, 150))
                        .setMaterial(new Material().setKd(0.3).setKs(0.3).setShininess(50).setKr(0.3)),
                new Triangle(new Point(-20, -50, -100), new Point(20, -50, -100), new Point(0, 50, -100)) // Orange prism
                        .setEmission(new Color(255, 165, 0)) // Orange color
                        .setMaterial(new Material().setKd(0.4).setKs(0.4).setShininess(90)));

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.lights.add(
                new DirectionalLight(new Color(400, 300, 300), new Vector(-1, -1, -1)));
        scene.lights.add(
                new PointLight(new Color(500, 300, 0), new Point(50, 50, 50))
                        .setKl(0.0005).setKq(0.0005));
        scene.lights.add(
                new SpotLight(new Color(300, 300, 300), new Point(-50, 50, 25), new Vector(1, -1, -2))
                        .setKl(0.0001).setKq(0.0001));
        scene.lights.add(
                new SpotLight(new Color(600, 400, 400), new Point(-50, -50, 25), new Vector(1, 1, -2))
                        .setKl(0.0001).setKq(0.0001));

        cameraBuilder.setLocation(new Point(0, 0, 300)).setVpDistance(300)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("general picture", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }
}

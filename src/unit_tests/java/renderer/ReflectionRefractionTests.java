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

    static final int DEFAULT_BEAM_RAYS = 16;
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");
    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), Vector.Y)
            .setRayTracer(new SimpleRayTracer(scene)/*.setAdaptiveGrid(true).setMaxLevel(3)*/);

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 200d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(new Point(0, 0, -50), 100d).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .ifImprovment(false)
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

        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .ifImprovment(true)
                .setBeamRays(100, 100)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("general picture", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }


    @Test
    public void miniProj1() {
        //scene.geometries.add(new Tube(new Ray(new Point(0, 0, 550), new Vector(1,-1,0) ),20d));
        scene.geometries.add(
                new Sphere(new Point(0, 12.5, -350), 65)
                        .setEmission(new Color(RED/*139, 69, 19*/ //brown
                                /*173, 216, 230*///lite blue
                                //0, 128, 0 //green
                                // 30, 144, 255 //blue
                        ))
                        .setMaterial(new Material().setKd(0.2).setKs(0.5).setShininess(100).setKt(0.1).setKr(0.4)),
                new Sphere(new Point(0, 12.5, -398), 70)
                        .setEmission(new Color(0, 0, 0 //brown
                                /*173, 216, 230*///lite blue
                                //0, 128, 0 //green
                                // 30, 144, 255 //blue
                        )),
                new Sphere(new Point(0, 12.5, -300), 20)
                        .setEmission(new Color(0, 0, 0 //brown
                                /*173, 216, 230*///lite blue
                                //0, 128, 0 //green
                                // 30, 144, 255 //blue
                        )).setMaterial(new Material().setKr(0.7)),

                //eye+
                new Polygon(
                        new Point(-150, 0, -399),   // Leftmost point
                        new Point(-120, 30, -399),  // Upper left curve
                        new Point(-90, 55, -399),   // Upper left
                        new Point(-50, 70, -399),   // Upper left
                        new Point(-20, 80, -399),   // Upper center-left
                        new Point(20, 80, -399),    // Upper center-right
                        new Point(50, 70, -399),    // Upper right
                        new Point(90, 55, -399),    // Upper right
                        new Point(120, 30, -399),   // Upper right curve
                        new Point(150, 0, -399),    // Rightmost point
                        new Point(120, -30, -399),  // Lower right curve
                        new Point(50, -55, -399),   // Lower right
                        new Point(-40, -55, -399),  // Lower left
                        new Point(-120, -30, -399)  // Lower left curve

                ).setEmission(new Color(245, 245, 245/*RED*/))


        );


        // Add cylinders to simulate lines in the iris
        int numCylinders = 12; // Number of lines
        double radius = 22; // Radius of the iris
        double height = 45; // Height of each cylinder (line length)
        double cylinderRadius = 1; // Thickness of each line

        for (int i = 0; i < numCylinders; i++) {
            double angle = 2 * Math.PI * i / numCylinders;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            Point start = new Point(x, y + 12.5, -300);
            Vector direction = new Vector(-x, -y, 50).normalize(); // Direction of the line
            scene.geometries.add(
                    new Cylinder(new Ray(start, direction), height, cylinderRadius)
                            .setEmission(new Color(0, 0, 0)) // black
            );
        }
        scene.setAmbientLight(new AmbientLight(new Color(ORANGE), 0.2));

        scene.lights.add(
                new DirectionalLight(new Color(YELLOW), new Vector(1, -1, 0)));
        scene.lights.add(
                new DirectionalLight(new Color(YELLOW), new Vector(1, 1, -1)));
        scene.lights.add(
                new PointLight(new Color(500, 300, 0), new Point(-100, 100, -90))
                        .setKl(0.0005).setKq(0.0005));
        scene.lights.add(
                new SpotLight(new Color(YELLOW), new Point(-100, 100, -200), new Vector(1, -1, -2))
                        .setKl(0.0001).setKq(0.0001));
        scene.lights.add(
                new SpotLight(new Color(600, 400, 400), new Point(-50, -50, 25), new Vector(1, 1, -2))
                        .setKl(0.0001).setKq(0.0001));

        cameraBuilder.setLocation(new Point(0, 0, 500)).setVpDistance(500)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("mini project 1", 500, 500))
                .ifImprovment(true)
                .setBeamRays(DEFAULT_BEAM_RAYS, DEFAULT_BEAM_RAYS)
                .setMultithreading(8)
                .setDebugPrint(1.0)
                .build()
                .renderImage()
                .writeToImage();
    }

    @Test
    public void newPic() {
        scene.geometries.add(
                // Sun
                new Sphere(new Point(-100, 100, -99), 20d).setEmission(new Color(YELLOW))
                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(500).setKt(0.1).setKr(0.3)),
                // Grass
                new Polygon(new Point(-200, -250, -100), new Point(200, -250, -100), new Point(200, -20, -100), new Point(-200, -20, -100))
                        .setEmission(new Color(GREEN)),
                // Sky
                new Polygon(new Point(-200, -20, -100), new Point(200, -20, -100), new Point(200, 230, -100), new Point(-200, 230, -100))
                        .setEmission(new Color(BLUE)),
                // Roof
                new Triangle(new Point(0, 0, 0), new Point(100, 0, 0), new Point(50, 50, 0))
                        .setEmission(new Color(RED)),
                // House
                new Polygon(new Point(10, -50, 0), new Point(90, -50, 0), new Point(90, 0, 0), new Point(10, 0, 0))
                        .setEmission(new Color(150, 75, 0)),
                // First flower
                new Cylinder(new Ray(new Point(-55, -50, 50), new Vector(0, 1, 0)), 30d, 0.5d)
                        .setEmission(new Color(34, 139, 34)),
                new Sphere(new Point(-55, -20, 50), 5d)
                        .setEmission(new Color(255, 215, 0)),
                new Sphere(new Point(-52.5, -20, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-57.5, -20, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-55, -22.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-55, -17.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                // Second flower
                new Cylinder(new Ray(new Point(-40, -40, 50), new Vector(0, 1, 0)), 30d, 0.5d)
                        .setEmission(new Color(34, 139, 34)),
                new Sphere(new Point(-40, -10, 50), 5d)
                        .setEmission(new Color(255, 215, 0)),
                new Sphere(new Point(-37.5, -10, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-42.5, -10, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-40, -12.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-40, -7.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                // Third flower
                new Cylinder(new Ray(new Point(-40, -60, 50), new Vector(0, 1, 0)), 30d, 0.5d)
                        .setEmission(new Color(34, 139, 34)),
                new Sphere(new Point(-40, -30, 50), 5d)
                        .setEmission(new Color(255, 215, 0)),
                new Sphere(new Point(-37.5, -30, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-42.5, -30, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-40, -32.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-40, -27.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                // Fourth flower
                new Cylinder(new Ray(new Point(-25, -50, 50), new Vector(0, 1, 0)), 30d, 0.5d)
                        .setEmission(new Color(34, 139, 34)),
                new Sphere(new Point(-25, -20, 50), 5d)
                        .setEmission(new Color(255, 215, 0)),
                new Sphere(new Point(-22.5, -20, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-27.5, -20, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-25, -22.5, 50), 5d)
                        .setEmission(new Color(PINK)),
                new Sphere(new Point(-25, -17.5, 50), 5d)
                        .setEmission(new Color(PINK))
        );

        // Set ambient light
        scene.setAmbientLight(new AmbientLight(new Color(ORANGE), 0.2));

        // Add lights to the scene
        scene.lights.add(
                new DirectionalLight(new Color(YELLOW), new Vector(1, -1, 0))
        );
        scene.lights.add(
                new PointLight(new Color(500, 300, 0), new Point(-100, 100, -90))
                        .setKl(0.0005).setKq(0.0005)
        );
        scene.lights.add(
                new SpotLight(new Color(YELLOW), new Point(-100, 100, -200), new Vector(1, -1, -2))
                        .setKl(0.0001).setKq(0.0001)
        );
        scene.lights.add(
                new SpotLight(new Color(600, 400, 400), new Point(-50, -50, 25), new Vector(1, 1, -2))
                        .setKl(0.0001).setKq(0.0001)
        );

        // Configure camera and render image
        cameraBuilder.setLocation(new Point(0, 0, 300))
                .setVpDistance(300)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("newPic", 500, 500))
                //.ifImprovment(true)  // Uncomment if needed
                //.setBeamRays(50)     // Uncomment if needed
                .build()
                .renderImage()
                .writeToImage();
    }
//        scene.geometries.add(
//
//                //sun
//                new Sphere(new Point(-100, 100, -99), 20d).setEmission(new Color(YELLOW))
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(500).setKt(0.1).setKr(0.3)),
//                        // שמש sphere
//               //grass
//                new Polygon(new Point(-200, -250, -100), new Point(200, -250, -100), new Point(200, -20, -100), new Point(-200, -20, -100)) // Colorful polygon
//                        .setEmission(new Color(GREEN)),//דשא
//                //sky
//                new Polygon(new Point(-200, -20, -100), new Point(200, -20, -100), new Point(200, 230, -100), new Point(-200, 230, -100)) // Colorful polygon
//                        .setEmission(new Color(BLUE)),
//                //roof
//                new Triangle(new Point(0, 0, 0), new Point(100, 0, 0), new Point(50, 50, 0)) // Orange prism
//                        .setEmission(new Color(RED)),//roof
//                //home
//                new Polygon(new Point(10, -50, 0), new Point(90, -50, 0), new Point(90, 0, 0), new Point(10, 0, 0)) // Base of the house
//                        .setEmission(new Color(150, 75, 0)),
//           //first flower
//                // יצירת הגבעול
//                new Cylinder(new Ray(new Point(-55, -50, 50), new Vector(0, 1, 0)), 30d, 0.5d)
//                        .setEmission(new Color(34, 139, 34)), // ירוק כהה
//                       // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(30)),
//                // יצירת מרכז הפרח
//                new Sphere(new Point(-55, -20, 50), 5d)
//                        .setEmission(new Color(255, 215, 0)),
//                       // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                // יצירת עלי כותרת
//                new Sphere(new Point(-52.5, -20, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-57.5, -20, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                       // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-55, -22.5, 50), 5d)
//                        .setEmission(new Color(PINK)),
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-55, -17.5, 50), 5d)
//                        .setEmission(new Color(PINK)),
//                        // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//// second flower
//                // יצירת הגבעול
//                new Cylinder(new Ray(new Point(-40, -40, 50), new Vector(0, 1, 0)), 30d, 0.5d)
//                .setEmission(new Color(34, 139, 34)), // ירוק כהה
//                //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(30)),
//                // יצירת מרכז הפרח
//                new Sphere(new Point(-40, -10, 50), 5d)
//                        .setEmission(new Color(255, 215, 0)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                // יצירת עלי כותרת
//                new Sphere(new Point(-37.5, -10, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-42.5, -10, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-40, -12.5, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-40, -7.5, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
////third flower
//                // יצירת הגבעול
//                new Cylinder(new Ray(new Point(-40, -60, 50), new Vector(0, 1, 0)), 30d, 0.5d)
//                        .setEmission(new Color(34, 139, 34)), // ירוק כהה
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(30)),
//                // יצירת מרכז הפרח
//                new Sphere(new Point(-40, -30, 50), 5d)
//                        .setEmission(new Color(255, 215, 0)),// זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                // יצירת עלי כותרת
//                new Sphere(new Point(-37.5, -30, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-42.5, -30, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-40, -32.5, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-40, -27.5, 50), 5d)
//                        .setEmission(new Color(PINK)), // זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//
////forth flower
//                // יצירת הגבעול
//                new Cylinder(new Ray(new Point(-25, -50, 50), new Vector(0, 1, 0)), 30d, 0.5d)
//                        .setEmission(new Color(34, 139, 34)), // ירוק כהה
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(30)),
//                // יצירת מרכז הפרח
//                new Sphere(new Point(-25, -20, 50), 5d)
//                        .setEmission(new Color(255, 215, 0)), //זהב
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                // יצירת עלי כותרת
//                new Sphere(new Point(-22.5, -20, 50), 5d)
//                        .setEmission(new Color(PINK)),
//                       // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-27.5, -20, 50), 5d)
//                        .setEmission(new Color(PINK)) ,
//                       // .setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-25, -22.5, 50), 5d)
//                        .setEmission(new Color(PINK)),
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50)),
//                new Sphere(new Point(-25, -17.5, 50), 5d)
//                        .setEmission(new Color(PINK))
//                        //.setMaterial(new Material().setKd(0.5).setKs(0.3).setShininess(50))
//        );
//
//
//        scene.setAmbientLight(new AmbientLight(new Color(   ORANGE), 0.2));
//
//        scene.lights.add(
//                new DirectionalLight(new Color(YELLOW), new Vector(1, -1, 0)));
//        scene.lights.add(
//                new PointLight(new Color(500, 300, 0), new Point(-100, 100, -90))
//                      .setKl(0.0005).setKq(0.0005));
//        scene.lights.add(
//                new SpotLight(new Color(YELLOW), new Point(-100, 100, -200), new Vector(1, -1, -2))
//                        .setKl(0.0001).setKq(0.0001));
//        scene.lights.add(
//                new SpotLight(new Color(600, 400, 400), new Point(-50, -50, 25), new Vector(1, 1, -2))
//                        .setKl(0.0001).setKq(0.0001));
//
//        cameraBuilder.setLocation(new Point(0, 0, 300)).setVpDistance(300)
//                .setVpSize(200, 200)
//                .setImageWriter(new ImageWriter("new pic", 500, 500))
//                .ifImprovment(true)
//                .setBeamRays(50)
//                .build()
//                .renderImage()
//                .writeToImage();
//    }
}


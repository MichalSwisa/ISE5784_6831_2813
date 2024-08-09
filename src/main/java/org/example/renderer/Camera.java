package org.example.renderer;

import org.example.primitives.Color;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import static org.example.primitives.Util.isZero;

public class Camera implements Cloneable {
    // Constants for exception messages
    private static final String MISSING_RENDER_DATA = "Missing rendering data";
    private static final String CAMERA_CLASS_NAME = "Camera";
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private Point position;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double viewPlaneWidth = 0.0;
    private double viewPlaneHeight = 0.0;
    private double viewPlaneDistance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTrace;
    private int lineBeamRays = 1;
    private int distance = 1;
    private double width = -1d;
    private double height = -1d;
    private boolean improvment = false;
    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads
    private double printInterval = 0; // printing progress percentage interval


    /**
     * Private default constructor for Camera.
     * Initializes the camera with default values.
     */
    private Camera() {
        this.position = new Point(0, 0, 0);
        this.vTo = new Vector(0, 0, -1);
        this.vUp = new Vector(0, 1, 0);
        this.vRight = new Vector(1, 0, 0);
    }

    /* Returns a new instance of the Builder class.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }


    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
     *
     * @param nX number of columns (width resolution)
     * @param nY number of rows (height resolution)
     * @param j  column index of the pixel
     * @param i  row index of the pixel
     * @return the constructed ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pCenter = position.add(vTo.scale(viewPlaneDistance));
        double rX = viewPlaneWidth / nX;
        double rY = viewPlaneHeight / nY;

        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        Point pIJ = pCenter;
        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }

        Vector vIJ = pIJ.subtract(position).normalize();

        return new Ray(position, vIJ);
    }

    /**
     * Find a ray from p0 to the center of the pixel from the given resolution.
     *
     * @param nX     the number of the rows
     * @param nY     the number of the columns
     * @param column column
     * @param row    row
     * @return ray from p0 the center to the center of the pixel in row column
     */
    public List<Ray> constructBeamRays(int nX, int nY, int column, int row) {
        if (lineBeamRays == 1) {
            return List.of(constructRay(nX, nY, column, row));
        }
        Point pointCenter = position.add(vTo.scale(viewPlaneDistance));
        double ratioY = viewPlaneHeight / nY;
        double ratioX = viewPlaneWidth / nX;

        double yI = (row - (nY - 1) / 2d) * ratioY;
        double xJ = (column - (nX - 1) / 2d) * ratioX;

        Point pointCenterPixel = pointCenter;
        if (!isZero(xJ)) {
            pointCenterPixel = pointCenterPixel.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pointCenterPixel = pointCenterPixel.add(vUp.scale(-yI));
        }

        List<Ray> rays = new LinkedList<>();
        double rY = ratioY / lineBeamRays;
        double rX = ratioX / lineBeamRays;
        for (int internalRow = 0; internalRow < lineBeamRays; internalRow++) {
            for (int internalColumn = 0; internalColumn < lineBeamRays; internalColumn++) {
                double ySampleI = (internalRow - (lineBeamRays - 1) / 2.0) * rY;
                double xSampleJ = (internalColumn - (lineBeamRays - 1) / 2.0) * rX;
                Point pIJ = pointCenterPixel;
                if (!isZero(xSampleJ)) {
                    pIJ = pIJ.add(vRight.scale(xSampleJ));
                }
                if (!isZero(ySampleI)) {
                    pIJ = pIJ.add(vUp.scale(ySampleI));
                }
                Ray ray = new Ray(position, pIJ.subtract(position).normalize());

                rays.add(ray);
            }
        }
        return rays;
    }

    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning of Camera failed", e);
        }
    }

    //public List<Point> findIntersections(int i, int i1, Sphere sphereTC01) {}
    /* Calculates a color for a specific pixel in an image.
     *
     * @param nX The number of pixels in a row in the view plane.
     * @param nY The number of pixels in a column in the view plane.
     * @param j  The row number of the pixel.
     * @param i  The column number of the pixel.
     */
    private void castRay(int nX, int nY, int row, int column, List<Ray> rays) {
        if (rays == null) {
            this.imageWriter.writePixel(/*row*/column, /*column*/row, rayTrace.traceRay(constructRay(nX, nY, /*row*/column, /*column*/row)));
            Pixel.pixelDone();
        } else {
            this.imageWriter.writePixel(/*row*/column, /*column*/row, rayTrace.traceRay(rays));
            Pixel.pixelDone();
        }
    }

    /**
     * Renders the image by casting rays from the camera through each pixel of the image and writing the resulting color to the imageWriter.
     * Throws UnsupportedOperationException if any of the required resources are missing (rayTracerBase, imageWriter, width, height, distance).
     */
    public Camera renderImage() {
        if (this.rayTrace == null || this.imageWriter == null || this.viewPlaneWidth == 0 || this.viewPlaneHeight == 0 || this.viewPlaneDistance == 0)
            throw new UnsupportedOperationException("MissingResourcesException");
        List<Ray> rays = null;
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        Pixel.initialize(nY, nX, printInterval);

        if (threadsCount == 0){
            for (int row = 0; row < nY; row++)
                for (int column = 0; column < nX; column++) {
                    if (improvment) {
                        rays = constructBeamRays(imageWriter.getNx(),
                                imageWriter.getNy(),
                                /*row*/column,
                                /*column*/row);
                    }

                    this.castRay(nX, nY, row, column, rays); //this.castRay(nX, nY, row, column, rays);
                }
        } else if (threadsCount == -1) {
            IntStream.range(0, nY)
                     .parallel()
                    .forEach(row -> IntStream.range(0, nX).parallel().forEach(
                            column -> {
                                List<Ray> raysConstruct = null;
                                if (improvment) {
                                    raysConstruct = constructBeamRays(imageWriter.getNx(),
                                            imageWriter.getNy(),
                                            /*row*/column,
                                            /*row*/row);
                                }
                                castRay(nX, nY, row, column, raysConstruct);

                            }
                    ));


        }  else{
            List<Thread> threads = new LinkedList<>();
            for (int i = 0; i < threadsCount; i++) {
                threads.add(new Thread(() -> {
                    Pixel pixel;
                    while ((pixel = Pixel.nextPixel()) != null){
                        List<Ray> raysConstruct = null;
                        if (improvment) {
                            raysConstruct = constructBeamRays(imageWriter.getNx(),
                                    imageWriter.getNy(),
                                    /*row*/pixel.col(),
                                    /*row*/pixel.row());
                        }
                        castRay(nX, nY, pixel.row(), pixel.col(), raysConstruct);
                    }
                }));

            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) {

                }
            }
        }


        return this;
    }


    /**
     * Draws a grid on the image by writing a specified color to the pixels that fall on the grid lines.
     * Throws UnsupportedOperationException if imageWriter object is null.
     *
     * @param interval The spacing between grid lines.
     * @param color    The color to use for the grid lines.
     */
    public Camera printGrid(int interval, Color color) throws MissingResourceException, IllegalArgumentException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);

        if (this.imageWriter.getNx() % interval != 0 || this.imageWriter.getNy() % interval != 0)
            throw new IllegalArgumentException("The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny");

        for (int i = 0; i < this.imageWriter.getNx(); i++)
            for (int j = 0; j < this.imageWriter.getNy(); j++)
                if (i % interval == 0 || (i + 1) % interval == 0 || j % interval == 0 || (j + 1) % interval == 0)
                    this.imageWriter.writePixel(i, j, color);
        return this;
    }

    /**
     * Writes pixels to final image by delegating to the ImageWriter
     */
    public Camera writeToImage()//מכתיבה את התמונה לתוך הקובץ שנקבע על ידי ImageWriter.
    {
        if (imageWriter == null)
            throw new MissingResourceException("Image writer was null", ImageWriter.class.getCanonicalName(), "");
        imageWriter.writeToImage();
        return this;
    }//ה • מתודה writeToImage שתאציל (delegation (את תפקידה לאובייקט ה-ImageWriter על מðת לשמור על חוק דמטר בטסטרים

    /* "Printing" the image - producing an unoptimized png file of the image
     * @throws MissingResourceException The render's field imageWriter mustn't be null
            /
    public Camera writeToImage() throws MissingResourceException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
        this.imageWriter.writeToImage();
        return this;
    }

    /**
     * Builder class for constructing Camera objects.
     */
    public static class Builder {
        final private Camera camera;

        /**
         * Default constructor for Builder. Initializes with a new Camera instance.
         */
        public Builder() {
            this.camera = new Camera();
        }

        /**
         * Constructor for Builder with an existing Camera instance.
         *
         * @param camera the Camera instance to initialize the builder with.
         */
        public Builder(Camera camera) {
            this.camera = camera;
        }

        /**
         * Sets the location of the camera.
         *
         * @param position the position of the camera.
         * @return the current Builder instance.
         * @throws IllegalArgumentException if the position is null.
         */
        public Builder setLocation(Point position) {
            if (position == null) {
                throw new IllegalArgumentException("Camera position cannot be null.");
            }
            this.camera.position = position;
            return this;
        }

        public Builder ifImprovment(boolean flag) {
            this.camera.improvment = flag;
            return this;
        }

        /**
         * Sets the number of beam rays for the camera's line rendering.
         *
         * @param beamRays The total number of beam rays to set.
         * @return This Camera object with the updated beam rays configuration.
         */
        public Builder setBeamRays(int beamRays) {
            if (this.camera.improvment) {
                this.camera.lineBeamRays = (int) Math.sqrt(beamRays);
                if (!isZero(this.camera.lineBeamRays - Math.sqrt(beamRays))) {
                    this.camera.lineBeamRays += 1;
                }
            }
            return this;
        }














        public Builder setMultithreading(int threads) {
            if (threads < -2) throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1) this.camera.threadsCount = threads;
            else if (threads == -2) {
                int cores = Runtime.getRuntime().availableProcessors() - this.camera.SPARE_THREADS;
                this.camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        public Builder setDebugPrint(double interval) {
            this.camera.printInterval = interval;
            return this;
        }

        public Camera renderImage() {
            final int nX = this.camera.imageWriter.getNx();
            final int nY = this.camera.imageWriter.getNy();
            Pixel.initialize(nY, nX, this.camera.printInterval);

            if (this.camera.threadsCount == 0) {
                for (int i = 0; i < nY; ++i) {
                    for (int j = 0; j < nX; ++j) {
                        this.camera.castRay(nX, nY, j, i, null);
                    }
                }
            } else {
                renderImageThreaded();
            }
            return this.camera;
        }

        private void renderImageThreaded() {
            final int nX = this.camera.imageWriter.getNx();
            final int nY = this.camera.imageWriter.getNy();

            IntStream.range(0, this.camera.threadsCount).forEach(i -> {
                new Thread(() -> {
                    for (int row = i; row < nY; row += this.camera.threadsCount) {
                        for (int col = 0; col < nX; ++col) {
                            this.camera.castRay(nX, nY, col, row, null);
                        }
                    }
                }).start();
            });
        }

        //private void castRay(int nX, int nY, int col, int row) {
        //    this.camera.imageWriter.writePixel(col, row, tracer.traceRay(constructRay(nX, nY, col, row)));
        //    Pixel.pixelDone();
        //}








        /**
         * Sets the direction of the camera.
         *
         * @param vTo the forward direction vector.
         * @param vUp the upward direction vector.
         * @return the current Builder instance.
         * @throws IllegalArgumentException if vTo or vUp are null or not perpendicular.
         */

        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null.");
            }
            if (!isPerpendicular(vTo, vUp)) {
                throw new IllegalArgumentException("Direction vectors must be perpendicular.");
            }
            this.camera.vTo = vTo.normalize();
            this.camera.vUp = vUp.normalize();
            this.camera.vRight = vTo.crossProduct(vUp).normalize();
            return this;
        }

        /**
         * Sets the size of the view plane.
         *
         * @param width  the width of the view plane.
         * @param height the height of the view plane.
         * @return the current Builder instance.
         * @throws IllegalArgumentException if width or height are not positive.
         */
        public Builder setVpSize(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be positive.");
            }
            this.camera.viewPlaneWidth = width;
            this.camera.viewPlaneHeight = height;
            return this;
        }

        /**
         * Sets the distance from the camera to the view plane.
         *
         * @param distance the distance to the view plane.
         * @return the current Builder instance.
         * @throws IllegalArgumentException if distance is not positive.
         */
        public Builder setVpDistance(double distance) {
            if (distance <= 0) {
                throw new IllegalArgumentException("View plane distance must be positive.");
            }
            this.camera.viewPlaneDistance = distance;
            return this;
        }

        public Builder setPosition(Point position) {
            this.camera.position = position;
            return this;
        }

        /**
         * setter for image writer.
         *
         * @param imageWriter image writer.
         * @return the Camera.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Setter for ray tracer base.
         *
         * @param rayTracerBase Ray tracer base.
         * @return The Camera.
         */
        public Builder setRayTracer(RayTracerBase rayTracerBase) {
            camera.rayTrace = rayTracerBase;
            return this;
        }

        /**
         * Builds and returns the Camera instance.
         *
         * @return the constructed Camera instance.
         */
        public Camera build() {
            validateCamera();
            return (Camera) camera.clone();
        }

        /**
         * Validates that all required fields of the camera are set.
         *
         * @throws MissingResourceException if any required field is not set.
         */
        private void validateCamera() {
            if (camera.position == null) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, "position");
            }
            if (camera.vTo == null || camera.vUp == null || camera.vRight == null) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, "direction vectors");
            }
            if (camera.viewPlaneWidth <= 0) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, "viewPlaneWidth");
            }
            if (camera.viewPlaneHeight <= 0) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, "viewPlaneHeight");
            }
            if (camera.viewPlaneDistance <= 0) {
                throw new MissingResourceException(MISSING_RENDER_DATA, CAMERA_CLASS_NAME, "viewPlaneDistance");
            }
        }

        /**
         * Checks if two vectors are perpendicular.
         *
         * @param v1 the first vector.
         * @param v2 the second vector.
         * @return true if the vectors are perpendicular, false otherwise.
         */
        private boolean isPerpendicular(Vector v1, Vector v2) {
            return isZero(v1.dotProduct(v2));
        }
    }

}
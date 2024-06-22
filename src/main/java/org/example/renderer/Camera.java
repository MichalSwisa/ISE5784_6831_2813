package org.example.renderer;


import org.example.primitives.*;


import java.util.MissingResourceException;

public class Camera implements Cloneable {
    // Constants for exception messages
    private static final String MISSING_RENDER_DATA = "Missing rendering data";
    private static final String CAMERA_CLASS_NAME = "Camera";
    private Point position;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double viewPlaneWidth = 0.0;
    private double viewPlaneHeight = 0.0;
    private double viewPlaneDistance = 0.0;
    private ImageWriter imageWriter;
    private RayTracerBase rayTrace;

    /* Private default constructor for Camera.
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

    /* Constructs a ray from the camera through a specific pixel on the view plane.
            * @param nX number of columns (width resolution)
     * @param nY number of rows (height resolution)
     * @param j  column index of the pixel
     * @param i  row index of the pixel
     * @return the constructed ray
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        // Calculate the center of the view plane
        Point pCenter = position.add(vTo.scale(viewPlaneDistance));

        // Calculate the pixel size
        double rX = viewPlaneWidth / nX;
        double rY = viewPlaneHeight / nY;

        // Calculate the pixel center
        double xJ = (j - (nX - 1) / 2.0) * rX;
        double yI = -(i - (nY - 1) / 2.0) * rY;

        Point pIJ = pCenter;

        if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));

        Vector vIJ = pIJ.subtract(position).normalize();

        return new Ray(position, vIJ);
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
    private void castRay(int nX, int nY, int j, int i) {
        this.imageWriter.writePixel(j, i, this.rayTrace.traceRay(this.constructRay(nX, nY, j, i)));
    }

            /* Renders the image by casting rays from the camera through each pixel of the image and writing the resulting color to the imageWriter.
     * Throws UnsupportedOperationException if any of the required resources are missing (rayTracerBase, imageWriter, width, height, distance).
            */
    public Camera renderImage() {
        if (this.rayTrace == null || this.imageWriter == null || this.viewPlaneWidth == 0 || this.viewPlaneHeight == 0 || this.viewPlaneDistance == 0)
            throw new UnsupportedOperationException("MissingResourcesException");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; i++)
            for (int j = 0; j < nX; j++)
                this.castRay(nX, nY, j, i);
        return this;
    }



    /* Draws a grid on the image by writing a specified color to the pixels that fall on the grid lines.
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

    /* "Printing" the image - producing an unoptimized png file of the image
     * @throws MissingResourceException The render's field imageWriter mustn't be null
            /
    public Camera writeToImage() throws MissingResourceException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
        this.imageWriter.writeToImage();
        return this;
    }

    /* Builder class for constructing Camera objects.
     */
    public static class Builder {
        final private Camera camera;

        /* Default constructor for Builder. Initializes with a new Camera instance.
                */
        public Builder() {
            this.camera = new Camera();
        }

        /* Constructor for Builder with an existing Camera instance.
                *
                * @param camera the Camera instance to initialize the builder with.
         */
        public Builder(Camera camera) {
            this.camera = camera;
        }

        /* Sets the location of the camera.
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
                /* Sets the direction of the camera.
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

        /* Sets the size of the view plane.
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

        /* Sets the distance from the camera to the view plane.
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
        /* setter for image writer.
                *
                * @param imageWriter image writer.
         * @return the Camera.
                */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter= imageWriter;
            return this;
        }

        /* Setter for ray tracer base.
         *
                 * @param rayTracerBase Ray tracer base.
                * @return The Camera.
                */
        public Builder setRayTracer(RayTracerBase rayTracerBase) {
            camera.rayTrace = rayTracerBase;
            return this;
        }

        /* Builds and returns the Camera instance.
                *
                * @return the constructed Camera instance.
                */
        public Camera build() {
            validateCamera();
            return (Camera) camera.clone();
        }
                /* Validates that all required fields of the camera are set.
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

        /* Checks if two vectors are perpendicular.
                *
                * @param v1 the first vector.
                * @param v2 the second vector.
                * @return true if the vectors are perpendicular, false otherwise.
         */
        private boolean isPerpendicular(Vector v1, Vector v2) {
            return v1.dotProduct(v2) == 0;
        }
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

}
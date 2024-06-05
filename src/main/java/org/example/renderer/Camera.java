package org.example.renderer;

import org.example.geometries.Sphere;
import org.example.primitives.Point;
import org.example.primitives.Ray;
import org.example.primitives.Vector;

import java.util.List;
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

    /**
     * Returns a new instance of the Builder class.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a ray from the camera through a specific pixel on the view plane.
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

    public Point getPosition() {
        return position;
    }

    public Vector getVTo() {
        return vTo;
    }

    public Vector getVUp() {
        return vUp;
    }

    public Vector getVRight() {
        return vRight;
    }

    public double getViewPlaneWidth() {
        return viewPlaneWidth;
    }

    public double getViewPlaneHeight() {
        return viewPlaneHeight;
    }

    public double getViewPlaneDistance() {
        return viewPlaneDistance;
    }


    @Override
    public Camera clone() {
        try {
            return (Camera) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning of Camera failed", e);
        }
    }

    /* public List<Point> findIntersections(int i, int i1, Sphere sphereTC01) {
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

        public Builder setVTo(Vector vTo) {
            this.camera.vTo = vTo;
            return this;
        }

        public Builder setVUp(Vector vUp) {
            this.camera.vUp = vUp;
            return this;
        }

        public Builder setVRight(Vector vRight) {
            this.camera.vRight = vRight;
            return this;
        }

        public Builder setViewPlaneWidth(double viewPlaneWidth) {
            this.camera.viewPlaneWidth = viewPlaneWidth;
            return this;
        }

        public Builder setViewPlaneHeight(double viewPlaneHeight) {
            this.camera.viewPlaneHeight = viewPlaneHeight;
            return this;
        }

        public Builder setViewPlaneDistance(double viewPlaneDistance) {
            this.camera.viewPlaneDistance = viewPlaneDistance;
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
            return v1.dotProduct(v2) == 0;
        }
    }


}

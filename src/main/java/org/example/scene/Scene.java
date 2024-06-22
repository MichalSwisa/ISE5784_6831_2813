package org.example.scene;

import org.example.geometries.Geometries;
import org.example.lighting.AmbientLight;
import org.example.lighting.LightSource;
import org.example.primitives.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * The Scene class represents a 3D scene containing geometries, background color,
 * ambient light, and the name of the scene.
 */
public class Scene {
    /**
     * The name of the scene.
     */
    public String name;
    /**
     * The background color of the scene. Defaults to black.
     */
    public Color background;
    /**
     * The ambient light of the scene. Defaults to no ambient light.
     */
    public AmbientLight ambientLight;
    /**
     * The geometries in the scene. Defaults to an empty set of geometries.
     */
    public Geometries geometries;

    /**
     * list of all light source in the scene
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a new Scene with the specified name.
     * Initializes the background to black, ambient light to none, and geometries to an empty set.
     *
     * @param name the name of the scene
     */
    public Scene(String name) {
        this.name = name;
        this.background = Color.BLACK;// Default background color is black
        this.ambientLight = AmbientLight.NONE;// Default ambient light is NONE
        this.geometries = new Geometries();// Default is an empty set of geometries
    }

    /**
     * Sets the background color of the scene.
     *
     * @param background the new background color
     * @return the current Scene object (this)
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     *
     * @param ambientLight the new ambient light
     * @return the current Scene object (this)
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     *
     * @param geometries the new set of geometries
     * @return the current Scene object (this)
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * setter for light source return this for builder
     *
     * @param lights list of source lights
     * @return this for builder
     */
    public Scene setlights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    @Override
    public String toString() {
        return "Scene [name=" + name + ", background=" + background + ", ambientLight=" + ambientLight + ", geometries="
                + geometries + ", lightSources=" + lights + "]";
    }
}

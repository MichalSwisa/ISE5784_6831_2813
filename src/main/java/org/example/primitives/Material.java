package org.example.primitives;

/**
 * The Material class represents the material properties of an object in a
 * scene. It includes the diffuse reflection coefficient (kD), the specular
 * reflection coefficient (kS), and the shininess (nShininess) of the material.
 */
public class Material {

    /**
     * The diffuse reflection coefficient.
     */
    public Double3 kD = Double3.ZERO;

    /**
     * The specular reflection coefficient.
     */
    public Double3 kS = Double3.ZERO;
    /**
     * The transparency attenuation coefficient.
     */
    public Double3 kT = Double3.ZERO;

    /**
     * The reflection attenuation coefficient.
     */
    public Double3 kR = Double3.ZERO;

    /**
     * The shininess of the material.
     */
    public int nShininess = 0;

    // ***************** setters builder pattern ********************** //

    /**
     * Sets the diffuse reflection coefficient (kD) to the specified value.
     *
     * @param kD The value of the diffuse reflection coefficient.
     * @return This Material object.
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) to the specified value.
     *
     * @param kS The value of the specular reflection coefficient.
     * @return This Material object.
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient (kT) to the specified value.
     *
     * @param kT The value of the transparency attenuation coefficient.
     * @return This Material object.
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (kR) to the specified value.
     *
     * @param kR The value of the reflection attenuation coefficient.
     * @return This Material object.
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Sets the diffuse reflection coefficient (kD) to the specified Double3 object.
     *
     * @param kD The Double3 object representing the diffuse reflection coefficient.
     * @return This Material object.
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Sets the specular reflection coefficient (kS) to the specified Double3 object.
     *
     * @param kS The Double3 object representing the specular reflection coefficient.
     * @return This Material object.
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Sets the transparency attenuation coefficient (kT) to the specified Double3 object.
     *
     * @param kT The Double3 object representing the transparency attenuation coefficient.
     * @return This Material object.
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Sets the reflection attenuation coefficient (kR) to the specified Double3 object.
     *
     * @param kR The Double3 object representing the reflection attenuation coefficient.
     * @return This Material object.
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Sets the shininess of the material.
     *
     * @param nShininess The shininess value.
     * @return This Material object.
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg.physics;


/**
 *
 * @author juanm
 */
public class PhysicalCharacteristics {

    public double mass;
    public double maxModuleAcceleration;
    public double maxModuleDeceleration;
    public double maxModuleSpeed;


    public PhysicalCharacteristics() {
        this.mass = 0;
        this.maxModuleAcceleration = 0;
        this.maxModuleDeceleration = 0;
        this.maxModuleSpeed = 0;
    }
}

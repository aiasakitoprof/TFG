package src.tg.physics;


import src.tg.helper.DoubleVector;

import java.io.Serializable;


public class PhysicalVariables implements Serializable {

    public DoubleVector acceleration; // speed per milliseconds
    public DoubleVector speed;        // pixels per milliseconds


    public PhysicalVariables() {
        this.acceleration = new DoubleVector(0, 0);
        this.speed = new DoubleVector(0, 0);
    }


    public void cloneAcceleration(DoubleVector dVector) {
        this.acceleration.set(dVector);
    }


    public void cloneSpeed(DoubleVector dVector) {
        this.speed.set(dVector);
    }

}

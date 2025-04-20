package src.tg.physics;


import java.io.Serializable;


public class PhysicalValues implements Serializable {

    public PhysicalCharacteristics phyCharacteristics;
    public PhysicalVariables phyVariables;

    public PhysicalValues() {
        this.phyCharacteristics = new PhysicalCharacteristics();
        this.phyVariables = new PhysicalVariables();
    }
}

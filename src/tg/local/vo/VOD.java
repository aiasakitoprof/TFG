package src.tg.local.vo;


import src.tg.helper.DoubleVector;
import src.tg.helper.Position;
import src.tg.physics.PhysicalModel;
import src.tg.physics.PhysicalVariables;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * VISUAL OBJECT DYNAMIC
 */
public abstract class VOD extends VO implements Runnable {

    private static int vodAmount = 0;
    private Thread thread;
    private PhysicalModel physicalModel;


    /**
     * CONSTRUCTORS
     */
    protected VOD() {
        super();

        this.init();
    }


    protected VOD(BufferedImage bfImage, double scale, DoubleVector coordinates) {
        super();
        this.init();

        this.getMainImage().setImageAndDimension(bfImage);
        this.getMainImage().setScale(scale);
        this.getPosition().set(coordinates);
    }

    /**
     * STATICS
     */
    public static int getVODAmount() {
        return VOD.vodAmount;
    }

    /**
     * PRIVATES
     */
    private void init() {
        this.physicalModel = new PhysicalModel();
        this.thread = new Thread(this);
        this.thread.setName("VOD Thread · " + VOD.vodAmount);

        VOD.vodAmount++;
    }


    /**
     * PUBLICS
     */
    public boolean activate() {
        if (!this.isLocalModelOk()) {
            System.err.println("ERROR Local model is not setted! · (VOD)");
            return false;
        }

        this.setState(VOState.ALIVE);
        this.thread.start();

        return true;
    }


    public PhysicalModel getPhysicalModel() {
        return this.physicalModel;
    }


    public final void nextMove(Position newPos, PhysicalVariables newPhyVars) {
        this.getPosition().clone(newPos);
        this.physicalModel.cloneAcceleration(newPhyVars.acceleration);
        this.physicalModel.cloneSpeed(newPhyVars.speed);

        this.getLocalModel().collisionDetection(this);
    }


    /**
     * OVERRIDES
     */
    @Override
    public void die() {
        super.die();
    }


    @Override
    public void run() {
        PhysicalVariables newPhyVar = new PhysicalVariables();
        Position newPos = new Position();

        while (this.getState() != VOState.DEAD) {

            if (this.getState() == VOState.ALIVE) {
                newPos.clone(this.getPosition());
                this.physicalModel.calcNewLocationGravity(newPos, newPhyVar);
                this.nextMove(newPos, newPhyVar);
            }

            try {
                /* Descansar */
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in the VOD thread! (VOD) · " + ex.getMessage());
            }
        }
    }


    @Override
    synchronized public void paint(Graphics gr) {
        super.paint(gr);

        this.physicalModel.paint(gr, this.getPosition());
    }

}

package src.tg.fx;


import src.tg.local.vo.VO;
import src.tg.local.vo.VOImage;
import src.tg.local.vo.VOState;


/**
 * @author juanm
 */
public class Spin extends Animation {

    private double degreesInc;


    public Spin(double degreesInc, long delayMillis) {
        super();

        this.init(degreesInc, delayMillis);
    }


    /**
     * PRIVATES
     */
    private void init(double degreesInc, long delayMillis) {
        this.setAnimationType(AnimationType.SPIN_AND_SCALE);
        this.setDelayMillis(delayMillis);
        this.degreesInc = degreesInc;
        this.setName("Spin and Scale FX Thread");
    }


    @Override
    public void run() {
        VO visualObject = this.getVisualObject();
        VOImage voImage = this.getVOImage();

        if (visualObject == null || voImage == null) {
            return; // =======================================================>
        }

        while (visualObject.getState() != VOState.DEAD) {

            if (visualObject.getState() == VOState.ALIVE) {
                voImage.rotate(this.degreesInc);
            }

            try {
                /* Descansar */
                Thread.sleep(this.getDelayMillis());
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping in the ball thread! (Spinning) · " + ex.getMessage());
            }
        }
    }
}

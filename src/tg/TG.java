package src.tg;


import src.tg.fx.Spin;
import src.tg.fx.SpinOverprint;
import src.tg.fx.SpinOverprintType;
import src.tg.generator.AsteroidGenerator;
import src.tg.helper.DoubleVector;
import src.tg.images.Images;
import src.tg.local.vo.VO;
import src.tg.local.vo.dynamics.Planet;
import src.tg.local.vo.dynamics.Ship;
import src.tg.peer.PCT;

import java.util.Random;


/**
 * The game main class
 *
 * @author juanm
 */
public class TG {

    private static TGState state;

    private PCT peerController;
    private AsteroidGenerator vodGenerator;


    /**
     * CONSTRUCTORS
     */
    public TG() {
        Images.loadAllImages();

        this.state = TGState.STARTING;
        this.peerController = new PCT();
        this.peerController.activate();

        this.vodGenerator = new AsteroidGenerator(this);
        this.vodGenerator.activate();
        this.addPlanets();

        Ship ship = new Ship(new DoubleVector(100, 600));
        this.peerController.addVisualObject(ship);
        setupRespawnKey();
    }

    /**
     * STATICS
     */
    public static TGState getState() {
        return TG.state;
    }

    public static long getVOCreated() {
        return PCT.getVOCreated();
    }

    public static long getVODeads() {
        return PCT.getVODeads();
    }

    public static void main(String[] args) {
        // TODO code application logic here
        new TG();
    }

    /**
     * PRIVATES
     */
    private void addPlanets() {
        Random rnd = new Random();
        Planet planet;

        // Planeta 1
        planet = new Planet(1, 0.25d, new DoubleVector(200, 200), 10e10d);
        planet.setAnimation(new Spin(0.8d, 1));
        //planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_1, 1.05d, 0.2d, 30));
        //planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_1, 1d, -0.15d, 30));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_7, 0.95d, -0.25d, 5));
        this.peerController.addVisualObject(planet);

        // Planeta 2
        planet = new Planet(2, 0.3d, new DoubleVector(325, 225), 10e10d);
        planet.setAnimation(new Spin(0.15d, 30));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_7, 0.25d, -0.9d, 30, new DoubleVector(70, 0)));
        //planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_10, 0.4d, -0.3d, 30));
        this.peerController.addVisualObject(planet);

        //Planeta 4
        //planet = new Planet(3, 0.25d, new DoubleVector(700, 225));
        //planet.setAnimation(new Spin(-0.2d, 30));
        //planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_3, 0.7d, 1d, 30));
        //this.peerController.addVisualObject(planet);
        // Planeta 5
        //planet = new Planet(4, 0.25d, new DoubleVector(1000, 225));
        //planet.setAnimation(new Spin(0.1d, 30));
        //planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_4, 0.7d, 0.5d, 30));
        //this.peerController.addVisualObject(planet);
        // Planeta 6
        planet = new Planet(5, 0.25d, new DoubleVector(700, 400), 10e10d);
        planet.setAnimation(new Spin(2d, 30));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_5, 0.3d, -2.2d, 30));
        this.peerController.addVisualObject(planet);
        // Planeta 7
        planet = new Planet(6, 0.5d, new DoubleVector(1000, 400), 10e10d);
        planet.setAnimation(new Spin(-0.2d, 30));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_6, 0.65d, 0.2d, 30));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.HALO_9, 0.9d, 0.25d, 30, new DoubleVector(-100, -10)));
        planet.setAnimation(new SpinOverprint(SpinOverprintType.BUBBLE_1, 0.2d, 0.8d, 30, new DoubleVector(100, 0)));
        this.peerController.addVisualObject(planet);
    }

    /**
     * PUBLICS
     */
    public void addVisualObject(VO vo) {
        this.peerController.addVisualObject(vo);
    }

    private void setupRespawnKey() {
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED && e.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                boolean aliveShipExists = false;
                for (VO vo : this.peerController.getVisualObjects()) {
                    if (vo instanceof Ship s && s.getState() == src.tg.local.vo.VOState.ALIVE) {
                        aliveShipExists = true;
                        break;
                    }
                }
                if (!aliveShipExists) {
                    Ship newShip = new Ship(new DoubleVector(100, 600));
                    this.peerController.addVisualObject(newShip);
                }
            }
            return false;
        });
    }

}
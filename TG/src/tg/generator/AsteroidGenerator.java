package tg.generator;


import java.util.Random;
import tg.TG;
import tg.fx.Spin;
import tg.helper.DoubleVector;
import tg.local.vo.dynamics.Asteroid;
import tg.local.vo.VO;


/**
 *
 * VISUAL DYNAMIC OBJECTS GENERATOR
 *
 */
public class AsteroidGenerator implements Runnable {

    private Thread VODThread;
    private TG theGame;
    private int maxAsteroids;
    private int maxMillis;
    private int minMillis;

    private DoubleVector maxAcceleration;
    private DoubleVector minAcceleration;

    private double maxScale;
    private double minScale;


    /**
     * CONSTRUCTORS
     *
     * @param theGame
     */
    public AsteroidGenerator(TG theGame) {
        this.theGame = theGame;
        this.maxAsteroids = 20;

        this.minMillis = 10;
        this.maxMillis = 40;

        this.minScale = 0.1;
        this.maxScale = 0.5;

        this.minAcceleration = new DoubleVector(-0.0001, -0.0001);
        this.maxAcceleration = new DoubleVector(0.0001, 0.0001);

        this.VODThread = new Thread(this);
        this.VODThread.setName("Generator Thread");
    }


    /**
     * PUBLICS
     */
    public void activate() {
        this.VODThread.start();
    }


    public Asteroid newAsteroid() {
        Random rnd = new Random();

        Asteroid asteroid = new Asteroid(1 + rnd.nextInt(5), this.randomScale(), this.randomCoordinates());
        asteroid.getPhysicalModel().cloneAcceleration(this.randomAcceleration());
        //asteroid.getPhysicalModel().cloneSpeed(new DoubleVector(0.001d,0.001d));
        asteroid.setAnimation(new Spin(5f * (float) rnd.nextGaussian(), 20));

        return asteroid;
    }


    public DoubleVector randomAcceleration() {
        Random rnd = new Random();
        DoubleVector newAcceleration = new DoubleVector();

        newAcceleration.setX(this.minAcceleration.getX() + rnd.nextGaussian() * (this.maxAcceleration.getX() - this.minAcceleration.getX()));
        newAcceleration.setY(this.minAcceleration.getY() + rnd.nextGaussian() * (this.maxAcceleration.getY() - this.minAcceleration.getY()));

        return newAcceleration;
    }


    public DoubleVector randomCoordinates() {
        float x, y;

        Random rnd = new Random();
        x = rnd.nextFloat() * 1300f;
        y = rnd.nextFloat() * 800f;

        DoubleVector coordinates = new DoubleVector(x, y);

        return coordinates;
    }


    public double randomScale() {
        Random rnd = new Random();

        return this.minScale + rnd.nextFloat() * (this.maxScale - this.minScale);
    }


    public void setAccelerationRange(float minX, float minY, float maxX, float maxY) {
        this.minAcceleration.setXY(minX, minY);
        this.maxAcceleration.setXY(maxX, maxY);
    }


    public void setMaxNumberOfVO(int maxNumberOfVO) {
        this.maxAsteroids = maxNumberOfVO;
    }


    public void setMillis(int minMillis, int maxMillis) {
        this.minMillis = minMillis;
        this.maxMillis = maxMillis;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        Random rnd = new Random();
        long vodQuantity = 0;

        // Show frames
        while (true) { // TO-DO End condition

            if (true) { // TO-DO Pause condition

                vodQuantity = TG.getVOCreated() - TG.getVODeads();

                if (vodQuantity < this.maxAsteroids) {
                    this.theGame.addVisualObject((VO) this.newAsteroid());
                } else {
                    System.out.println("Max number of live asteroids reached! CREATED: " + TG.getVOCreated() + " · DEADS:" + TG.getVODeads());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            try {
                Thread.sleep(rnd.nextInt(this.maxMillis - this.minMillis) + this.minMillis);
            } catch (InterruptedException ex) {
            }
        }
    }

}

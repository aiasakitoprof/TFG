package src.tg.local.vo.dynamics;


import src.tg.fx.SpriteAnimation;
import src.tg.fx.SpriteType;
import src.tg.helper.DoubleVector;
import src.tg.images.Images;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;

import java.io.Serializable;
import java.util.Random;


public class Asteroid extends VOD implements Serializable {

    private SpriteType explosionType;


    /**
     * CONSTRUCTORS
     */
    public Asteroid(int asteroidNumber, double scale, DoubleVector coordinates) {
        super(Images.getAsteroid(asteroidNumber), scale, coordinates);

        this.explosionType = this.calculateExplosionType(asteroidNumber);

        double minSpeed = 0.1;
        double maxSpeed = 0.2;

        Random rand = new Random();
        double angle = rand.nextDouble() * 2 * Math.PI; // 0 ... 2π radians
        double speed = minSpeed + (maxSpeed - minSpeed) * rand.nextDouble();

        double vx = speed * Math.cos(angle);
        double vy = speed * Math.sin(angle);

        this.getPhysicalModel().phyVariables.speed.setXY(vx, vy);
    }


    public void die() {
        this.setState(VOState.COLLIDED);

        SpriteAnimation explosion = new SpriteAnimation(this.explosionType);
        this.setAnimation(explosion);
        try {
            explosion.join();
        } catch (InterruptedException ex) {
        }

        this.setState(VOState.DEAD);
        this.getLocalModel().removeVO(this);
    }


    private SpriteType calculateExplosionType(int asteroidNumber) {
        return SpriteType.values()[asteroidNumber];
    }

}

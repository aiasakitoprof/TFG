package src.tg.local.vo.dynamics;


import src.tg.fx.SpriteAnimation;
import src.tg.fx.SpriteType;
import src.tg.helper.DoubleVector;
import src.tg.images.Images;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;

import java.io.Serializable;


public class Asteroid extends VOD implements Serializable {

    private SpriteType explosionType;


    /**
     * CONSTRUCTORS
     */
    public Asteroid(int asteroidNumber, double scale, DoubleVector coordinates) {
        super(Images.getAsteroid(asteroidNumber), scale, coordinates);

        this.explosionType = this.calculateExplosionType(asteroidNumber);
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

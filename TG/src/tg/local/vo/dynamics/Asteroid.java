package tg.local.vo.dynamics;


import tg.local.vo.VOD;
import java.io.Serializable;
import tg.fx.SpriteType;
import tg.fx.SpriteAnimation;
import tg.helper.DoubleVector;
import tg.images.Images;
import tg.local.vo.VOState;


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

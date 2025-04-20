package tg.local.vo.dynamics;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import tg.local.vo.VOD;
import java.io.Serializable;
import java.util.Random;
import tg.fx.Overprint;
import tg.fx.OverprintType;
import tg.helper.DoubleVector;
import tg.images.Images;
import tg.local.vo.VO;
import tg.local.vo.VOState;


public class Planet extends VO implements Serializable {

    public static boolean debugMode = true;
    long impacts;
    long cracks;


    /**
     * CONSTRUCTORS
     *
     * @param imageNumber
     * @param scale
     * @param coordinates
     */
    public Planet(int imageNumber, double scale, DoubleVector coordinates) {
        super(Images.getPlanet(imageNumber), scale, coordinates);
        this.impacts = 0;
    }


    /**
     * PUBLICS
     */
    /**
     * OVERRIDES
     */
    @Override
    public void die() {
        this.setState(VOState.ALIVE);
    }

    // Final de obras ... Juanito 607.54.49.31 

    public void impact(VOD vod) {
        this.setState(VOState.ALIVE);
        this.impacts++;

        if (impacts <= 40) {
            Overprint hole
                    = new Overprint(
                            OverprintType.SHOT_HOLE_2, 0.03d,
                            vod.getPosition().calculateOffset(this.getPosition()));

            hole.getVOImage().getOffset().rotate(-this.getMainImage().getAngle());
            this.setAnimation(hole);
        }

        if (this.impacts % 40 == 0 && this.impacts <= 150) {
            //.updateScale(1.3d);
            this.showCrack();
        }
    }


    private void showCrack() {
        double scale, imageRadius, crackRadius;
        int imageIndex = new Random().nextInt(4);
        //int imageIndex = 2;

        this.cracks++;
        
        imageRadius = this.getMainImage().getScaledImageDimension().getModule();
        crackRadius = Overprint.getVOImage(imageIndex).getImageDimension().getModule();
        scale = imageRadius / crackRadius * 0.75d;

        Overprint crack = new Overprint(
                OverprintType.values()[imageIndex], scale);

        crack.getVOImage().setAngle(new Random().nextInt(360));
        this.setAnimation(crack);
    }


    public void showDebugInfo(Graphics g2d, int x, int y) {
        if (!Planet.debugMode) {
            return;
        }

        x -= 35;
        y += 18;
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(x - 4, y - 12, 80, 16, 15, 15);
        g2d.setColor(Color.RED);
        g2d.drawString("Impacts " + Long.toString(this.impacts), x, y);
    }


    @Override
    public void paint(Graphics gr) {
        super.paint(gr);

        this.showDebugInfo(
                gr,
                (int) this.getPosition().getX(),
                (int) this.getPosition().getY());
    }
}

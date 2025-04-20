package src.tg.local.vo.dynamics;


import src.tg.fx.Overprint;
import src.tg.fx.OverprintType;
import src.tg.helper.DoubleVector;
import src.tg.images.Images;
import src.tg.local.vo.VO;
import src.tg.local.vo.VOD;
import src.tg.local.vo.VOState;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


public class Planet extends VO implements Serializable {

    public static boolean debugMode = true;
    long impacts;
    long cracks;
    private double mass;
    private static final List<Planet> allPlanets = new CopyOnWriteArrayList<>();

    /**
     * CONSTRUCTORS
     *
     * @param imageNumber
     * @param scale
     * @param coordinates
     */
    public Planet(int imageNumber, double scale, DoubleVector coordinates, double mass) {
        super(Images.getPlanet(imageNumber), scale, coordinates);
        this.impacts = 0;
        this.mass = mass;
        allPlanets.add(this);
    }

    public double getMass() {
        return this.mass;
    }

    public static List<Planet> getAllPlanets() {
        return allPlanets;
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

package tg.local.vo;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import tg.fx.SpriteType;
import tg.fx.Animation;
import tg.fx.SpriteAnimation;
import tg.helper.DoubleVector;
import tg.helper.Position;
import tg.local.LM;


/**
 * VISUAL OBJECT
 */
public abstract class VO {

    public static boolean debugMode = false;

    private LM localModel;
    private Position position;
    private VOState state;
    private VOImage mainImage;
    private ArrayList<VOImage> overPrintImage;
    private long id;


    /**
     * CONSTRUCTORS
     */
    public VO() {
        this.init();
    }


    public VO(BufferedImage bfImage, double scale, DoubleVector coordinates) {
        this.init();

        this.mainImage.setImageAndDimension(bfImage);
        this.mainImage.setScale(scale);
        this.position.set(coordinates);
        this.setState(VOState.ALIVE);
    }


    /**
     * PRIVATES
     */
    private AffineTransform calcRotateOverprint(
            AffineTransform mainRotation, VOImage overprintImg,
            Double overPrintX, Double overPrintY) {

        // Calcula la rotación de overprint según su propio ángulo
        AffineTransform rotateOverprintTransform
                = AffineTransform.getRotateInstance(
                        Math.toRadians(overprintImg.getAngle()),
                        overPrintX, overPrintY);

        // Concatena la rotación principal a la del overprint
        AffineTransform relativeRotation = (AffineTransform) mainRotation.clone();
        relativeRotation.concatenate(rotateOverprintTransform);
        return relativeRotation;

    }
    
    private AffineTransform calcRotateSpinOverprint(
            AffineTransform mainRotation, VOImage overprintImg,
            Double overPrintX, Double overPrintY) {

        // Calcula la rotación de overprint según su propio ángulo
        AffineTransform rotateOverprintTransform
                = AffineTransform.getRotateInstance(
                        Math.toRadians(overprintImg.getAngle()),
                        overPrintX, overPrintY);

        // Caso de overprint sin desplazamiento respecto a la imagen principal 
        if (overprintImg.getOffset().getModule() == 0) {
            // Solo se aplica la rotación especifica del overprint

            // Útil para "halos" concentricos a la imagen principal
            // que giran independientemente del giro de la imagen principal
            return rotateOverprintTransform;
        }

        // Caso de desplazamiento respecto a imagen principal (offset!=0)
        // Se aplica primero la rotación de la imagen principal y despues la
        // rotación propia del overprint
        // Util para elementos satélite que orbitan alrededor de la imagen
        // principal y ademas autrotan sobre si mismos
        AffineTransform relativeRotation = (AffineTransform) mainRotation.clone();
        relativeRotation.concatenate(rotateOverprintTransform);
        return relativeRotation;

    }


    private void init() {
        this.overPrintImage = new ArrayList<>();
        this.localModel = null;
        this.position = new Position();
        this.state = VOState.STARTING;
        this.id = 0;

        this.mainImage = new VOImage();
    }


    private void showDebugInfo(Graphics2D g2d, int x, int y) {
        if (!VO.debugMode) {
            return;
        }

        x -= 10;
        y -=10; 
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(x - 4, y - 12, 30, 16, 15, 15);

        g2d.setColor(Color.GREEN);
        if (this.state != VOState.ALIVE) {
            g2d.setColor(Color.MAGENTA);
        }

        g2d.drawString(Long.toString(this.id), x, y);
    }


    private boolean showMainImage(
            Graphics2D g2d, AffineTransform mainRotation,
            Double x, Double y) {
        int width, height;

        BufferedImage image = this.mainImage.getImage();
        if (image == null) {
            return false; // =======================================================>
        }

        g2d.setTransform(mainRotation);

        // Mostrar imagen principal escalada
        width = (int) this.mainImage.getScaledImageDimension().getX();
        height = (int) this.mainImage.getScaledImageDimension().getY();
        g2d.drawImage(
                this.mainImage.getImage(),
                (int) (x - width / 2d), (int) (y - height / 2d),
                (int) width, (int) height,
                null);

        return true;
    }


    private void showOverprints(
            Graphics2D g2d, AffineTransform mainRotation,
            Double mainX, Double mainY) {
        Double width, height, overPrintX, overPrintY;

        for (VOImage overprint : this.overPrintImage) {
            overPrintX = mainX + overprint.getOffset().getX();
            overPrintY = mainY + overprint.getOffset().getY();

            if (overprint.getAnimation().getAnimationType().name().equals("SPIN_OVERPRINT")) {
                g2d.setTransform(this.calcRotateSpinOverprint(
                        mainRotation, overprint, overPrintX, overPrintY));
            } else {
                g2d.setTransform(this.calcRotateOverprint(mainRotation, overprint, overPrintX, overPrintY));
                //g2d.setTransform(mainRotation);
            }

            width = overprint.getScaledImageDimension().getX();
            height = overprint.getScaledImageDimension().getY();

            g2d.drawImage(
                    overprint.getImage(),
                    (int) (overPrintX - width / 2d), (int) (overPrintY - height / 2d),
                    (int) (width * 1d), (int) (height * 1d),
                    null);
        }
    }


    /**
     * PUBLICS
     */
    public void setAnimation(Animation animation) {
        animation.setVisualObject(this);

        if (!animation.isVOImageSetted()) {
            animation.setVOImage(this.mainImage);
            this.mainImage.setAnimation(animation);
        }

        animation.start();
    }


    synchronized public VOImage addOverPrintImage(BufferedImage image) {
        VOImage voImage = new VOImage();
        this.overPrintImage.add(voImage);
        voImage.setImageAndDimension(image);

        return voImage;
    }


    synchronized public VOImage addOverPrintImage(VOImage voImage) {
        this.overPrintImage.add(voImage);
        return voImage;
    }


    public void die() {
        this.setState(VOState.COLLIDED);

        SpriteAnimation explosion = new SpriteAnimation(SpriteType.EXPLOSION_2);
        this.setAnimation(explosion);
        try {
            explosion.join();
        } catch (InterruptedException ex) {
        }

        this.setState(VOState.DEAD);
        this.localModel.removeVO(this);
    }


    public void remove() {
        this.setState(VOState.DEAD);
        this.localModel.removeVO(this);
    }


    public Rectangle2D.Double getBoundingBox() {
        Position pos = this.getPosition();

        return this.mainImage.getBoundingBox(pos.getX(), pos.getY());
    }


    public Ellipse2D.Double getBoundingEllipse() {
        Position pos = this.getPosition();

        return this.mainImage.getBoundingEllipse(pos.getX(), pos.getY());
    }


    public LM getLocalModel() {
        return this.localModel;
    }


    public Position getPosition() {
        return this.position;
    }


    public final VOState getState() {
        return this.state;
    }


    public final VOImage getMainImage() {
        return this.mainImage;
    }


    public boolean isLocalModelOk() {
        return this.localModel != null;
    }


    public void setId(long id) {
        this.id = id;
    }


    public void setLocalModel(LM localModel) {
        // A mejorar el acoplamient con LM mediante interface
        this.localModel = localModel;
    }


    public void setState(VOState state) {
        this.state = state;
    }


    public void updateScale(double factor) {
        this.mainImage.updateScale(factor);
    }


    synchronized public void paint(Graphics gr) {
        double width, height, x, y;

        x = this.getPosition().getX();
        y = this.getPosition().getY();

        Graphics2D g2d = (Graphics2D) gr.create();
        AffineTransform defaultTransform = g2d.getTransform();

        AffineTransform mainRotation
                = AffineTransform.getRotateInstance(
                        Math.toRadians(this.mainImage.getAngle()),
                        this.getPosition().getX(), this.getPosition().getY());

        if (this.showMainImage(g2d, mainRotation, x, y)) {
            this.showOverprints(g2d, mainRotation, x, y);
        }

        g2d.setTransform(mainRotation);

        if (VO.debugMode) {
            g2d.setColor(this.mainImage.getBoundingBoxColor());
            g2d.draw(this.mainImage.getBoundingBox(x, y));
            g2d.draw(this.mainImage.getBoundingEllipse(x, y));
        }

        g2d.setTransform(defaultTransform);
        this.showDebugInfo(g2d, (int) x, (int) y);
        g2d.dispose();
    }

}

package tg.local.vo;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import tg.fx.Animation;
import tg.helper.DoubleVector;


/**
 *
 * @author juanm
 */
public class VOImage {

    private BufferedImage image;
    private BufferedImage imageScaled;
    private Animation animation;

    private DoubleVector imageDimension;
    private DoubleVector scaledImageDimension;

    private DoubleVector offset;
    private double angle;
    private double scale;

    private Rectangle2D.Double boundingBox;
    private Ellipse2D.Double boundingEllipse;
    private Color boundingBoxColor;


    /**
     * CONSTRUCTOR
     */
    public VOImage() {
        this.init();
    }


    public VOImage(BufferedImage bfImage) {
        this.init();

        this.setImageAndDimension(bfImage);
    }


    /**
     * PRIVATES
     */
    private void init() {
        this.image = null;
        this.imageScaled = null;
        this.animation = null;
        this.imageDimension = new DoubleVector(0, 0);
        this.offset = new DoubleVector(0d, 0d);
        this.angle = 0;

        this.scaledImageDimension = new DoubleVector(0, 0);
        this.boundingBoxColor = Color.GRAY;
        this.boundingEllipse = new Ellipse2D.Double();
        this.boundingBox = new Rectangle2D.Double(0, 0, 0, 0);
        this.scale = 1;
    }


    private void updateImageScaled() {
        this.scaledImageDimension.setX(this.imageDimension.getX() * this.getScale());
        this.scaledImageDimension.setY(this.imageDimension.getY() * this.getScale());

        if (this.scaledImageDimension.getX() == 0 || this.scaledImageDimension.getY() == 0) {
            return;
        }

        int type = this.image.getType();
        if (type == 0) {
            type = BufferedImage.TYPE_INT_ARGB;
        }

        this.imageScaled
                = new BufferedImage(
                        (int) this.scaledImageDimension.getX(),
                        (int) this.scaledImageDimension.getY(),
                        type);

        Graphics2D g = this.imageScaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(this.image, 0, 0,
                    (int) this.scaledImageDimension.getX(),
                    (int) this.scaledImageDimension.getY(),
                    null);

        g.dispose();
    }


    /**
     * PUBLICS
     */
    public void rotate(double rotation) {
        this.angle += rotation;
    }


    public VOImage clone() {
        VOImage clonedVOImage = new VOImage();

        clonedVOImage.image = this.image;
        clonedVOImage.imageDimension.set(this.imageDimension);
        clonedVOImage.offset.set(this.offset);
        clonedVOImage.angle = this.angle;
        clonedVOImage.scale = this.scale;
        clonedVOImage.scaledImageDimension.set(this.scaledImageDimension);
        clonedVOImage.boundingBox = (Rectangle2D.Double) this.boundingBox.clone();
        clonedVOImage.boundingEllipse = (Ellipse2D.Double) this.boundingEllipse.clone();

        clonedVOImage.boundingBoxColor = this.boundingBoxColor;

        return clonedVOImage;
    }


    public Animation getAnimation() {
        return this.animation;
    }


    public Rectangle2D.Double getBoundingBox(double x, double y) {

        this.boundingBox.x = x - this.scaledImageDimension.getX() / 2;
        this.boundingBox.y = y - this.scaledImageDimension.getY() / 2;
        this.boundingBox.width = this.scaledImageDimension.getX();
        this.boundingBox.height = this.scaledImageDimension.getY();

        return this.boundingBox;
    }


    public Color getBoundingBoxColor() {
        return this.boundingBoxColor;
    }


    public Ellipse2D.Double getBoundingEllipse(double x, double y) {
        this.boundingEllipse.x = x - this.scaledImageDimension.getX() * 0.9d / 2;
        this.boundingEllipse.y = y - this.scaledImageDimension.getY() * 0.9d / 2;
        this.boundingEllipse.width = this.scaledImageDimension.getX() * 0.9d;
        this.boundingEllipse.height = this.scaledImageDimension.getY() * 0.9d;

        return this.boundingEllipse;
    }


    public BufferedImage getImage() {
        // return this.image;
        return this.imageScaled;
    }


    public DoubleVector getImageDimension() {
        return this.imageDimension;
    }


    public DoubleVector getOffset() {
        return this.offset;
    }


    public double getAngle() {
        return this.angle;
    }


    public DoubleVector getScaledImageDimension() {
        return this.scaledImageDimension;
    }


    public double getScale() {
        return this.scale;
    }


    public void setAnimation(Animation animation) {
        this.animation = animation;
    }


    public void setBoundingBoxColor(Color color) {
        this.boundingBoxColor = color;
    }


    public void setImage(BufferedImage image) {
        this.image = image;
        this.updateImageScaled();
    }


    public void setImageAndDimension(BufferedImage image) {
        this.imageDimension = new DoubleVector(image.getWidth(), image.getHeight());
        this.image = image;
        this.updateImageScaled();
    }


    public void setOffset(DoubleVector offset) {
        this.offset.set(offset);
    }


    public void setAngle(double rotation) {
        this.angle = rotation;
    }


    public void setScale(double scale) {
        this.scale = scale;
        this.updateImageScaled();
    }


    public void updateScale(double factor) {
        this.scale *= factor;

        this.updateImageScaled();
    }

}

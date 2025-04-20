package tg.fx;


import java.awt.image.BufferedImage;


/**
 *
 * @author juanm
 */
public class SpriteAnimationConstraints {

    public SpriteType id;
    public int cols;
    public long delayMillis;
    public int frames;
    public int rows;
    public float scaleup;
    public BufferedImage[] sprites;
    public BufferedImage spriteSheet;
    public String spriteSheetName;
    public AnimationType type;
}

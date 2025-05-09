package src.tg.fx;


import src.tg.local.vo.VO;
import src.tg.local.vo.VOImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author juanm
 */
public abstract class Animation extends Thread {

    private long delayMills;
    private VO visualObject;
    private VOImage voImage;
    private AnimationType animationType;


    /**
     * CONSTRUCTORS
     */
    public Animation() {
        this.delayMills = 0;
    }


    public Animation(VOImage voImage) {
        this.voImage = voImage.clone();
        this.delayMills = 0;
        this.setPriority(MAX_PRIORITY);
    }


    /**
     * STATICS
     */
    static public BufferedImage loadImage(String fileName) {
        BufferedImage img;

        img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.err.println("ERROR: Loading image · <Animation> · [" + fileName + "] · " + e.getMessage());
            img = null;
        }

        return img;
    }


    /**
     * PUBLICS
     */
    public AnimationType getAnimationType() {
        return this.animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public long getDelayMillis() {
        return this.delayMills;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMills = delayMillis;
    }

    public VO getVisualObject() {
        return this.visualObject;
    }

    public void setVisualObject(VO visualObject) {
        this.visualObject = visualObject;
    }

    public VOImage getVOImage() {
        return this.voImage;
    }

    public void setVOImage(VOImage voImage) {
        this.voImage = voImage;
        this.voImage.setAnimation(this);
    }

    public boolean isVOImageSetted() {
        return this.voImage != null;
    }

    public void setVOImageClone(VOImage voImage) {
        this.voImage = voImage.clone();
    }
}

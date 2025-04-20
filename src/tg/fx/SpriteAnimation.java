package src.tg.fx;


import src.tg.local.vo.VO;

import java.awt.image.BufferedImage;


/**
 * Sprite-based special visual effects.
 *
 * @author juanm
 */
public class SpriteAnimation extends Animation {

    private static SpriteAnimationConstraints[] animations;
    private static String assetsPath = "src/tg/fx/assets/SpriteSheets/";


    static {
        SpriteAnimation.staticInit();
    }

    private SpriteAnimationConstraints animation;
    private long delayMillis;
    private double scaleup;


    /**
     * CONSTRUCTOR
     *
     * @param id
     * @param spriteSheet
     * @param rows
     * @param cols
     * @param delayMillis
     * @param scalup
     */
    public SpriteAnimation(SpriteType id) {
        super();

        this.animation = SpriteAnimation.animations[id.ordinal()];
        this.delayMillis = this.animation.delayMillis;
        this.scaleup = this.animation.scaleup;

        this.setName("Sprite Animation · Show animation based in a spritesheet");
    }


    /**
     * STATICS
     */
    public static void loadAnimations() {
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_1, "explosion-1.png", 8, 4, 30, 1.05f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_2, "explosion-2.png", 4, 8, 20, 1.06f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_3, "explosion-3.png", 5, 7, 35, 1.02f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_4, "explosion-4.png", 7, 7, 15, 1.05f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_5, "explosion-5.png", 6, 8, 35, 1.02f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_6, "explosion-6.png", 4, 5, 55, 1.1f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_7, "explosion-7.png", 6, 8, 35, 1.03f, AnimationType.SPRITE_ONE_TIME);
        SpriteAnimation.addAnimation(SpriteType.EXPLOSION_8, "explosion-8.png", 4, 4, 10, 1.05f, AnimationType.SPRITE_ONE_TIME);
    }


    /**
     * Add a new animation and all its parameters to the list
     *
     * @param id
     * @param spriteSheetName
     * @param rows
     * @param cols
     * @param delayMillis
     * @param scalup
     * @param type
     * @return
     */
    public static boolean addAnimation(SpriteType id, String spriteSheetName, int rows, int cols, long delayMillis, float scalup, AnimationType type) {

        SpriteAnimationConstraints animation = new SpriteAnimationConstraints();
        animation.spriteSheet = Animation.loadImage(SpriteAnimation.assetsPath + spriteSheetName);

        if (animation.spriteSheet == null) {
            return false;
        }

        animation.cols = cols;
        animation.delayMillis = delayMillis;
        animation.frames = cols * rows;
        animation.rows = rows;
        animation.scaleup = scalup;
        animation.spriteSheetName = spriteSheetName;
        animation.type = type;

        animation.sprites = SpriteAnimation.extractSprites(animation.spriteSheet, animation.rows, animation.cols);

        SpriteAnimation.animations[id.ordinal()] = animation;
        return true;
    }


    public static BufferedImage[] extractSprites(BufferedImage spriteSheet, int rows, int cols) {
        int width = spriteSheet.getWidth() / cols;
        int height = spriteSheet.getHeight() / rows;

        BufferedImage[] sprites = new BufferedImage[rows * cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                sprites[(r * cols) + c] = spriteSheet.getSubimage(c * width + 3, r * height + 3, width - 6, height - 6);
            }
        }

        return sprites;
    }


    private static void staticInit() {
        // Allow to initialize static variables
        SpriteAnimation.animations = new SpriteAnimationConstraints[SpriteType.values().length];
        SpriteAnimation.loadAnimations();
    }


    /**
     * OVERRIDES
     */
    @Override
    public void run() {
        VO visualObject = this.getVisualObject();

        if (visualObject == null) {
            return; // ========================================================>
        }

        double voScale = visualObject.getMainImage().getScale();

        for (int frame = 0; frame < (animation.frames); frame++) {
            visualObject.getMainImage().setImage(animation.sprites[frame]);
            visualObject.updateScale(scaleup);

            try {
                /* Descansar */
                Thread.sleep(this.delayMillis);
            } catch (InterruptedException ex) {
                System.err.println("ERROR Sleeping! (Sprite Animation) · " + ex.getMessage());
            }
        }

        visualObject.getMainImage().setScale(voScale);
    }

}

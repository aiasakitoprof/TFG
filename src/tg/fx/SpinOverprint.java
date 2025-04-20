package src.tg.fx;


import src.tg.helper.DoubleVector;
import src.tg.local.vo.VO;
import src.tg.local.vo.VOImage;


/**
 * @author juanm
 */
public class SpinOverprint extends Spin {

    private static VOImage[] voImages = new VOImage[SpinOverprintType.values().length];
    private static String assetsPath = "src/tg/fx/assets/SpinOverprints/";
    private static VOImage voImage;

    static {
        SpinOverprint.staticInit();
    }


    /**
     * CONSTRUCTOR
     *
     * @param bfImage
     * @param scale
     * @param degreesInc
     * @param delayMillis
     */
    public SpinOverprint(SpinOverprintType id, double scale, double degreesInc, long delayMillis) {
        super(degreesInc, delayMillis);
        this.setAnimationType(AnimationType.SPIN_OVERPRINT);
        this.setName("Spin Overprint FX Thread");

        this.setVOImage(SpinOverprint.voImages[id.ordinal()].clone());
        this.getVOImage().setScale(scale);
    }


    public SpinOverprint(SpinOverprintType id, double scale, double degreesInc, long delayMillis, DoubleVector offset) {
        super(degreesInc, delayMillis);
        this.setAnimationType(AnimationType.SPIN_OVERPRINT);
        this.setName("Spin Overprint FX Thread");

        this.setVOImage(SpinOverprint.voImages[id.ordinal()].clone());
        this.getVOImage().setScale(scale);
        this.getVOImage().setOffset(offset);
    }


    /**
     * STATICS
     */
    private static void staticInit() {
        // Allow to initialize static variables
        SpinOverprint.loadAnimations();
    }


    public static void loadAnimations() {
        SpinOverprint.addAnimation(SpinOverprintType.HALO_1, "HALO-1.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_2, "HALO-2.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_3, "HALO-3.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_4, "HALO-4.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_5, "HALO-5.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_6, "HALO-6.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_7, "HALO-7.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_8, "HALO-8.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_9, "HALO-9.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.HALO_10, "HALO-10.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.ASTEROID_3_MINI, "ASTEROID-3-MINI.PNG", AnimationType.SPIN_OVERPRINT);
        SpinOverprint.addAnimation(SpinOverprintType.BUBBLE_1, "BUBBLE-1.PNG", AnimationType.SPIN_OVERPRINT);
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
    public static boolean addAnimation(SpinOverprintType id, String overprintName, AnimationType type) {

        VOImage voImage = new VOImage(Animation.loadImage(SpinOverprint.assetsPath + overprintName));

        if (voImage.getImage() == null) {
            System.err.println("Spin overprint image not loaded " + overprintName);
            return false;  // ================================================>
        }

        SpinOverprint.voImages[id.ordinal()] = voImage;
        return true;
    }


    /**
     * OVERRIDES
     */
    @Override
    public void setVisualObject(VO visualObject) {
        super.setVisualObject(visualObject);
        this.getVisualObject().addOverPrintImage(this.getVOImage());
    }

}

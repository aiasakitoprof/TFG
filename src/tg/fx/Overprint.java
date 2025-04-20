package src.tg.fx;


import src.tg.helper.DoubleVector;
import src.tg.local.vo.VO;
import src.tg.local.vo.VOImage;


/**
 * @author juanm
 */
public class Overprint extends Animation {

    private static VOImage[] voImages = new VOImage[OverprintType.values().length];
    private static String assetsPath = "src/tg/fx/assets/Overprints/";
    private static VOImage voImage;

    static {
        Overprint.staticInit();
    }


    /**
     * CONSTRUCTOR
     *
     * @param voImage
     * @param degreesInc
     * @param delayMillis
     */
    public Overprint(OverprintType id) {

        this.setVOImage(Overprint.voImages[id.ordinal()]);

        //this.getVisualObject().addOverPrintImage(this.voImage);
        this.getVOImage().setAnimation(this);
        this.setAnimationType(AnimationType.OVERPRINT);

    }


    /**
     * CONSTRUCTOR
     *
     * @param bfImage
     * @param scale
     * @param degreesInc
     * @param delayMillis
     */
    public Overprint(OverprintType id, double scale) {

        this.setVOImage(Overprint.voImages[id.ordinal()].clone());
        this.getVOImage().setScale(scale);

        this.getVOImage().setAnimation(this);
        this.setAnimationType(AnimationType.OVERPRINT);
    }


    public Overprint(OverprintType id, double scale, DoubleVector offset) {

        this.setVOImage(Overprint.voImages[id.ordinal()].clone());

        this.getVOImage().setScale(scale);
        this.getVOImage().setOffset(offset);

        this.getVOImage().setAnimation(this);
        this.setAnimationType(AnimationType.OVERPRINT);
    }


    /**
     * STATICS
     */
    public static VOImage getVOImage(int index) {
        return voImages[index];
    }


    private static void staticInit() {
        // Allow to initialize static variables 
        Overprint.loadAnimations();
    }


    public static void loadAnimations() {
        Overprint.addAnimation(OverprintType.CRACK_1, "CRACKS-1.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.CRACK_2, "CRACKS-2.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.CRACK_3, "CRACKS-3.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.CRACK_4, "CRACKS-4.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.CRACK_5, "CRACKS-5.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.SHOT_HOLE_1, "SHOT-HOLE-1.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.SHOT_HOLE_2, "SHOT-HOLE-2.PNG", AnimationType.OVERPRINT);
        Overprint.addAnimation(OverprintType.SHOT_HOLE_3, "SHOT-HOLE-3.PNG", AnimationType.OVERPRINT);
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
    public static boolean addAnimation(OverprintType id, String overprintName, AnimationType type) {

        VOImage voImage = new VOImage(Animation.loadImage(Overprint.assetsPath + overprintName));

        if (voImage.getImage() == null) {
            return false;
        }

        Overprint.voImages[id.ordinal()] = voImage;
        System.out.println(overprintName + " · " + id.ordinal());
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
